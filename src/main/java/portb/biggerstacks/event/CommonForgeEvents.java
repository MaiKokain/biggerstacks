/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.event;

import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.util.ConfigCommand;

import java.nio.file.Files;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class CommonForgeEvents
{
    private final static Style                     SETUP_COMMAND_SHORTCUT = Style.EMPTY.withClickEvent(new ClickEvent(
            ClickEvent.Action.RUN_COMMAND,
            "/biggerstacks quicksetup"
    )).withColor(TextFormatting.BLUE).setUnderlined(true);
    private final static Style                     WIKI_LINK              = Style.EMPTY.withClickEvent(new ClickEvent(
            ClickEvent.Action.OPEN_URL,
            "https://codeberg.org/PORTB/BiggerStacks/wiki"
    )).withColor(TextFormatting.BLUE).setUnderlined(true);
    private final static IFormattableTextComponent BULLET_POINT           = new StringTextComponent("\n> ").withStyle(
            TextFormatting.WHITE);
    
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
            if (FMLEnvironment.dist.isDedicatedServer() &&
                        !event.getEntity().hasPermissions(Constants.CHANGE_STACK_SIZE_COMMAND_PERMISSION_LEVEL))
                return;
            
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
}
