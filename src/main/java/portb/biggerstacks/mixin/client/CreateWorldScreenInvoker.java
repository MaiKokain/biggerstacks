package portb.biggerstacks.mixin.client;

import net.minecraft.client.gui.screen.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreateWorldScreen.class)
public interface CreateWorldScreenInvoker
{
    @Invoker("onCreate")
    void invokeOnCreate();
}