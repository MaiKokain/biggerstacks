/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.prettypipes;

import de.ellpeck.prettypipes.pipe.modules.retrieval.RetrievalModuleItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.util.SlotLimitHelper;

@Mixin(RetrievalModuleItem.class)
public class RetrievalModuleItemMixin
{
    @Redirect(method = "tick",
              at = @At(value = "FIELD",
                       target = "Lde/ellpeck/prettypipes/pipe/modules/retrieval/RetrievalModuleItem;maxExtraction:I",
                       opcode = Opcodes.GETFIELD),
              require = 0,
              remap = false)
    private int increaseTransferRate(RetrievalModuleItem instance)
    {
        int rate = ((RetrievalModuleItemAccessor) instance).getMaxExtractionRate();
        
        if (ServerConfig.get().increaseTransferRate.get())
            return SlotLimitHelper.scaleTransferRate(rate, false);
        else
            return rate;
    }
}