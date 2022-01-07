package portb.biggerstacks.mixin;

import net.minecraft.server.commands.GiveCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GiveCommand.class)
public class GiveCommandMixin
{
    //minecraft limits give command to 100 stacks, but when it multiplies MAXINT by 100 it has an overflow
    @ModifyVariable(method = "giveItem", at = @At("STORE"), ordinal = 2)
    private static int preventIntegerOverflow(int value)
    {
        if (value < 0)
            return Integer.MAX_VALUE;
        else
            return value;
    }
}
