package portb.biggerstacks.mixin.compat.colossalchests;

import org.cyclops.colossalchests.inventory.container.ContainerColossalChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.ServerConfig;

@Mixin(ContainerColossalChest.class)
public class ContainerColossalChestMixin
{
    @ModifyConstant(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/FriendlyByteBuf;)V", constant = @Constant(intValue = 64), require = 0)
    private static int increaseStackSize(int value)
    {
        return ServerConfig.INSTANCE.maxStackCount.get();
    }
}
