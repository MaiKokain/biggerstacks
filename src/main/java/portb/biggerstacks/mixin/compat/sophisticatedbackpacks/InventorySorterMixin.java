package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.p3pp3rf1y.sophisticatedbackpacks.util.InventorySorter;
import net.p3pp3rf1y.sophisticatedbackpacks.util.ItemStackKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.ItemExtension;

/**
 * Fixes items being overstacked when using the sort function in a sophisticated backpack
 */
@Mixin(InventorySorter.class)
public class InventorySorterMixin
{
    @ModifyConstant(method = "placeStack", constant = @Constant(intValue = 64), remap = false)
    private static int increaseStackLimit(int val){
        return AutoSidedConfig.getMaxStackSize();
    }
}
