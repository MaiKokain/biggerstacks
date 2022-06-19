package portb.biggerstacks.mixin.compat.rftools;

import mcjty.lib.container.GenericItemHandler;
import mcjty.lib.tileentity.GenericTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import portb.biggerstacks.config.AutoSidedConfig;

import java.util.function.Supplier;

@Mixin(GenericItemHandler.Builder.class)
public class GenericItemHandlerBuilderMixin
{
    @Inject(method = "<init>", at = @At("TAIL"), require = 0)
    private void increaseStackLimit(GenericTileEntity te, Supplier<?> factorySupplier, CallbackInfo ci)
    {
        ((BuilderAccessor) this).setSlotLimit(AutoSidedConfig.getMaxStackSize());
    }
}
