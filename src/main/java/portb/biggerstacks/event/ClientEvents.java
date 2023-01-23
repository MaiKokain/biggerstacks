/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID)
public class ClientEvents
{
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
            event.getToolTip()
                 .add(1,
                      Component.translatable("biggerstacks.exact.count",
                                             Component.literal(TOOLTIP_NUMBER_FORMAT.format(stack.getCount()))
                                                      .withStyle(ChatFormatting.DARK_AQUA)
                               )
                               .withStyle(ChatFormatting.GRAY)
                 );
        }
    }
    
    /**
     * Unloads the ruleset when the client disconnects from a world/server
     */
    @SubscribeEvent
    static public void forgetRuleset(ClientPlayerNetworkEvent.LoggingOut event)
    {
        if (event.getPlayer() != null)
        {
            StackSizeRules.setRuleSet(null);
        }
    }
}
