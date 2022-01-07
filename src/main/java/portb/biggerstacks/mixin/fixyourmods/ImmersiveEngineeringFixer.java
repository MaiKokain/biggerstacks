package portb.biggerstacks.mixin.fixyourmods;

import blusunrize.immersiveengineering.common.gui.BlockEntityInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.ServerConfig;

@Mixin(BlockEntityInventory.class)
class ImmersiveEngineeringFixer
{
    @Inject(method = {"getMaxStackSize", "m_6893_"}, at = @At("RETURN"), cancellable = true, remap = false, require = 0)
    private void fixMaxStackSize(CallbackInfoReturnable<Integer> returnInfo)
    {
        if(returnInfo.getReturnValue() == 64)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(ServerConfig.maxStackCount.get());
        }
    }
}
