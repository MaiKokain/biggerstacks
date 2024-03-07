package portb.biggerstacks.mixin.compat.ae2;

import appeng.client.gui.AEBaseScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(AEBaseScreen.class)
public class AEBaseScreenMixin
{
    @ModifyConstant(method = "checkHotbarKeyPressed", constant = @Constant(intValue = 64), remap = false)
    private int increaseStackLimit(int val)
    {
        return StackSizeHelper.getNewStackSize();
    }
}