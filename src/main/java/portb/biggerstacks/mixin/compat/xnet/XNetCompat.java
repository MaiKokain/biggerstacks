package portb.biggerstacks.mixin.compat.xnet;

import mcjty.xnet.apiimpl.items.ItemChannelSettings;
import mcjty.xnet.apiimpl.items.ItemConnectorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin({
        ItemConnectorSettings.class,
        ItemChannelSettings.class
})
public class XNetCompat
{
    @ModifyConstant(method = {"tickItemHandler", "createGui"},
                    constant = @Constant(intValue = 64),
                    require = 0,
                    remap = false)
    private int increaseMaxExtractAmount(int value)
    {
        if (AutoSidedConfig.increaseTransferRate())
        {
            return AutoSidedConfig.getMaxStackSize();
        }
        else
        {
            return value;
        }
    }
}
