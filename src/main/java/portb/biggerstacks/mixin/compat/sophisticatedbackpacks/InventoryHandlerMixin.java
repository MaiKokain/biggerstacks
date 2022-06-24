package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(InventoryHandler.class)
public class InventoryHandlerMixin
{
    @ModifyVariable(method = "setSlotLimit", at = @At("HEAD"), ordinal = 0, remap = false, argsOnly = true)
    private int scaleSlotLimit(int slotLimit)
    {
        //Avoid potentially returning 0 with math.max
        return Math.max(1, slotLimit * AutoSidedConfig.getMaxStackSize() / 64);
    }

    @ModifyConstant(method = "setSlotLimit", constant = @Constant(intValue = 64), remap = false)
    private int increaseStackLimit(int val)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}
