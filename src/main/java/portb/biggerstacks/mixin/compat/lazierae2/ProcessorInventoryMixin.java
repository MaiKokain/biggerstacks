package portb.biggerstacks.mixin.compat.lazierae2;

import com.almostreliable.lazierae2.content.processor.ProcessorInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(ProcessorInventory.class)
public class ProcessorInventoryMixin
{
    @ModifyConstant(method = "getSlotLimit", constant = @Constant(intValue = 64), remap = false)
    private int increaseStackLimit(int val)
    {
        return StackSizeHelper.getNewSlotLimit();
    }
}
