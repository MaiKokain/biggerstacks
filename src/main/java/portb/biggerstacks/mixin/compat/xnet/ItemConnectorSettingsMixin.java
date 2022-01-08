package portb.biggerstacks.mixin.compat.xnet;

import mcjty.xnet.apiimpl.items.ItemChannelSettings;
import mcjty.xnet.apiimpl.items.ItemConnectorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.ServerConfig;

@Mixin({
        ItemConnectorSettings.class,
        ItemChannelSettings.class
})
public class ItemConnectorSettingsMixin
{
    @ModifyConstant(method = {"tickItemHandler", "createGui"}, constant = @Constant(intValue = 64), require = 1, remap = false)
    private int increaseMaxExtractAmount(int value)
    {
        return ServerConfig.INSTANCE.maxStackCount.get();
    }
}
