/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.refinedstorage;

import com.refinedmods.refinedstorage.inventory.item.UpgradeItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.SlotLimitHelper;

@Mixin(UpgradeItemHandler.class)
public class UpgradeItemHandlerMixin
{
    //increases the transfer rate for importers and similar things
    @Inject(method = "getStackInteractCount", at = @At("RETURN"), cancellable = true, require = 0, remap = false)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        if (AutoSidedConfig.increaseTransferRate())
            SlotLimitHelper.scaleTransferRate(returnInfo, false);
    }
}
