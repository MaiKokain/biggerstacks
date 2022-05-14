package portb.biggerstacks.mixin.compat.rftools;

import mcjty.rftoolsstorage.storage.GlobalStorageItemWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(GlobalStorageItemWrapper.class)
public class GlobalStorageItemWrapperMixin
{
    @Inject(method = "getSlotLimit", at = @At("RETURN"), remap = false, require = 0, cancellable = true)
    private void fixSlotLimit(int slot, CallbackInfoReturnable<Integer> returnInfo)
    {
        if (returnInfo.getReturnValue() == 64)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(AutoSidedConfig.getMaxStackSize());
        }
    }
}
