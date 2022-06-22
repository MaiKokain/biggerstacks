package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(StorageContainerMenuBase.class)
public abstract class BackpackContainerMixin
{
    @ModifyConstant(method = "calculateMaxCountForStack", constant = @Constant(intValue = 64), remap = false)
    private static int increaseStackLimit(int val)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}
