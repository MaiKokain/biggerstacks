/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
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

import static portb.biggerstacks.Constants.TOOLTIP_NUMBER_FORMAT;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID)
public class ClientEvents
{
    private final static Style                     SETUP_COMMAND_SHORTCUT = Style.EMPTY.withClickEvent(new ClickEvent(
            ClickEvent.Action.RUN_COMMAND,
            "/biggerstacks quicksetup"
    )).withColor(TextFormatting.BLUE).withUnderlined(true);
    private final static Style                     WIKI_LINK              = Style.EMPTY.withClickEvent(new ClickEvent(
            ClickEvent.Action.OPEN_URL,
            "https://codeberg.org/PORTB/BiggerStacks/wiki"
    )).withColor(TextFormatting.BLUE).withUnderlined(true);
    private final static IFormattableTextComponent BULLET_POINT           = new StringTextComponent("\n> ").withStyle(
            TextFormatting.WHITE);
    
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
     *
     * @param event
     */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    static public void showExactItemStackCount(ItemTooltipEvent event)
    {
        if (!ClientConfig.enableNumberShortening.get())
            return;
        
        ItemStack stack = event.getItemStack();
        
        if (stack.getCount() > Constants.ONE_THOUSAND)
        {
            String                    countString    = TOOLTIP_NUMBER_FORMAT.format(stack.getCount());
            IFormattableTextComponent countComponent = new StringTextComponent(countString).withStyle(TextFormatting.DARK_AQUA);
            TextComponent tooltip = new TranslationTextComponent("biggerstacks.exact.count",
                                                                 countComponent
            );
            
            event.getToolTip().add(1, tooltip.withStyle(TextFormatting.GRAY));
        }
    }
    
    /**
     * Unloads the ruleset when the client disconnects from a world/server
     *
     * @param event
     */
    @OnlyIn(Dist.CLIENT)
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
            event.getEntity().sendMessage(
                    new StringTextComponent(
                            "Biggerstacks is installed, but you have not configured it and there are no other mods installed that use it.")
                            .append(BULLET_POINT)
                            .append("Run ")
                            .append(new StringTextComponent("/biggerstacks quicksetup").withStyle(SETUP_COMMAND_SHORTCUT))
                            .append(" and the mod will generate a simple ruleset for you")
                            .append(BULLET_POINT)
                            .append("Or click ")
                            .append(new StringTextComponent("here").withStyle(WIKI_LINK))
                            .append(" to see how to create a custom ruleset")
                            .withStyle(TextFormatting.GOLD),
                    Util.NIL_UUID
            );
        }
    }
}
