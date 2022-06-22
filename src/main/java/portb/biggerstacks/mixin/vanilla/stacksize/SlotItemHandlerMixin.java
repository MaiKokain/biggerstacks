package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.SlotItemHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageInventorySlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(SlotItemHandler.class)
public class SlotItemHandlerMixin
{
    @Inject(method = "getMaxStackSize()I", at = @At("RETURN"), cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        //Do not change slot size for sophisticated backpacks as it causes issues with overstacking.
        //noinspection ConstantConditions
        if (ModList.get()
                   .isLoaded(Constants.SOPHISTICATED_BACKPACKS) && ((Object) this) instanceof StorageInventorySlot)
            return;

        if (returnInfo.getReturnValue() == 64)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(AutoSidedConfig.getMaxStackSize());
        }
    }
}
