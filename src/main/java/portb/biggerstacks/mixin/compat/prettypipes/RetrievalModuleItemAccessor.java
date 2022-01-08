package portb.biggerstacks.mixin.compat.prettypipes;

import de.ellpeck.prettypipes.pipe.modules.extraction.ExtractionModuleItem;
import de.ellpeck.prettypipes.pipe.modules.retrieval.RetrievalModuleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RetrievalModuleItem.class)
public interface RetrievalModuleItemAccessor
{
    @Accessor(value = "maxExtraction", remap = false)
    int getMaxExtractionRate();
}
