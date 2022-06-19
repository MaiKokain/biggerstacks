package portb.biggerstacks.mixin.compat.immersiveengineering;

import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.config.AutoSidedConfig;

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
        int limit = inventory.getSlotLimit(slot);

        if (limit == 64)
            return AutoSidedConfig.getMaxStackSize();

        return limit;
    }
}
