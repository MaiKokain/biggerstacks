package portb.biggerstacks.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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

import java.text.DecimalFormat;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID)
public class ClientEvents
{
    private static final DecimalFormat TOOLTIP_NUMBER_FORMAT = new DecimalFormat("###,###,###,###,###,###");
    
    /**
     * Shows item count on the tooltip
     */
    @OnlyIn(Dist.CLIENT)
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
