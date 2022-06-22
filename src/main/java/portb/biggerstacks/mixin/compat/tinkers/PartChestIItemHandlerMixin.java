package portb.biggerstacks.mixin.compat.tinkers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import slimeknights.tconstruct.tables.tileentity.chest.PartChestTileEntity;

@Mixin(PartChestTileEntity.PartChestItemHandler.class)
public class PartChestIItemHandlerMixin
{
    /**
     * Increases stack limit for part chest to 1/8th of the maximum stack size
     */
    @ModifyConstant(method = "getSlotLimit", constant = @Constant(intValue = 8), remap = false)
    private int increaseStackLimit(int val)
    {
        return AutoSidedConfig.getMaxStackSize() / 8;
    }
}
