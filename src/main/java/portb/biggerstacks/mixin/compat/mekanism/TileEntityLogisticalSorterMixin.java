package portb.biggerstacks.mixin.compat.mekanism;

import mekanism.common.tile.TileEntityLogisticalSorter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(TileEntityLogisticalSorter.class)
public class TileEntityLogisticalSorterMixin
{
    @ModifyConstant(method = "onUpdateServer", constant = @Constant(intValue = 64), require = 0, remap = false)
    private int increaseStackSize(int val) {
        if(AutoSidedConfig.increaseTransferRate())
            return AutoSidedConfig.getMaxStackSize();
        else
            return val;
    }

}
