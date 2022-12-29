package portb.biggerstacks.event;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.util.ConfigCommand;

import java.nio.file.Files;
import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID)
public class ClientEvents
{
    private final static Style         SETUP_COMMAND_SHORTCUT = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                                                                                          "/biggerstacks quicksetup"
    )).withColor(ChatFormatting.BLUE).withUnderlined(true);
    private final static Style         WIKI_LINK              = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                                                                                                          "https://codeberg.org/PORTB/BiggerStacks/wiki"
    )).withColor(ChatFormatting.BLUE).withUnderlined(true);
    private final static Component     BULLET_POINT           = new TextComponent("\n> ").withStyle(ChatFormatting.WHITE);
    private static final DecimalFormat TOOLTIP_NUMBER_FORMAT  = new DecimalFormat("###,###,###,###,###,###");
    
    /**
     * Command is only registered for singleplayer. (this is not because it reaches across logical sides)
     *
     * @param event
     */
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event)
    {
        ConfigCommand.register(event);
    }
    
    /**
     * Shows item count on the tooltip
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    static public void showExactItemStackCount(ItemTooltipEvent event)
    {
        if (!ClientConfig.enableNumberShortening.get())
            return;
        
        var stack = event.getItemStack();
        
        if (stack.getCount() > Constants.ONE_THOUSAND)
        {
            String           countString    = TOOLTIP_NUMBER_FORMAT.format(stack.getCount());
            MutableComponent countComponent = new TextComponent(countString).withStyle(ChatFormatting.DARK_AQUA);
            var              tooltip        = new TranslatableComponent("biggerstacks.exact.count", countComponent);
            
            event.getToolTip().add(1, tooltip.withStyle(ChatFormatting.GRAY));
        }
    }
    
    /**
     * Unloads the ruleset when the client disconnects from a world/server
     */
    @SubscribeEvent
    static public void forgetRuleset(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        if (event.getPlayer() != null)
        {
            StackSizeRules.setRuleSet(null);
        }
    }
    
    /**
     * Instructs the player on what to do if they haven't set the mod up yet
     *
     * @param event
     */
    @SubscribeEvent
    public static void warnIfNoRulesetExists(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!Files.exists(Constants.RULESET_FILE) && StackSizeRules.maxRegisteredItemStackSize == 64)
        {
            //can't be bothered to make language entries for this
            event.getPlayer().sendMessage(
                    new TextComponent(
                            "Biggerstacks is installed, but you have not configured it and have no other mods that use it.")
                            .append(BULLET_POINT)
                            .append("Run ")
                            .append(new TextComponent("/biggerstacks quicksetup").withStyle(SETUP_COMMAND_SHORTCUT))
                            .append(" to configure it using a simple GUI")
                            .append(BULLET_POINT)
                            .append("Or click ")
                            .append(new TextComponent("here").withStyle(WIKI_LINK))
                            .append(" to see how to create a custom ruleset")
                            .withStyle(ChatFormatting.GOLD),
                    Util.NIL_UUID
            );
        }
    }
}
