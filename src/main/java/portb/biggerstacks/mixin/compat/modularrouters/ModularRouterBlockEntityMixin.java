/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.modularrouters;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.ModularRoutersHelper;

@Mixin(ModularRouterBlockEntity.class)
public class ModularRouterBlockEntityMixin
{
    @ModifyConstant(method = "compileUpgrades", constant = @Constant(intValue = 6), require = 0, remap = false)
    private int increaseMaxStackUpgrades(int constant)
    {
        if (AutoSidedConfig.increaseTransferRate())
            return ModularRoutersHelper.getMaxStackUpgrades();
        else
            return constant;
    }
}
