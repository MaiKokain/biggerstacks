package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(value = StorageContainerMenuBase.class)
public class StorageContainerMenuBaseMixin
{
    @ModifyConstant(method = "calculateMaxCountForStack",
                    constant = @Constant(intValue = 64),
                    require = 0,
                    remap = false)
    private static int increaseStackSize(int val)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}
