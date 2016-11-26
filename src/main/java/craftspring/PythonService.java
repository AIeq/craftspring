package craftspring;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

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
		tasksExecutor=Executors.newSingleThreadExecutor();
		tasksExecutor.execute(new	Runnable(){

					@Override
					public void run() {
						shutdown=false;
						py = Runtime.getRuntime().exec("cmd /c dir");
						runUpdateCycle();
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

	public static	void	runUpdateCycle()
	{
		int	t1=0;
		int	t2=0;
		String	command="";
		int	i=0;
		while(!shutdown){
			
			while(queueNonEmpty()){
//				System.out.println("P1");
				synchronized(queue){
					command=queue.removeFirst();
				}
				
//				System.out.println("P2");
				if(shutdown)
					break;
//				System.out.println("P3");
				if(i>queue.size())
					break;
				i++;
			}
			i=0;
			while(!shutdown&&(i<15))
					try {
						i++;
						Thread.currentThread();
						Thread.sleep(55);
//						System.out.println("P4");
					} catch (InterruptedException e) {
				// 	TODO Auto-generated catch block
						e.printStackTrace();
					}
//			System.out.println("P5");
		}
	}
	
	protected	synchronized static	boolean	queueNonEmpty()
	{
		return	queue.size()>0;
	}
}
