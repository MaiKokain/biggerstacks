package portb.biggerstacks.mixin.compat.ae2;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(targets = "appeng.api.inventories.ContainerAdapter")
public class ContainerAdapterMixin
{
    /**
     * I'm unsure what ContainerAdapter does, but this constant should probably be fixed.
     */
    @ModifyConstant(method = "getMaxStackSize", constant = @Constant(intValue = 64))
    private int increaseStackLimit(int val){
        return StackSizeHelper.getNewSlotLimit();
    }
}
