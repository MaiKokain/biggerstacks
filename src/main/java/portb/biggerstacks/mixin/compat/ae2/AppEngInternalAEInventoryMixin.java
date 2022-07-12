package portb.biggerstacks.mixin.compat.ae2;

import appeng.tile.inventory.AppEngInternalAEInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(AppEngInternalAEInventory.class)
public class AppEngInternalAEInventoryMixin
{
    @Inject(method = "getSlotLimit", at = @At("RETURN"), cancellable = true, remap = false)
    private void increaseStackLimit(int slot, CallbackInfoReturnable<Integer> cir)
    {
        StackSizeHelper.scaleSlotLimit(cir);
    }
}
