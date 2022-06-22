package portb.biggerstacks.mixin.compat.quark;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import vazkii.quark.content.management.entity.ChestPassengerEntity;

@Mixin(ChestPassengerEntity.class)
public abstract class ChestPassengerEntityMixin
{
    @ModifyConstant(method = "getMaxStackSize", constant = @Constant(intValue = 64), require = 0)
    private int increaseStackLimit(int val)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}
