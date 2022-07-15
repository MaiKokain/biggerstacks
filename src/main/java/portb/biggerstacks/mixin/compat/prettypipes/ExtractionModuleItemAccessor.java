package portb.biggerstacks.mixin.compat.prettypipes;

import de.ellpeck.prettypipes.pipe.modules.extraction.ExtractionModuleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExtractionModuleItem.class)
public interface ExtractionModuleItemAccessor
{
    @Accessor(value = "maxExtraction", remap = false)
    int getMaxExtractionRate();
}
