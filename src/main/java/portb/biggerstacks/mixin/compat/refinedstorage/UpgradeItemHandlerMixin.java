package portb.biggerstacks.mixin.compat.refinedstorage;

import com.refinedmods.refinedstorage.inventory.item.UpgradeItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.ServerConfig;

@Mixin(UpgradeItemHandler.class)
public class UpgradeItemHandlerMixin
{
    @Inject(method = "getStackInteractCount", at=@At("RETURN"),cancellable = true, require = 0, remap = false)
    private void fixStackInteractCount(CallbackInfoReturnable<Integer> returnInfo)
    {
        if(returnInfo.getReturnValue() == 64 && ServerConfig.INSTANCE.increaseTransferRate.get())
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(ServerConfig.INSTANCE.maxStackCount.get());
        }
    }
}
