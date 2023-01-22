/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.StackSizeRules;

import static portb.biggerstacks.Constants.TOOLTIP_NUMBER_FORMAT;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID)
public class ClientEvents
{
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
}
