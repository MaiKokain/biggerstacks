package portb.biggerstacks.mixin.compat.mekanism;

import org.spongepowered.asm.mixin.Mixin;
import mekanism.common.tile.TileEntityLogisticalSorter;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(TileEntityLogisticalSorter.class)
public class TileEntityLogisticalSorterMixin
{
    @ModifyConstant(method = "onUpdateServer", constant = @Constant(intValue = 64), require = 0, remap = false)
    private int increaseTransferRate(int val)
    {
        if (ServerConfig.get().increaseTransferRate.get())
            return StackSizeHelper.increaseTransferRate(val);
        else
            return val;
    }

}