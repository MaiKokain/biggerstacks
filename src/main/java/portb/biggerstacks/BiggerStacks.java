package portb.biggerstacks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.LocalConfig;
import portb.biggerstacks.config.ServerConfig;

import java.text.DecimalFormat;

@Mod(Constants.MOD_ID)
public class BiggerStacks
{
    public static final  TagKey<Item>  BLACKLIST_TAG         = ItemTags.create(new ResourceLocation(Constants.MOD_ID,
                                                                                                    "blacklist"
    ));
    public static final  TagKey<Item>  WHITELIST_TAG         = ItemTags.create(new ResourceLocation(Constants.MOD_ID,
                                                                                                    "whitelist"
    ));
    private static final DecimalFormat TOOLTIP_NUMBER_FORMAT = new DecimalFormat("###,###,###,###,###,###");

    public BiggerStacks()
    {
        MinecraftForge.EVENT_BUS.register(this);

        var context = ModLoadingContext.get();

        context.registerConfig(ModConfig.Type.CLIENT,
                               ClientConfig.SPEC,
                               Constants.MOD_ID + "-client.toml"
        );
        context.registerConfig(ModConfig.Type.CLIENT,
                               LocalConfig.INSTANCE.SPEC,
                               Constants.MOD_ID + "-local.toml"
        );
        context.registerConfig(ModConfig.Type.SERVER,
                               ServerConfig.INSTANCE.SPEC,
                               Constants.MOD_ID + "-server.toml"
        );
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void showExactItemStackCount(ItemTooltipEvent event)
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
}
