package portb.biggerstacks.mixin.compat.prettypipes;

import de.ellpeck.prettypipes.pipe.modules.extraction.ExtractionModuleItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import portb.biggerstacks.PrettyPipesHelper;
import portb.biggerstacks.config.ServerConfig;

@Mixin(ExtractionModuleItem.class)
public class ExtractionModuleItemFixer
{
    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lde/ellpeck/prettypipes/pipe/modules/extraction/ExtractionModuleItem;maxExtraction:I", opcode = Opcodes.GETFIELD), require = 0, remap = false)
    private int fixMaxExtractionRate(ExtractionModuleItem instance)
    {
        var rate = ((ExtractionModuleItemAccessor)instance).getMaxExtractionRate();

        if(!ServerConfig.INSTANCE.increaseTransferRate.get())
            return rate;

        if(rate == 1)
        {
            return 1;
        }
        else
        {
            return PrettyPipesHelper.calculateExtractionRate(rate);
        }
    }
}
