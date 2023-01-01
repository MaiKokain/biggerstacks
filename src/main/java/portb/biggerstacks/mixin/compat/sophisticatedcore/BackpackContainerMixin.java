/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.sophisticatedcore;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(StorageContainerMenuBase.class)
public abstract class BackpackContainerMixin
{
    @Redirect(method = "mergeItemStack(Lnet/minecraft/world/item/ItemStack;IIZZZ)Z",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;getMaxStackSize()I"))
    private int avoidDoubleIncreasingStackSize(Slot slot)
    {
        if (slot.container.getClass() == Inventory.class)
            //If the slot is an inventory slot, calling slot.getMaxStackSize() will end up increasing the stack size twice from 2 different mixins.
            //to avoid this, just call StackSizeHelper directly. Yes it's hacky but this whole stupid mod is just a massive crappy hack
            return StackSizeHelper.getNewStackSize();
        else
            return slot.getMaxStackSize();
    }
}
