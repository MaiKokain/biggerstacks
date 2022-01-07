package portb.biggerstacks.mixin.compat.modularrouters;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.ModularRoutersHelper;
import portb.biggerstacks.config.ServerConfig;

@Mixin(ModularRouterBlockEntity.class)
public class ModularRouterBlockEntityMixin
{
    @ModifyConstant(method = "compileUpgrades", constant = @Constant(intValue = 6), require = 0, remap = false)
    private int increaseMaxStackUpgrades(int constant)
    {
        if (ServerConfig.INSTANCE.increaseTransferRate.get())
        {
            return ModularRoutersHelper.getMaxStackUpgrades();
        }
        else
        {
            return constant;
        }
    }
}
