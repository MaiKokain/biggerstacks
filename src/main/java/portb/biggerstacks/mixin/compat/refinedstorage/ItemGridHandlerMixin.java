/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.refinedstorage;

import com.refinedmods.refinedstorage.apiimpl.network.grid.handler.ItemGridHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(ItemGridHandler.class)
public class ItemGridHandlerMixin
{
    @ModifyConstant(method = "onExtract(Lnet/minecraft/entity/player/ServerPlayerEntity;Ljava/util/UUID;II)V",
                    constant = @Constant(intValue = 64),
                    remap = false,
                    require = 0)
    private int increaseStackLimit(int value)
    {
        return StackSizeHelper.getNewStackSize();
    }
}
