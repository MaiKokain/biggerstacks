package portb.biggerstacks.mixin.compat.rftools;

import mcjty.lib.container.GenericItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(GenericItemHandler.class)
public class GenericItemHandlerMixin
{
    @Inject(method = "getSlotLimit", at = @At("RETURN"), require = 0, remap = false, cancellable = true)
    private void increaseStackLimit(int slot, CallbackInfoReturnable<Integer> returnInfo)
    {
        StackSizeHelper.scaleSlotLimit(returnInfo);
    }

}
