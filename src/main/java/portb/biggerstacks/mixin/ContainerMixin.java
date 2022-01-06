package portb.biggerstacks.mixin;

import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import portb.biggerstacks.BiggerStacks;

@Mixin(Container.class)
public interface ContainerMixin extends Container
{
    @Override
    default int getMaxStackSize()
    {
        return BiggerStacks.MAX_STACK_SIZE;
    }
}
