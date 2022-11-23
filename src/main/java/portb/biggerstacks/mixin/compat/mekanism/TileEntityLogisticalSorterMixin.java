package portb.biggerstacks.mixin.compat.mekanism;

import mekanism.common.tile.TileEntityLogisticalSorter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(TileEntityLogisticalSorter.class)
public class TileEntityLogisticalSorterMixin
{
    @ModifyConstant(method = "onUpdateServer", constant = @Constant(intValue = 64), require = 0, remap = false)
    private int increaseTransferRate(int val)
    {
        if (AutoSidedConfig.increaseTransferRate())
            return StackSizeHelper.increaseTransferRate(val);
        else
            return val;
    }
    
}
