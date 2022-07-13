package portb.biggerstacks.mixin.compat.ae2;

import appeng.util.inv.AppEngInternalInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(value = AppEngInternalInventory.class, remap = false)
public class AppEngInternalInventoryMixin
{
    @ModifyVariable(method = "<init>(Lappeng/util/inv/InternalInventoryHost;IILappeng/util/inv/filter/IAEItemFilter;)V", ordinal = 1, at = @At("HEAD"),
                    argsOnly = true)
    private static int increaseStackLimitInConstructor(int value){
        return StackSizeHelper.scaleSlotLimit(value);
    }
    
    @ModifyVariable(method = "setMaxStackSize", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private int increaseStackLimit(int value){
        return StackSizeHelper.scaleSlotLimit(value);
    }
}
