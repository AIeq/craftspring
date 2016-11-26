import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = CraftSpring.MODID, name = CraftSpring.NAME, 
	version = CraftSpring.VERSION)
public class CraftSpring {

	public static final String MODID = "craftspring";
	public static final String NAME = "The Craft Spring";
    public static final String VERSION = "0.1a";
    
    @Mod.Instance(MODID)
    public	static	CraftSpring	instance;
    
    @Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {

	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
//    	commandHandler=new	SixthSenseCommand();
    	MinecraftForge.EVENT_BUS.register(new	CraftSpringEventHandler());
//        ClientCommandHandler.instance.registerCommand(commandHandler);
    }
    
    @Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
    	
	}
    
    protected	void	registerMinions()
    {
    	EntityRegistry.registerModEntity(EntityMinion.class, "Minion", 0,
    			CraftSpring.instance, 48, 55, true, 0xFF0000, 0x00FF00);
//    	registerEntityRenderingHandler(EntityMinion.class, RenderNewMob::new);
    }
}
