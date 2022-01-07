package portb.biggerstacks.mixin;

import net.minecraft.world.Containers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.ServerConfig;

@Mixin(Containers.class)
public class ContainersMixin
{
    @ModifyConstant(method = "dropItemStack", constant = {@Constant(intValue = 21), @Constant(intValue = 10)})
    private static int scaleDroppedItemStackSize(int value)
    {
        return value * ServerConfig.INSTANCE.maxStackCount.get() / 64;
    }
}
