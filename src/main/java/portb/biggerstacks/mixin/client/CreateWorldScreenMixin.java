/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.gui.HighStackSizeWarning;
import portb.configlib.ConfigLib;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin
{
    /**
     * this.createButton = this.addRenderableWidget(new Button(i, this.height - 28, 150, 20, new TranslatableComponent("selectWorld.create"),
     * (p_170188_) -> {                     <------- this lambda
     * this.onCreate();
     * }
     * ));
     *
     * @author PORTB
     * @reason Shows warning for users who may have been relying on stack size cap before update. The method is only 1 function call and injecting and cancelling or redirecting is basically the same as overwriting at that point
     */
    @Overwrite(remap = false)
    void lambda$init$13(Button b)
    {
        wrapOnCreate();
    }
    
    @Redirect(method = "keyPressed",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;onCreate()V"))
    private void showWarningBeforeWorldLoad(CreateWorldScreen instance)
    {
        wrapOnCreate();
    }
    
    private void wrapOnCreate()
    {
        if (!ClientConfig.stfuWarning.get())
        {
            int max = ConfigLib.readRuleset(FMLPaths.CONFIGDIR.get().resolve(Constants.RULESET_FILE_NAME)).getMaxStacksize();
    
            //check if user may have been relying on value cap before update
            if (ServerConfig.LOCAL_INSTANCE.globalMaxStackSize.get() != 1 &&
                        max > ServerConfig.LOCAL_INSTANCE.globalMaxStackSize.get())
            {
                HighStackSizeWarning.createWarningScreen(max, () -> {
                    ((CreateWorldScreenInvoker) this).invokeOnCreate();
                });
        
                return;
            }
        }
        
        ((CreateWorldScreenInvoker) this).invokeOnCreate();
    }
}
