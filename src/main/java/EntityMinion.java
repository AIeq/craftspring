import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;

public class EntityMinion extends EntityWolf {

	public EntityMinion(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
		this.setNoAI(true);
		this.tasks.taskEntries.clear();
		this.targetTasks.taskEntries.clear();
	}

	
}
