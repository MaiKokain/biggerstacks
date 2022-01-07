package portb.biggerstacks.mixin.compat.rftools;

import mcjty.lib.container.GenericItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GenericItemHandler.Builder.class)
public interface BuilderAccessor
{
    @Accessor(value = "slotLimit", remap = false)
    void setSlotLimit(int value);
}
