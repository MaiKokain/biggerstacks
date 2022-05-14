package portb.biggerstacks.mixin.compat.prettypipes;

import de.ellpeck.prettypipes.pipe.modules.retrieval.RetrievalModuleItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.PrettyPipesHelper;

@Mixin(RetrievalModuleItem.class)
public class RetrievalModuleItemFixer
{
    @Redirect(method = "tick",
              at = @At(value = "FIELD",
                       target = "Lde/ellpeck/prettypipes/pipe/modules/retrieval/RetrievalModuleItem;maxExtraction:I",
                       opcode = Opcodes.GETFIELD),
              require = 0,
              remap = false)
    private int fixMaxExtractionRate(RetrievalModuleItem instance)
    {
        var rate = ((RetrievalModuleItemAccessor) instance).getMaxExtractionRate();

        if (!AutoSidedConfig.increaseTransferRate())
            return rate;

        if (rate == 1)
        {
            return 1;
        }
        else
        {
            return PrettyPipesHelper.calculateExtractionRate(rate);
        }
    }
}