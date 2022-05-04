package portb.biggerstacks.mixin.stacksize;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.ServerConfig;

//@Mixin(CompoundContainer.class)
public class CompoundContainerMixin
{
    //@Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void fixMaxStackSize(CallbackInfoReturnable<Integer> returnInfo)
    {
        if(returnInfo.getReturnValue() == 64)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(ServerConfig.INSTANCE.maxStackCount.get());
        }
    }
}
