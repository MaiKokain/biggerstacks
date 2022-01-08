package portb.biggerstacks.mixin.compat.refinedstorage;

import com.refinedmods.refinedstorage.apiimpl.network.grid.handler.ItemGridHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.ServerConfig;

@Mixin(ItemGridHandler.class)
public class RefinedStorageFixer
{
    @ModifyConstant(method = "onExtract(Lnet/minecraft/server/level/ServerPlayer;Ljava/util/UUID;II)V", constant = @Constant(intValue = 64), remap = false, require = 0)
    private int biggerStackExtract(int value)
    {
        return ServerConfig.INSTANCE.maxStackCount.get();
    }
}
