package portb.biggerstacks.mixin.compat.modularrouters;

import me.desht.modularrouters.item.upgrade.StackUpgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.ModularRoutersHelper;
import portb.biggerstacks.config.ServerConfig;

@Mixin(StackUpgrade.class)
public class ModularRoutersStackUpgradeMixin
{
    @Inject(method = "getStackLimit", at = @At("RETURN"), remap = false, require = 0, cancellable = true)
    private void increaseStackLimit(int slot, CallbackInfoReturnable<Integer> returnInfo)
    {
        if(ServerConfig.INSTANCE.increaseTransferRate.get())
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(ModularRoutersHelper.getMaxStackUpgrades());
        }
    }
}
