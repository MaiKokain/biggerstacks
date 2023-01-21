/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.vanilla;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RepairContainer.class)
public class RepairContainerMixin
{
    @Redirect(method = "onTake",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/inventory/IInventory;setItem(ILnet/minecraft/item/ItemStack;)V",
                       ordinal = 3))
    void fixStackedEnchantedBooksBeingDeleted(IInventory inputSlots, int i, ItemStack itemStack)
    {
        inputSlots.getItem(1).shrink(1);
    }
}
