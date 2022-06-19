package portb.biggerstacks.mixin.compat.quark;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import vazkii.quark.content.automation.block.be.ChuteBlockEntity;

@Mixin(targets = "vazkii/quark/content/automation/block/be/ChuteBlockEntity$1")
public class ChuteBlockEntityMixin
{
    @ModifyConstant(method = "getSlotLimit(I)I", constant = @Constant(intValue = 64), remap = false, require = 0)
    private int increaseStackLimit(int val)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}