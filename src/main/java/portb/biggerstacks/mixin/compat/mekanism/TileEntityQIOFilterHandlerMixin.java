/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.mekanism;

import mekanism.common.tile.qio.TileEntityQIOFilterHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.util.SlotLimitHelper;

@Mixin(value = TileEntityQIOFilterHandler.class, remap = false)
public class TileEntityQIOFilterHandlerMixin
{
    @ModifyConstant(method = "getMaxTransitCount", constant = @Constant(intValue = 64), require = 0)
    private int increaseTransferRate(int val)
    {
        if (ServerConfig.get().increaseTransferRate.get())
            return SlotLimitHelper.increaseTransferRate(val);
        else
            return val;
    }
    
    @ModifyConstant(method = "getMaxTransitCount", constant = @Constant(intValue = 32), require = 0)
    private int increaseUpgradeTransferRate(int val)
    {
        if (ServerConfig.get().increaseTransferRate.get())
            return SlotLimitHelper.scaleTransferRate(val, false);
        else
            return val;
    }
}
