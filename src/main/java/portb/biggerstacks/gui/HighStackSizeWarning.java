package portb.biggerstacks.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;

@OnlyIn(Dist.CLIENT)
public class HighStackSizeWarning
{
    public static void createWarningScreen(int max, Runnable after)
    {
        Component title = Component.literal("WARNING: Your maximum stack size is currently " +
                                                    Constants.TOOLTIP_NUMBER_FORMAT.format(max)).withStyle(
                ChatFormatting.RED);
        Component msg = Component.literal("Biggerstacks no longer uses the global maximum stack limit config value.")
                                 .append("\nIt now gets the global max stack limit from the rule with the highest stack limit.")
                                 .append("\n\nIf you do not intend for items to stack this high, please adjust your rules file,\nor run the ")
                                 .append(Component.literal("\"/biggerstacks quicksetup\"").withStyle(Style.EMPTY.withColor(
                                         0xb4ffb3)))
                                 .append(" command (in a temporary world) to generate a new rules file.")
                                 .append("\n\nIf you ")
                                 .append(Component.literal("do").withStyle(Style.EMPTY.withItalic(true))) //thanks java
                                 .append(" intend for items to stack this high, click proceed.")
                                 .append(Component.literal("\n\nThis warning will not show again.").withStyle(Style.EMPTY.withColor(
                                         0xff0000)));
        
        ClientConfig.stfuWarning.set(true);
        
        Minecraft.getInstance().setScreen(new ConfirmScreen(
                confirmed ->
                {
                    if (confirmed)
                        after.run();
                    else
                        Minecraft.getInstance().setScreen(null);
                }, title, msg, CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL
        ));
    }
}
