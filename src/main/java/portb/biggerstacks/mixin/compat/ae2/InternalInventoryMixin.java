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
        return StackSizeHelper.getNewSlotLimit();
    }
    //    /**
//     * Fixes shift-clicking from the crafting output slot making stacks of 64 and ignoring the new stack limit
//     * @return
//     */
//    int getSlotLimit()
//    {
//        return StackSizeHelper.getNewSlotLimit();
//    }
}
