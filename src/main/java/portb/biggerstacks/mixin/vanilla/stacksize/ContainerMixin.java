package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(IInventory.class)
public interface ContainerMixin extends IInventory
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
