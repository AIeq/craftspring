import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;

public class RenderMinion extends RenderSheep {

	public RenderMinion(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
		super(renderManagerIn, modelBaseIn, shadowSizeIn);
		// TODO Auto-generated constructor stub
	}

/*	@Override
	protected ResourceLocation getEntityTexture(EntitySheep entity)
	{
		if(entity instanceof EntityMinion){
			EntityMinion	ent=(EntityMinion)entity;
			switch(ent.color){
			case	BLACK:return	new ResourceLocation(CraftSpring.MODID,
					"textures/entity/black.png");
			case	WHITE:return	new ResourceLocation(CraftSpring.MODID,
					"textures/white.png");
			case	RED:return	new ResourceLocation(CraftSpring.MODID,
					"textures/red.png");
			case	GREEN:return	new ResourceLocation(CraftSpring.MODID,
					"textures/green.png");
			case	BLUE:return	new ResourceLocation(CraftSpring.MODID,
					"textures/blue.png");
			}
		}
		return super.getEntityTexture(entity);
	}*/
}
