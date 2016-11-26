

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
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;

public class PythonService {
	
	protected	static	ExecutorService	tasksExecutor=null;
	protected	static	Process py = null;
	protected	static	boolean	shutdown=false;
	
	protected	static	LinkedList<String>	queue;
	
	public	synchronized static	void init()
	{
		
		queue=new	LinkedList<String>();
		if(tasksExecutor!=null)
			shutdown();
		tasksExecutor=Executors.newFixedThreadPool(2);
		tasksExecutor.execute(new	Runnable(){

					@Override
					public void run() {
						shutdown=false;
						getPythonFile("Genome.py");
						File	pyFile=getPythonFile("AI.py");
						if(pyFile==null)
							return;
						try {
							py = Runtime.getRuntime().exec("python3 "+pyFile.getAbsolutePath());
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
						runListener(py);
						
						
					}
			
				}
			);
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
}
