package portb.biggerstacks.mixin.stacksize;

import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(Container.class)
public interface ContainerMixin extends Container
{
    @Override
    default int getMaxStackSize()
    {
        return AutoSidedConfig.maxStackSize();
    }
}
