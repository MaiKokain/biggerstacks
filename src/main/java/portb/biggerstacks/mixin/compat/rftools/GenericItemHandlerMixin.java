package portb.biggerstacks.mixin.compat.rftools;

import mcjty.lib.container.GenericItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(GenericItemHandler.class)
public class GenericItemHandlerMixin
{
    //todo: this is like the 3rd time you've written this for the same method
    @Inject(method = "getSlotLimit", at = @At("RETURN"), require = 0, remap = false, cancellable = true)
    private void fixSlotLimit(int slot, CallbackInfoReturnable<Integer> returnInfo)
    {
        returnInfo.cancel();
        returnInfo.setReturnValue(AutoSidedConfig.maxStackSize());
    }

}
