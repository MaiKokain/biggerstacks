package portb.biggerstacks.mixin.stacksize;

import net.minecraftforge.items.wrapper.InvWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(InvWrapper.class)
public class InvWrapperMixin
{
    @Redirect(method = "insertItem",
              at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/wrapper/InvWrapper;getSlotLimit(I)I"),
              remap = false)
    private int fixMaxStackSize(InvWrapper instance, int slot)
    {
        var value = instance.getSlotLimit(slot);

        if (value == 64)
        {
            return AutoSidedConfig.maxStackSize();
        }

        return value;
    }
}
