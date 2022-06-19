package portb.biggerstacks.mixin.compat.ae2;

import appeng.parts.automation.IOBusPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(IOBusPart.class)
public class IOBusPartMixin
{
    @Inject(method = "getOperationsPerTick", at = @At("RETURN"), cancellable = true, require = 0, remap = false)
    private void increaseTransferRate(CallbackInfoReturnable<Integer> returnInfo)
    {
        if (AutoSidedConfig.increaseTransferRate() && returnInfo.getReturnValue() != 1)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(returnInfo.getReturnValue() * AutoSidedConfig.getMaxStackSize() / 64);
        }
    }

}
