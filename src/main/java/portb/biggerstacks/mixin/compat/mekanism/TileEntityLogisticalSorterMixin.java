/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.mekanism;

import mekanism.common.tile.TileEntityLogisticalSorter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.util.SlotLimitHelper;

@Mixin(TileEntityLogisticalSorter.class)
public class TileEntityLogisticalSorterMixin
{
    @ModifyConstant(method = "onUpdateServer", constant = @Constant(intValue = 64), require = 0, remap = false)
    private int increaseStackLimit(int val)
    {
        if (ServerConfig.get().increaseTransferRate.get())
            return SlotLimitHelper.increaseTransferRate(val);
        else
            return val;
    }
    
}
