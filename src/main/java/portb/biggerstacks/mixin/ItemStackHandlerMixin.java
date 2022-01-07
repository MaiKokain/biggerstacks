package portb.biggerstacks.mixin;

import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.ServerConfig;

@Mixin(ItemStackHandler.class)
public class ItemStackHandlerMixin
{
    @Inject(method = "getSlotLimit", at = @At("RETURN"), cancellable = true, remap = false)
    private void increaseSlotLimit(int slot, CallbackInfoReturnable<Integer> returnInfo)
    {
        returnInfo.cancel();
        returnInfo.setReturnValue(ServerConfig.INSTANCE.maxStackCount.get());
    }
}
