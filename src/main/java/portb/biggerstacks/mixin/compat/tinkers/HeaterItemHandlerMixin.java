package portb.biggerstacks.mixin.compat.tinkers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;
import slimeknights.tconstruct.smeltery.block.entity.inventory.HeaterItemHandler;

@Mixin(HeaterItemHandler.class)
public class HeaterItemHandlerMixin
{
    /**
     * Increases slot size for the seared heater
     */
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 64))
    private static int increaseStackLimit(int val)
    {
        return StackSizeHelper.getNewSlotLimit();
    }
}