package portb.biggerstacks.mixin.compat.titanium;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(InventoryComponent.class)
public class InventoryComponentMixin
{
    @Inject(method = "getSlotLimit", at = @At("RETURN"), cancellable = true, remap = false, require = 0)
    private void increaseStackLimit(int slot, CallbackInfoReturnable<Integer> cir)
    {
        StackSizeHelper.scaleSlotLimit(cir);
    }
}
