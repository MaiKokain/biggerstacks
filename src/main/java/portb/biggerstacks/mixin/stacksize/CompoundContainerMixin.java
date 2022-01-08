package portb.biggerstacks.mixin.stacksize;

import net.minecraft.world.CompoundContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.ServerConfig;

@Mixin(CompoundContainer.class)
public class CompoundContainerMixin
{
    @Inject(method = "getMaxStackSize()I", at = @At("RETURN"), cancellable = true, remap = false)
    private void fixMaxStackSize(CallbackInfoReturnable<Integer> returnInfo)
    {
        if(returnInfo.getReturnValue() == 64)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(ServerConfig.INSTANCE.maxStackCount.get());
        }
    }
}
