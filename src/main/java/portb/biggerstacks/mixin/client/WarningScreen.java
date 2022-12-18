package portb.biggerstacks.mixin.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.Constants;
import portb.biggerstacks.StackSizeWarning;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.LocalConfig;
import portb.configlib.ConfigLib;

@Mixin(WorldOpenFlows.class)
public abstract class WarningScreen
{
    @Redirect(method = "doLoadLevel(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;ZZ)V",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows;doLoadLevel(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;ZZZ)V"))
    private void showWarning(WorldOpenFlows instance, Screen worldstem, String exception, boolean pld, boolean packrepository, boolean anotherBoolean)
    {
        if (!ClientConfig.stfuWarning.get())
        {
            int max = ConfigLib.readRuleset(FMLPaths.CONFIGDIR.get().resolve(Constants.RULESET_FILE_NAME)).getMaxStacksize();
            //read config file and find max stack size
            if (max > 10000 || max > LocalConfig.INSTANCE.maxStackCount.get())
            {
                StackSizeWarning.createWarningScreen(max, () -> {
            
                    ClientConfig.stfuWarning.set(true);
            
                    ((WorldFlowInvoker) instance).invokeDoLoadLevel(worldstem,
                                                                    exception,
                                                                    pld,
                                                                    packrepository,
                                                                    anotherBoolean
                    );
                });
                
                return;
            }
        }
        
        ((WorldFlowInvoker) instance).invokeDoLoadLevel(worldstem, exception, pld, packrepository, anotherBoolean);
    }
}
