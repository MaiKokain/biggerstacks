package portb.biggerstacks.mixin.compat.quark;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;
import vazkii.quark.base.handler.ProxiedItemStackHandler;

@Mixin(value = ProxiedItemStackHandler.class, remap = false)
public class ProxiedItemStackHandlerMixin
{
    @ModifyConstant(method = "getSlotLimit", constant = @Constant(intValue = 64), require = 0)
    private int increaseStackLimit(int val)
    {
        return StackSizeHelper.getNewSlotLimit();
    }
}
