package portb.biggerstacks.mixin.compat.sheiphiancore;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import portb.biggerstacks.config.AutoSidedConfig;
import shetiphian.core.common.inventory.InventoryInternal;

@Mixin(InventoryInternal.class)
public abstract class InventoryInternalMixin
{
    @ModifyVariable(method = "<init>(Lnet/minecraft/tileentity/TileEntity;Ljava/lang/String;IILjava/lang/String;Lshetiphian/core/common/inventory/IContainerCallback;)V",
                    at = @At("HEAD"),
                    ordinal = 1,
                    argsOnly = true)
    private static int increaseStackLimit(int val)
    {
        if (val != 1)
            //Avoid potentially returning 0 with math.max
            return Math.max(1, val * AutoSidedConfig.getMaxStackSize() / 64);
        else
            return val;
    }
}
