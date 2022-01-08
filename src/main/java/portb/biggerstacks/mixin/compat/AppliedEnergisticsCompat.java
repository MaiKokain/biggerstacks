package portb.biggerstacks.mixin.compat;

import appeng.parts.automation.IOBusPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.ServerConfig;

@Mixin(IOBusPart.class)
public class AppliedEnergisticsCompat
{
    @Inject(method = "getOperationsPerTick", at = @At("RETURN"), cancellable = true, require = 0, remap = false)
    private void scaleOperationsPerTick(CallbackInfoReturnable<Integer> returnInfo)
    {
        if(ServerConfig.INSTANCE.increaseTransferRate.get() && returnInfo.getReturnValue() != 1)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(returnInfo.getReturnValue() * ServerConfig.INSTANCE.maxStackCount.get() / 64);
        }
    }

}
