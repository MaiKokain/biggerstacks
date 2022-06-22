package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackInventorySlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.ItemExtension;

@Mixin(BackpackContainer.class)
public abstract class BackpackContainerMixin
{
    @ModifyConstant(method = "calculateMaxCountForStack", constant = @Constant(intValue = 64), remap = false)
    private static int increaseStackLimit(int val){
        return AutoSidedConfig.getMaxStackSize();
    }
}
