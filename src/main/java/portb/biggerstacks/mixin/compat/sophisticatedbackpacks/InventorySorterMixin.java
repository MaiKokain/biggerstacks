package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.p3pp3rf1y.sophisticatedbackpacks.util.InventorySorter;
import net.p3pp3rf1y.sophisticatedbackpacks.util.ItemStackKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.util.ItemExtension;
import portb.biggerstacks.config.AutoSidedConfig;

/**
 * Fixes items being overstacked when using the sort function in a sophisticated backpack
 */
@Mixin(InventorySorter.class)
public class InventorySorterMixin
{
    /**
     * When sorting a backpack, it assumes that the maximum stack size of an item will not be bigger than the limit of the backpack's slots.
     * This mixin returns the most limiting factor so that the number of items in a stack won't exceed the slot limit of the backpack.
     * @param stack The stack
     * @param handler The backpack itemhandler
     * @param current duplicate of stack
     * @param count not used
     * @param slot The slot index that is being considered
     * @return .
     */
    @Redirect(method = "placeStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxStackSize()I", ordinal = 1), require = 0)
    private static int considerSlotLimitWhenCalculatingAmountToPlace(ItemStack stack, IItemHandlerModifiable handler, ItemStackKey current, int count, int slot){
        return Math.min(stack.getMaxStackSize(), handler.getSlotLimit(slot));
    }

    /**
     * This mixin corrects an issue with integer division and considers the maximum stack size of the slot AND the item
     * being inserted into it to prevent overstacking.
     * @param a ignored
     * @param b ignored
     * @param handler Backpack inventory handler
     * @param current The current item stack key
     * @param count The amount of items to place
     * @param slot The index of the slot being placed into
     * @return .
     */
    @Redirect(method = "placeStack", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", ordinal = 0), require = 0, remap = false)
    private static int accountForIncreasedStackSizeWhenPlacingStack(int a, int b, IItemHandlerModifiable handler, ItemStackKey current, int count, int slot) {
        //consider the original max stack size of 64 if the item is blacklisted, else just use the new increased one
        int globalMaxStackSize = ((ItemExtension)current.getStack().getItem()).hasStackSizeBeenIncreased() ? AutoSidedConfig.getMaxStackSize() : 64;

        int slotLimit = handler.getSlotLimit(slot);

        //switch order of multiplication and division to prevent integer rounding to 0
        return Math.min(count, slotLimit * current.getStack().getMaxStackSize() / globalMaxStackSize);
    }

}
