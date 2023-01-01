package portb.biggerstacks.mixin.client;

import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
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

@Mixin(WorldSelectionList.WorldListEntry.class)
public class WorldSelectionListMixin
{
    @Inject(method = "loadWorld",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/gui/screens/worldselection/WorldSelectionList$WorldListEntry;queueLoadScreen()V"),
            cancellable = true)
    private void showWarningBeforeWorldLoad(CallbackInfo ci)
    {
        if (!ClientConfig.stfuWarning.get())
        {
            int max = ConfigLib.readRuleset(FMLPaths.CONFIGDIR.get().resolve(Constants.RULESET_FILE_NAME)).getMaxStacksize();
            
            //check if user may have been relying on value cap before update
            if (LocalConfig.INSTANCE.maxStackCount.get() != 1 &&
                        max > LocalConfig.INSTANCE.maxStackCount.get())
            {
                ci.cancel();
                
                HighStackSizeWarning.createWarningScreen(max, () -> {
                    WorldSelectionList$WorldListEntryInvoker invoker = ((WorldSelectionList$WorldListEntryInvoker) this);
                    invoker.invokeQueueLoadScreen();
                    invoker.getMinecraft().createWorldOpenFlows().loadLevel(invoker.accessScreen(),
                                                                            invoker.getSummary().getLevelId()
                    );
                });
            }
        }
    }
}
