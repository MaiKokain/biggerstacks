package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackInventoryHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackInventorySlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BackpackInventorySlot.class)
public interface BackpackInventorySlotAccessor
{
    @Accessor(value = "inventoryHandler", remap = false)
    BackpackInventoryHandler accessInventoryHandler();
}
