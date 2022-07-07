package portb.biggerstacks.mixin.compat.mekanism;

import mekanism.common.inventory.slot.BasicInventorySlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(BasicInventorySlot.class)
public class BasicInventorySlotMixin
{
    @ModifyVariable(method = "<init>(ILjava/util/function/BiPredicate;Ljava/util/function/BiPredicate;Ljava/util/function/Predicate;Lmekanism/api/IContentsListener;II)V",
                    at = @At(value = "HEAD"),
                    ordinal = 0,
                    require = 0,
                    remap = false,
                    argsOnly = true)
    private static int increaseStackLimit(int value)
    {
        return StackSizeHelper.scaleSlotLimit(value);
    }
}