package portb.biggerstacks.mixin.compat.sheiphiancore;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;
import shetiphian.core.common.inventory.InventoryInternal;

@Mixin(InventoryInternal.class)
public abstract class InventoryInternalMixin
{
    @ModifyVariable(method = "<init>(Lnet/minecraft/tileentity/TileEntity;Ljava/lang/String;IILjava/lang/String;Lshetiphian/core/common/inventory/IContainerCallback;)V",
                    at = @At("HEAD"),
                    ordinal = 1,
                    argsOnly = true,
                    remap = false)
    private static int increaseStackLimit(int val)
    {
        return StackSizeHelper.scaleSlotLimit(val);
    }
}
