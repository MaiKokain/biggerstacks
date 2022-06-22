package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStack.class)
public interface ItemStackAccessor
{
    @Accessor("count")
    void accessSetCount(int value);
}
