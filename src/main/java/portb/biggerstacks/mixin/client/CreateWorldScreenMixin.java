package portb.biggerstacks.mixin.client;

import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import portb.biggerstacks.config.DefaultServerConfig;
import portb.biggerstacks.config.ServerConfig;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin
{
    @Inject(method = "onCreate", at = @At("TAIL"))
    private void setDefaultStackSize(CallbackInfo callbackInfo)
    {
        //set the default stack size so the user doesn't have to do it manually every time
        ServerConfig.INSTANCE.maxStackCount.set(DefaultServerConfig.INSTANCE.maxStackCount.get());
    }
}
