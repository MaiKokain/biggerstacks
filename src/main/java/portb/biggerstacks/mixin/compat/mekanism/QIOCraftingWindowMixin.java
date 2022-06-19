package portb.biggerstacks.mixin.compat.mekanism;

import mekanism.common.content.qio.QIOCraftingWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(QIOCraftingWindow.class)
public class QIOCraftingWindowMixin
{
    @ModifyConstant(method = "calculateMaxCraftAmount", constant = @Constant(intValue = 64), require = 0, remap = false)
    private int increaseMaxStackSize(int val)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}
