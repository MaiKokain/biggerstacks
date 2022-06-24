package portb.biggerstacks.mixin.compat.ae2;

import appeng.parts.automation.ImportBusPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(ImportBusPart.class)
public class IOBusPartMixin
{
    @Inject(method = "calculateMaximumAmountToImport",
            at = @At("RETURN"),
            cancellable = true,
            require = 0,
            remap = false)
    private void increaseTransferRate(CallbackInfoReturnable<Integer> returnInfo)
    {
        if (AutoSidedConfig.increaseTransferRate() && returnInfo.getReturnValue() != 1)
        {
            //Avoid potentially returning 0 with math.max
            int increasedRate = Math.max(1, returnInfo.getReturnValue() * AutoSidedConfig.getMaxStackSize() / 64);
            returnInfo.cancel();
            returnInfo.setReturnValue(increasedRate);
        }
    }

}
