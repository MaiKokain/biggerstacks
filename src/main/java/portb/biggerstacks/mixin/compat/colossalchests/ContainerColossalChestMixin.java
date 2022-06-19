package portb.biggerstacks.mixin.compat.colossalchests;

import org.cyclops.colossalchests.inventory.container.ContainerColossalChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(ContainerColossalChest.class)
public class ContainerColossalChestMixin
{
    @ModifyConstant(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/network/PacketBuffer;)V",
                    constant = @Constant(intValue = 64),
                    remap = false,
                    require = 0)
    private static int increaseStackLimit(int value)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}
