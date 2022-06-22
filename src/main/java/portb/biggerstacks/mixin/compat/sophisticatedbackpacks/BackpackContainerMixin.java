package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageInventorySlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.ItemExtension;

/**
 * Fixes items being overstacked when inserting into sophisticated backpacks
 */
@Mixin(StorageContainerMenuBase.class)
public abstract class BackpackContainerMixin
{

    /**
     * When sorting a backpack, it assumes that the maximum stack size of an item will not be bigger than the limit of the backpack's slots.
     * This mixin returns the most limiting factor so that the maximum number of items that can fit in a slot will be correct.
     * @param slot The slot
     * @return .
     */
    @Redirect(method = "mergeItemStack(Lnet/minecraft/world/item/ItemStack;IIZZZ)Z",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/inventory/Slot;getMaxStackSize()I",
                       ordinal = 1))
    private int correctMaximumStackValue(Slot slot)
    {
        if(slot instanceof StorageInventorySlot)
            //getSlotLimit ignores the slot index, any value will work fine
            return Math.min(((BackpackInventorySlotAccessor) slot).accessInventoryHandler().getSlotLimit(0), slot.getMaxStackSize());
        else
            return slot.getMaxStackSize();
    }


    /**
     * The default implementation does not scale the upgraded stack size correctly with the new bigger stack limit.
     * This mixin scales it correctly and corrects an issue where the maximum stack size is returned as 0 due to integer
     * maths. It also considers the original stack size of blacklisted items so they don't have a smaller stack size when
     * put into a backpack with stack upgrades.
     * @param slotLimit The original call to slot.getMaxStackSize() is redirected. See {@link BackpackContainerMixin#correctMaximumStackValue}.
     * @param stack The stack to find the max size for
     * @return .
     */
    @Redirect(method = "mergeItemStack(Lnet/minecraft/world/item/ItemStack;IIZZZ)Z",
              at = @At(value = "INVOKE",
                       target = "Lnet/p3pp3rf1y/sophisticatedcore/common/gui/StorageContainerMenuBase;calculateMaxCountForStack(ILnet/minecraft/world/item/ItemStack;)I"),
              remap = false)
    private int calculateMaxCountForStack(int slotLimit, ItemStack stack)
    {
        //This prevents overstacking blacklisted items - blacklisted items will only have a max stack size of 64 (or less),
        // while the slot will have a higher stack limit, which would cause overstacking.
        int slotLimitOrMaxStackSize = Math.min(slotLimit, stack.getMaxStackSize());

        //consider the original max stack size of 64 if the item is blacklisted, else just use the new increased one
        var globalMaxStackSize = ((ItemExtension)stack.getItem()).hasStackSizeBeenIncreased() ? AutoSidedConfig.getMaxStackSize() : 64;

        //switch order of multiplication and division to prevent integer rounding to 0
        return slotLimitOrMaxStackSize * stack.getMaxStackSize() / globalMaxStackSize;
    }
}