package portb.biggerstacks.mixin.compat.tinkers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import slimeknights.tconstruct.tables.block.entity.chest.CastChestBlockEntity;

@Mixin(CastChestBlockEntity.CastChestIItemHandler.class)
public class CastChestIItemHandlerMixin
{
    /**
     * Increases stack limit for cast chest to 1/16th of the maximum stack size
     */
    @ModifyConstant(method = "getSlotLimit", constant = @Constant(intValue = 4), remap = false)
    private int increaseStackLimit(int val)
    {
        //Avoid potentially returning 0 with math.max
        return Math.max(1, AutoSidedConfig.getMaxStackSize() / 16);
    }
}
