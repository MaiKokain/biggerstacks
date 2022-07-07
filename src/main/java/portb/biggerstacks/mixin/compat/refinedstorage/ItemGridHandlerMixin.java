package portb.biggerstacks.mixin.compat.refinedstorage;

import com.refinedmods.refinedstorage.apiimpl.network.grid.handler.ItemGridHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(ItemGridHandler.class)
public class ItemGridHandlerMixin
{
    @ModifyConstant(method = "onExtract(Lnet/minecraft/server/level/ServerPlayer;Ljava/util/UUID;II)V",
                    constant = @Constant(intValue = 64),
                    remap = false,
                    require = 0)
    private int increaseStackLimit(int value)
    {
        return StackSizeHelper.getNewStackSize();
    }
}
