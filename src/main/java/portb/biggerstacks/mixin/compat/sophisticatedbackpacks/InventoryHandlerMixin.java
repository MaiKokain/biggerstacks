package portb.biggerstacks.mixin.compat.sophisticatedbackpacks;

import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackInventoryHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(BackpackInventoryHandler.class)
public class InventoryHandlerMixin
{
    @Shadow private int slotLimit;
    
    @ModifyVariable(method = "setSlotLimit", at = @At("HEAD"), ordinal = 0, remap = false, argsOnly = true)
    private int scaleSlotLimit(int slotLimit)
    {
        return StackSizeHelper.scaleSlotLimit(slotLimit);
    }

    @ModifyConstant(method = "setSlotLimit", constant = @Constant(intValue = 64), remap = false)
    private int increaseStackLimit(int val)
    {
        return StackSizeHelper.getNewSlotLimit();
    }
}
