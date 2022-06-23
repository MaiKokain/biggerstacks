package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.network.play.ServerPlayNetHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(ServerPlayNetHandler.class)
public class ServerGamePacketListenerImplMixin
{
    /**
     * Removes the hard coded limit to disallow giving more than 64 items in creative mode.
     */
    @ModifyConstant(method = "handleSetCreativeModeSlot", constant = @Constant(intValue = 64))
    private int increaseStackLimit(int value)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}