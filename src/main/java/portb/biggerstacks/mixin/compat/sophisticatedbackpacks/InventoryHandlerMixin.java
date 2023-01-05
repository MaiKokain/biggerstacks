/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackInventoryHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import portb.biggerstacks.util.SlotLimitHelper;

@Mixin(BackpackInventoryHandler.class)
public class InventoryHandlerMixin
{
    @ModifyVariable(method = "setSlotLimit", at = @At("HEAD"), ordinal = 0, remap = false, argsOnly = true)
    private int scaleSlotLimit(int slotLimit)
    {
        return SlotLimitHelper.scaleSlotLimit(slotLimit);
    }
    
    @ModifyConstant(method = "setSlotLimit", constant = @Constant(intValue = 64), remap = false)
    private int increaseStackLimit(int val)
    {
        return SlotLimitHelper.getNewStackSize();
    }
}
