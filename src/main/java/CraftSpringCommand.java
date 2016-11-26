import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CraftSpringCommand extends CommandBase {

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "of";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		if(args.length==0)
			return;
		if(args[0].equalsIgnoreCase("start"))
		{
			PythonService.init();
		}
		if(args[0].equalsIgnoreCase("stop"))
		{
			PythonService.shutdown();
		}
		
		if(args[0].equalsIgnoreCase("command"))
		{
			if(args.length>1){
				String	command=args[1];
				for(int	i=2;i<args.length;i++)
					command=command+" "+args[i];
				PythonService.addCommandToQueue(command);
			}
		}
	}

}
