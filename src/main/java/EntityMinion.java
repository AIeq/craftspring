import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.World;

public class EntityMinion extends EntitySheep {

	public	enum	Color{
		BLACK,
		WHITE,
		RED,
		GREEN,
		BLUE
	}
	
	public	Color	color=Color.BLACK;
	
	public EntityMinion(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
		this.setNoAI(true);
		this.tasks.taskEntries.clear();
		this.targetTasks.taskEntries.clear();
	}

	public	void	setColor(Color	color)
	{
		switch(color){
		case	BLACK:this.setFleeceColor(EnumDyeColor.BLACK);break;
		case	WHITE:this.setFleeceColor(EnumDyeColor.WHITE);break;
		case	RED:this.setFleeceColor(EnumDyeColor.RED);break;
		case	GREEN:this.setFleeceColor(EnumDyeColor.GREEN);break;
		case	BLUE:this.setFleeceColor(EnumDyeColor.BLUE);break;
		}
	}
}
