package portb.biggerstacks.mixin.compat.quark;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;
import vazkii.quark.content.management.entity.ChestPassengerEntity;

@Mixin(ChestPassengerEntity.class)
public abstract class ChestPassengerEntityMixin
{
    @ModifyConstant(method = "getInventoryStackLimit", constant = @Constant(intValue = 64), require = 0, remap = false)
    private int increaseStackLimit(int val)
    {
        return StackSizeHelper.getNewSlotLimit();
    }
}
