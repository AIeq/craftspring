

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class PythonService {
	
	protected	static	ExecutorService	tasksExecutor=null;
	protected	static	Process py = null;
	protected	static	boolean	shutdown=false;
//	protected   static  String interpreter = "C:\\Users\\Allu\\AppData\\Local\\Programs\\Python\\Python35-32\\python.exe " ;
	protected   static  String interpreter = "python3 ";
	
	protected	static	LinkedList<String>	queue;
	
	protected	static	int	pSize=10;
	protected	static	Hashtable<String,Integer>	minions=null;
	
	protected static	World	world=null;
	
	public	synchronized static	void init()
	{
		System.out.println("INIT!!!");
		world=Minecraft.getMinecraft().getIntegratedServer().getEntityWorld();
		queue=new	LinkedList<String>();
		
		if(tasksExecutor!=null)
			shutdown();
		tasksExecutor=Executors.newFixedThreadPool(4);
		tasksExecutor.execute(new	Runnable(){

					@Override
					public void run() {
						shutdown=false;
						preparePythonScript("ListGenome.py");
						preparePythonScript("Genome.py");
						File	pyFile=preparePythonScript("AI.py");

						if(pyFile==null){
							System.out.println("NO PYTHON SCRIPT!!!");

							return;
						}
						try {
							py = Runtime.getRuntime().exec(interpreter+pyFile.getAbsolutePath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						tasksExecutor.execute(new	Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								runSender(py);
							}
							
						});
						tasksExecutor.execute(new	Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								runErrorListener(py);
							}
							
						});
						tasksExecutor.execute(new	Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								runListener(py);
							}
							
						});						
						runUpdates();
					}
					
				}
			);
	}
	
	protected	static	void	runUpdates()
	{
//		System.out.println("U0");
		World	world=Minecraft.getMinecraft().getIntegratedServer().getEntityWorld();
		while(!shutdown){
//			System.out.println("U1");
			if(minions!=null){
				String	command="see";
				synchronized(minions){		
					for(int	i=0;i<minions.size();i++){
						command=command+" "+senseColor("minion_"+i,world);
					}
				}
				System.out.println(command);
				addCommandToQueue(command);
//				System.out.println("U2");
			}
			Thread.currentThread();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		System.out.println("U3");
	}
	
	public	synchronized	static	void	shutdown()
	{
		shutdown=true;
		tasksExecutor.shutdown();
		try {
			if(tasksExecutor.awaitTermination(6, TimeUnit.SECONDS)){
				tasksExecutor.shutdownNow();
				if(tasksExecutor.awaitTermination(6, TimeUnit.SECONDS))
					System.err.println("Cannot stop the scanner thread!");
				else
					tasksExecutor=null;
			}
			else
				tasksExecutor=null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tasksExecutor.shutdownNow();
			tasksExecutor=null;
		}
	}

	public static	void	runSender(Process	py)
	{
		BufferedWriter out = new BufferedWriter
		        (new OutputStreamWriter(py.getOutputStream()));
		String	command="";
		int	i=0;
		while(!shutdown){
			
			while(queueNonEmpty()){
//				System.out.println("P1");
				synchronized(queue){
					command=queue.removeFirst();
				}
//				System.out.println("COMMAND: "+command);
				if(shutdown){
					try {
						out.write("exit\n");
						out.flush();
						py.destroy();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				try {
					out.write(command+"\n");
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(shutdown){
					try {
						out.write("exit\n");
						out.flush();
						py.destroy();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				if(shutdown){
					try {
						out.write("exit\n");
						out.flush();
						py.destroy();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
//				System.out.println("P3");
				if(i>queue.size())
					break;
				i++;
			}
			i=0;
//			System.out.println("P4");
			Thread.currentThread();
			try {
				Thread.sleep(55);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println("P5");
		}
	}
	
	public static	void	runListener(Process	py)
	{
		BufferedReader in = new BufferedReader
		        (new InputStreamReader(py.getInputStream()));
		
		int	i=0;
		while(!shutdown){
			String line=null;
			try {
				while((line = in.readLine())!=null){
					interpret(line);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Thread.currentThread();
			try {
				Thread.sleep(55);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static	void	runErrorListener(Process	py)
	{
		BufferedReader in = new BufferedReader
		        (new InputStreamReader(py.getErrorStream()));
		
		int	i=0;
		while(!shutdown){
			String line=null;
			try {
				while((line = in.readLine())!=null){
					interpretError(line);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Thread.currentThread();
			try {
				Thread.sleep(55);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected	synchronized static	boolean	queueNonEmpty()
	{
		return	queue.size()>0;
	}
	
	public	static	File	preparePythonScript(String	scriptname)
	{
		//ResourceLocation	location=new	ResourceLocation(CraftSpring.MODID,"python/AI.py");
		InputStream	in=null;
/*		try {
			in=Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		URL url=null;
		try {
			url = new URL("https://raw.githubusercontent.com/AIeq/craftspring/master/src/main/resources/python/"+scriptname);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}   
		if(url==null)
			return	null;
		HttpURLConnection urlConnection = null;
	    try {
			urlConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    if(urlConnection!=null)
			try {
				in = new BufferedInputStream(urlConnection.getInputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		File	pyFile=null;
		if(in!=null)
		{
			BufferedWriter writer = null;
			pyFile=getPythonFile(scriptname);
			try {
				writer = new BufferedWriter(new FileWriter(pyFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(writer!=null)
			{
				
				BufferedReader	r=new	BufferedReader(new	InputStreamReader(in));
				String	line=null;
				try {
					while((line=r.readLine())!=null)
						try {
							writer.write(line+"\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					r.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(urlConnection!=null)
			urlConnection.disconnect();
		return pyFile;		
	}
	
	protected	static	File	getDataDir()
	{
//		String	world_dir=FMLCommonHandler.instance().getClientToServerNetworkManager().getRemoteAddress().toString().replaceAll(":.*", "");
		File	fDir=new	File(Loader.instance().getConfigDir()+"/"+CraftSpring.MODID+"/python");
		fDir.mkdirs();
		return	fDir;
	}
	
	protected	static	File	getPythonFile(String	scriptname)
	{
		return	new	File(getDataDir().getAbsolutePath()+"/"+scriptname);
	}
	
	protected	static	void	interpret(String	line)
	{
		displayToChat("\247a"+line);
		String[]	args=line.replace("\t", " ").split("\\s");
		
		if(args[0].equalsIgnoreCase("properties"))
		{
			int	j=0;
			minions=new	Hashtable<String,Integer>();
			synchronized(minions){
				for(int	i=1;i<args.length;i++)
				{
					if(args[i].length()==0)
						continue;
					System.out.println("SPAWNING "+args[i]);
					EntityMinion	entity=new	EntityMinion(world);
					entity.setCustomNameTag("minion_"+j);
					minions.put("minion_"+j, entity.getEntityId());
					j++;
					EntityMinion.Color	color=EntityMinion.Color.BLACK;
					if(args[i].equalsIgnoreCase("black"))
						color=EntityMinion.Color.BLACK;
					if(args[i].equalsIgnoreCase("white"))
						color=EntityMinion.Color.WHITE;
					if(args[i].equalsIgnoreCase("red"))
						color=EntityMinion.Color.RED;
					if(args[i].equalsIgnoreCase("green"))
						color=EntityMinion.Color.GREEN;
					if(args[i].equalsIgnoreCase("blue"))
						color=EntityMinion.Color.BLUE;
					entity.setColor(color);
					double	x=Minecraft.getMinecraft().thePlayer.posX+Math.random()*4;
					double	y=Minecraft.getMinecraft().thePlayer.posY;
					double	z=Minecraft.getMinecraft().thePlayer.posZ+Math.random()*4;
					float	yaw=Minecraft.getMinecraft().thePlayer.rotationYaw;
					float	pitch=Minecraft.getMinecraft().thePlayer.rotationPitch;
//					EntityGhast	entityIn=new	EntityGhast(world);
					entity.setPositionAndUpdate(x, y, z);
					world.spawnEntityInWorld(entity);
								
					entity.setLocationAndAngles(x, y, z, yaw, pitch);
				}
			}
			pSize=j;
		}
	}
	
	protected	static	void	interpretError(String	line)
	{
		displayToChat("\247c"+line);
	}
	
	public	static	void	displayToChat(String	msg)
	{
		Minecraft.getMinecraft().thePlayer.addChatMessage(new	TextComponentString(msg));
	}
	
	public	static	void addCommandToQueue(String	command)
	{
		synchronized(queue){
			queue.add(command);
		}
	}
	
	public	static	String	senseColor(String	name,World	world)
	{
		EntityMinion	entity=(EntityMinion) world.getEntityByID(minions.get(name));
		if(entity==null)
			return	"none";
		EnumFacing	facing=entity.getAdjustedHorizontalFacing();
		BlockPos	pos=null;
		int	i=0;
		switch(facing){
		case	EAST:do{
				pos=new	BlockPos(entity.posX+i,entity.posY,entity.posZ);
//				System.out.println("E");
				i++;
			}while((world.getBlockState(pos).getBlock().getUnlocalizedName().equalsIgnoreCase("tile.air"))&&(i<3));
			break;
		case	WEST:do{
				pos=new	BlockPos(entity.posX-i,entity.posY,entity.posZ);
//				System.out.println("W");
				i++;
		}while((world.getBlockState(pos).getBlock().getUnlocalizedName().equalsIgnoreCase("tile.air"))&&(i<3));
			break;
		case	NORTH:do{
				pos=new	BlockPos(entity.posX,entity.posY,entity.posZ-i);
//				System.out.println("N");
				i++;
		}while((world.getBlockState(pos).getBlock().getUnlocalizedName().equalsIgnoreCase("tile.air"))&&(i<3));
			break;
		case	SOUTH:do{
				pos=new	BlockPos(entity.posX,entity.posY,entity.posZ+i);
//				System.out.println("S");
				i++;
		}while((world.getBlockState(pos).getBlock().getUnlocalizedName().equalsIgnoreCase("tile.air"))&&(i<3));
			break;
		}
		if(pos!=null){
//			System.out.println("X: "+entity.posX+", "+"Y: "+entity.posY+", "+"Z: "+entity.posZ);
			return	senseBlock(pos,world);
		}
		return	"none";
	}
	
	public	static	String	senseBlock(BlockPos	pos,World	world)
	{
		IBlockState	blockState=world.getBlockState(pos);
		Block	block=blockState.getBlock();
		System.out.println("BLOCK: "+block.getUnlocalizedName());
		if(block.getUnlocalizedName().equalsIgnoreCase("tile.cloth"))
		{
			PropertyEnum PROPERTYCOLOUR = PropertyEnum.create("color", EnumDyeColor.class);
			EnumDyeColor	color=(EnumDyeColor) blockState.getValue(PROPERTYCOLOUR);
			switch(color){
			case	BLACK:return	"black";
			case	WHITE:return	"white";
			case	RED:return	"red";
			case	GREEN:return	"green";
			case	BLUE:return	"blue";
			}
			System.out.println("NO COLOR!!!");
		}
		return	"none";
	}
}
