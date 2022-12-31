package portb.biggerstacks.mixin.client;

import net.minecraft.client.gui.screen.WorldSelectionList;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.LocalConfig;
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
            int max = ConfigLib.readRuleset(FMLPaths.CONFIGDIR.get().resolve(Constants.RULESET_FILE_NAME)).getMaxStacksize();
            
            //check if user may have been relying on value cap before update
            if (LocalConfig.INSTANCE.globalMaxStackSize.get() != 1 &&
                        max > LocalConfig.INSTANCE.globalMaxStackSize.get())
            {
                ci.cancel();
                
                HighStackSizeWarning.createWarningScreen(max, () -> {
                    WorldSelectionList$WorldListEntryInvoker invoker = ((WorldSelectionList$WorldListEntryInvoker) this);
                    invoker.invokeQueueLoadScreen();
                    invoker.getMinecraft().loadLevel(invoker.getSummary().getLevelId());
                });
            }
        }
    }
}
