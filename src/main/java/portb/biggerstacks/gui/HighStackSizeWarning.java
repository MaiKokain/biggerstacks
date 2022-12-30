package portb.biggerstacks.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;

public class HighStackSizeWarning
{
    public static void createWarningScreen(int max, Runnable after)
    {
        Component title = new TextComponent("WARNING: Your maximum stack size is currently " +
                                                    Constants.TOOLTIP_NUMBER_FORMAT.format(max)).withStyle(
                ChatFormatting.RED);
        Component msg = new TextComponent("Biggerstacks no longer uses the global maximum stack limit config value.")
                                .append("\nIt now gets the global max stack limit from the rule with the highest stack limit.")
                                .append("\n\nIf you do not intend for items to stack this high, please adjust your rules file,\nor run the ")
                                .append(new TextComponent("\"/biggerstacks quicksetup\"").withStyle(Style.EMPTY.withColor(
                                        0xb4ffb3)))
                                .append(" command (in a temporary world) to generate a new rules file.")
                                .append("\n\nIf you ")
                                .append(new TextComponent("do").withStyle(Style.EMPTY.withItalic(true))) //thanks java
                                .append(" intend for items to stack this high, click proceed.")
                                .append(new TextComponent("\n\nThis warning will not show again.").withStyle(Style.EMPTY.withColor(
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
