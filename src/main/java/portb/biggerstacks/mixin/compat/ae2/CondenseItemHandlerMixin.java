package portb.biggerstacks.mixin.compat.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(targets = "appeng.tile.misc.CondenserTileEntity$CondenseItemHandler")
public class CondenseItemHandlerMixin
{
    @ModifyConstant(method = "getSlotLimit", constant = @Constant(intValue = 64), remap = false)
    private int increaseStackLimit(int val)
    {
        return StackSizeHelper.getNewSlotLimit();
    }
}
