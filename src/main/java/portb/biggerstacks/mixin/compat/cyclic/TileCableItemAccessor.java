package portb.biggerstacks.mixin.compat.cyclic;

import com.lothrazar.cyclic.block.cable.item.TileCableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TileCableItem.class)
public interface TileCableItemAccessor
{
    @Accessor("extractQty")
    void setExtractQty(int value);
}
