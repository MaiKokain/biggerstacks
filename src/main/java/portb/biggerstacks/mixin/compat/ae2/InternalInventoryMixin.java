package portb.biggerstacks.mixin.compat.ae2;

import appeng.api.inventories.InternalInventory;
import org.spongepowered.asm.mixin.Mixin;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(InternalInventory.class)
public interface InternalInventoryMixin extends InternalInventory
{
    @Override
    default int getSlotLimit(int slot)
    {
        return StackSizeHelper.getNewStackSize();
    }
}