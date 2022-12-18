package portb.biggerstacks;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class StackSizeWarning
{
    static public void createWarningScreen(int max, Runnable after)
    {
        Component title = Component.literal("WARNING: ").withStyle(ChatFormatting.RED).append(
                "Your maximum stack size is currently " + max);
        Component msg   = Component.literal(
                "Biggerstacks v3.1+ no longer uses the global maximum stack limit config value.\nIt no longer caps the stack limit of rules and instead finds the rule with the highest stacksize and uses that as the maximum stack limit.").append(
                "\n\n").append(
                "Please adjust your rules file if you did not intend for items to stack this high. Otherwise click proceed.").append(
                "\n\nThis warning will not show again.");
        
        Screen screen = new ConfirmScreen(confirmed ->
                                          {
                                              if (confirmed)
                                              {
                                                  after.run();
                                              }
                                              else
                                              {
                                                  Minecraft.getInstance().setScreen(null);
                                              }
                                          }, title, msg, CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL);
        
        Minecraft.getInstance().setScreen(screen);
    }
}
