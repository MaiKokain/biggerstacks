package portb.biggerstacks.mixin.compat.sophisticatedcore;

import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(InventoryHandler.class)
public class InventoryHandlerMixin
{
    @Inject(method = "getStackLimit", at = @At("RETURN"), cancellable = true, remap = false)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> cir)
    {
        StackSizeHelper.scaleSlotLimit(cir);
    }
}
