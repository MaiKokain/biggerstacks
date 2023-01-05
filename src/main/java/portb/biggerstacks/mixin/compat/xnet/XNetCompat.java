/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.xnet;

import mcjty.xnet.apiimpl.items.ItemChannelSettings;
import mcjty.xnet.apiimpl.items.ItemConnectorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.SlotLimitHelper;

@Mixin({
        ItemConnectorSettings.class,
        ItemChannelSettings.class
})
public class XNetCompat
{
    @ModifyConstant(method = {"tickItemHandler", "createGui"},
                    constant = @Constant(intValue = 64),
                    require = 0,
                    remap = false)
    private int increaseTransferRate(int value)
    {
        if (AutoSidedConfig.increaseTransferRate())
            return SlotLimitHelper.increaseTransferRate(value);
        else
            return value;
    }
}
