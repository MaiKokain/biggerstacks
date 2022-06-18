package portb.biggerstacks.mixin;

import net.minecraft.inventory.container.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Container.class)
public class ContainerMixin
{
    //the game gives you the bigger half in the mouse when you split a stack, but the way it does this produces a integer overflow when the stack is MAXINT items
    @ModifyArg(method = "doClick",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/inventory/container/Slot;remove(I)Lnet/minecraft/item/ItemStack;"),
               index = 0)
    private int preventIntegerOverflow(int value)
    {
        if (value == (Integer.MAX_VALUE + 1) / 2)
        {
            return Integer.MAX_VALUE / 2 + 1;
        }

        return value;
    }
}
