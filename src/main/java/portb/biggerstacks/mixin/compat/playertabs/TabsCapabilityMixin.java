package portb.biggerstacks.mixin.compat.playertabs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import wolforce.playertabs.TabsCapability;

@Mixin(TabsCapability.class)
public class TabsCapabilityMixin
{
    @ModifyConstant(require = 0,
                    method = {"saveInvToCurrTab", "writeTabToInv"},
                    constant = @Constant(intValue = 64),
                    remap = false)
    private int increaseMaxStackSize(int constant)
    {
        return AutoSidedConfig.maxStackSize();
    }
}
