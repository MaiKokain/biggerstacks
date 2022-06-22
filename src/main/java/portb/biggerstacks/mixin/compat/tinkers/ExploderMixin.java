package portb.biggerstacks.mixin.compat.tinkers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import slimeknights.tconstruct.gadgets.Exploder;

@Mixin(Exploder.class)
public class ExploderMixin
{
    /**
     * For EFLN explosions - increases the max stack size that all the items can be compressed into.
     * E.g. If an EFLN explodes 100 sandstone, it will normally compress into a stack of 64 and a stack of 46. If this
     * mod is used to increase the stack size to 100, it will compress into a single stack of 100.
     */
    @ModifyConstant(method = "finish", constant = @Constant(intValue = 64), remap = false)
    private static int increaseStackLimit(int val){
        return AutoSidedConfig.getMaxStackSize();
    }
}
