package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(Container.class)
public interface ContainerMixin extends Container
{
    /**
     * Increases the slot limit for vanilla containers.
     * Any (modded) containers that don't override this method will be effected by this. Containers that do override this
     * method will need their own compat.
     */
    @Override
    default int getMaxStackSize()
    {
        return StackSizeHelper.getNewSlotLimit();
    }
}
