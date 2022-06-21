package portb.biggerstacks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
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

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.MOD_ID)
public class BiggerStacks
{
    public static final IOptionalNamedTag<Item> BLACKLIST_TAG = ItemTags.createOptional(new ResourceLocation(Constants.MOD_ID,
                                                                                                             "blacklist"));
    public static final IOptionalNamedTag<Item> WHITELIST_TAG = ItemTags.createOptional(new ResourceLocation(Constants.MOD_ID,
                                                                                                             "whitelist"));

    private static final DecimalFormat TOOLTIP_NUMBER_FORMAT = new DecimalFormat("###,###,###,###,###,###");

    public BiggerStacks()
    {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get()
                         .registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, Constants.MOD_ID + "-client.toml");
        ModLoadingContext.get()
                         .registerConfig(ModConfig.Type.CLIENT,
                                         LocalConfig.INSTANCE.SPEC,
                                         Constants.MOD_ID + "-local.toml");
        ModLoadingContext.get()
                         .registerConfig(ModConfig.Type.SERVER,
                                         ServerConfig.INSTANCE.SPEC,
                                         Constants.MOD_ID + "-server.toml");
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public void showExactItemStackCount(ItemTooltipEvent event)
    {
        if (!ClientConfig.enableNumberShortening.get())
            return;

        ItemStack stack = event.getItemStack();

        if (stack.getCount() > Constants.ONE_THOUSAND)
        {
            event.getToolTip()
                 .add(1,
                      new TranslationTextComponent("biggerstacks.exact.count",
                                                   new StringTextComponent(TOOLTIP_NUMBER_FORMAT.format(stack.getCount())).withStyle(
                                                           TextFormatting.DARK_AQUA)).withStyle(TextFormatting.GRAY));
        }
    }
}
