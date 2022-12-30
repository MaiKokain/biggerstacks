package portb.biggerstacks.mixin.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.LocalConfig;
import portb.configlib.ConfigLib;

@Mixin(WorldOpenFlows.class)
public class WarningScreen
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
            if (max > 10000 || (LocalConfig.INSTANCE.maxStackCount.get() != 1 &&
                                        max > LocalConfig.INSTANCE.maxStackCount.get()))
            {
                createWarningScreen(max, () -> {
        
                    ClientConfig.stfuWarning.set(true);
        
                    ((WorldFlowInvoker) instance).invokeDoLoadLevel(
                            worldstem,
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
    
    static void createWarningScreen(int max, Runnable after)
    {
        Component title = Component.literal("WARNING: ").withStyle(ChatFormatting.RED).append(
                "Your maximum stack size is currently " + max);
        Component msg = Component.literal("Biggerstacks no longer uses the global maximum stack limit config value.")
                                 .append("\nIt now gets the global max stack limit from the rule with the highest stack size.")
                                 .append("\n\nPlease adjust your rules file if you did not intend for items to stack this high. Otherwise click proceed.")
                                 .append("\n\nThis warning will not show again.");
        
        Minecraft.getInstance().setScreen(new ConfirmScreen(
                confirmed ->
                {
                    if (confirmed)
                        after.run();
                    else
                        Minecraft.getInstance().setScreen(null);
                },
                title, msg, CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL
        ));
    }
}
