/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.cyclic;

import com.lothrazar.cyclic.block.cable.item.TileCableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.util.SlotLimitHelper;

@Mixin(value = TileCableItem.class, remap = false)
public class TileCableItemMixin
{
    @Inject(method = "<init>", at = @At("TAIL"), require = 0)
    private void increaseExtractQty(CallbackInfo callbackInfo)
    {
        //may need a world restart to take effect properly
        //as far as i can tell, this isn't actually changed anywhere,
        //i think it's a legacy thing anyway, you used to be able to set the extract
        //rate in the gui, but that doesn't seem like an option anymore
        if (ServerConfig.get().increaseTransferRate.get())
            ((TileCableItemAccessor) this).setExtractQty(SlotLimitHelper.increaseTransferRate(64));
    }
    
    @ModifyConstant(method = "normalFlow", constant = @Constant(intValue = 64), require = 0)
    private int increaseTransferRate(int value)
    {
        if (ServerConfig.get().increaseTransferRate.get())
            return SlotLimitHelper.increaseTransferRate(value);
        else
            return value;
    }
}
