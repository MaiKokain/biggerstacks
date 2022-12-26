package portb.biggerstacks.mixin.compat.sophisticatedcore;

import net.p3pp3rf1y.sophisticatedcore.util.InventorySorter;
import org.spongepowered.asm.mixin.Mixin;
import portb.biggerstacks.util.StackSizeHelper;

/**
 * Fixes items being overstacked when using the sort function in a sophisticated backpack
 */
@Mixin(InventorySorter.class)
public class InventorySorterMixin
{
    //@ModifyConstant(method = "placeStack", constant = @Constant(intValue = 64), remap = false)
    private static int increaseStackLimit(int val)
    {
        return StackSizeHelper.getNewStackSize();
    }
}
