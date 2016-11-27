import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
    
    public	static	ICommand	commandHandler;
    
    @Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
//    	MinecraftForge.EVENT_BUS.register(new	CraftSpringEventHandler());
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	commandHandler=new	CraftSpringCommand();
    	
        ClientCommandHandler.instance.registerCommand(commandHandler);
        registerMinions();
    }
    
    @Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
    	
	}
    
    protected	void	registerMinions()
    {
    	EntityRegistry.registerModEntity(EntityMinion.class, "Minion", 0, 
    			CraftSpring.instance, 64, 3, true,0xFF0000,0x00FF00);
//    	new	RenderWolf(Minecraft.getMinecraft().getRenderManager(), null, 0);
    	RenderingRegistry.registerEntityRenderingHandler(EntityMinion.class, 
    			new	RenderMinion(Minecraft.getMinecraft().getRenderManager(), 
    					new	ModelSheep1(), 0.75F));
//    	RenderingRegistry.registerEntityRenderingHandler(EntityMinion.class, 
//    			new	RenderLiving(new	ModelSilverfish(),0.75F));
    }
}
