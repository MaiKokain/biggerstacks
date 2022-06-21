package portb.biggerstacks.mixin.stacksize;

import net.minecraftforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(SlotItemHandler.class)
public class SlotItemHandlerMixin
{
    @Inject(method = "getMaxStackSize()I", at = @At("RETURN"), cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        //Unlike in 1.18, we do not need to check if the container is a sophisticated backpack because it is implemented
        // differently and never calls this method.

        if (returnInfo.getReturnValue() == 64)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(AutoSidedConfig.getMaxStackSize());
        }
    }
}
