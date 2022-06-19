package portb.biggerstacks.mixin;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin
{
    //the game gives you the bigger half in the mouse when you split a stack, but the way it does this produces a integer overflow when the stack is MAXINT items
    @ModifyArg(method = "doClick",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/world/inventory/Slot;tryRemove(IILnet/minecraft/world/entity/player/Player;)Ljava/util/Optional;"),
               index = 0)
    private int preventIntegerOverflow(int value)
    {
        //noinspection NumericOverflow
        if (value == (Integer.MAX_VALUE + 1) / 2)
            return Integer.MAX_VALUE / 2 + 1;

        return value;
    }
}
