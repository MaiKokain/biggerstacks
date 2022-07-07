package portb.biggerstacks.mixin.compat.immersiveengineering;

import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(IEInventoryHandler.class)
public class IEInventoryHandlerMixin
{
    @Redirect(method = "insertItem",
              at = @At(value = "INVOKE",
                       target = "Lblusunrize/immersiveengineering/common/util/inventory/IIEInventory;getSlotLimit(I)I"),
              remap = false,
              require = 0)
    private int increaseStackLimit(IIEInventory inventory, int slot)
    {
        return StackSizeHelper.scaleSlotLimit(inventory.getSlotLimit(slot));
    }
}
