/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.client;

import net.minecraft.client.gui.screen.WorldSelectionList;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.gui.HighStackSizeWarning;
import portb.configlib.ConfigLib;

@Mixin(WorldSelectionList.Entry.class)
public class WorldSelectionListMixin
{
    @Inject(method = "loadWorld",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/gui/screen/WorldSelectionList$Entry;queueLoadScreen()V"),
            cancellable = true)
    private void showWarningBeforeWorldLoad(CallbackInfo ci)
    {
        if (!ClientConfig.stfuWarning.get())
        {
            int configMax = ServerConfig.LOCAL_INSTANCE.globalMaxStackSize.get();
            int actualMax = ConfigLib.readRuleset(FMLPaths.CONFIGDIR.get().resolve(Constants.RULESET_FILE_NAME)).getMaxStacksize();
    
            //check if user may have been relying on value cap before update
            if (configMax != 1 && actualMax > configMax)
            {
                ci.cancel();
        
                HighStackSizeWarning.createWarningScreen(actualMax, () -> {
                    WorldSelectionList$WorldListEntryInvoker invoker = ((WorldSelectionList$WorldListEntryInvoker) this);
                    invoker.invokeQueueLoadScreen();
                    invoker.getMinecraft().loadLevel(invoker.getSummary().getLevelId());
                });
            }
        }
    }
}
