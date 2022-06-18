package portb.biggerstacks.mixin.stacksize;

import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(IInventory.class)
public interface IInventoryMixin extends IInventory
{
    @Override
    default int getMaxStackSize()
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}
