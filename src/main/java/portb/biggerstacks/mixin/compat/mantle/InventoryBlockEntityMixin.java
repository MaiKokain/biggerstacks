package portb.biggerstacks.mixin.compat.mantle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.util.StackSizeHelper;
import slimeknights.mantle.tileentity.InventoryTileEntity;

@Mixin(InventoryTileEntity.class)
public class InventoryBlockEntityMixin
{
    @Inject(method = "getInventoryStackLimit", at = @At("RETURN"), require = 0, cancellable = true, remap = false)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> cir)
    {
        StackSizeHelper.scaleSlotLimit(cir);
    }
}
