/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.world.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.SlotLimitHelper;

@Mixin(BundleItem.class)
public class BundleItemMixin
{
    @ModifyConstant(method = "getFullnessDisplay", constant = @Constant(floatValue = 64.0f))
    private static float increaseFloatStackLimit(float value)
    {
        return (float) SlotLimitHelper.getNewStackSize();
    }
    
    @ModifyConstant(method = {"overrideStackedOnOther", "getBarWidth", "getWeight", "appendHoverText", "add"},
                    constant = @Constant(intValue = 64))
    private static int increaseStackLimit(int value)
    {
        return SlotLimitHelper.getNewStackSize();
    }
}
