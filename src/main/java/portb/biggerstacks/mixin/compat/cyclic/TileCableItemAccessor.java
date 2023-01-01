/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.cyclic;

import com.lothrazar.cyclic.block.cable.item.TileCableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TileCableItem.class)
public interface TileCableItemAccessor
{
    @Accessor(value = "extractQty", remap = false)
    void setExtractQty(int value);
}
