package portb.biggerstacks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
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
import java.util.ArrayList;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.MOD_ID)
public class BiggerStacks
{
    public static final ArrayList<Class<?>> IGNORED_CLASSES = new ArrayList<>();

    public static final TagKey<Item> BLACKLIST_TAG = ItemTags.create(new ResourceLocation(Constants.MOD_ID,
                                                                                          "blacklist"));
    public static final TagKey<Item> WHITELIST_TAG = ItemTags.create(new ResourceLocation(Constants.MOD_ID,
                                                                                          "whitelist"));

    private static final DecimalFormat TOOLTIP_NUMBER_FORMAT = new DecimalFormat("###,###,###,###,###,###");

    //for debugging every packet sent ever
    static
    {
        IGNORED_CLASSES.add(ClientboundMoveEntityPacket.Rot.class);
        IGNORED_CLASSES.add(ClientboundMoveEntityPacket.PosRot.class);
        IGNORED_CLASSES.add(ClientboundMoveEntityPacket.Pos.class);
        IGNORED_CLASSES.add(ClientboundTeleportEntityPacket.class);
        IGNORED_CLASSES.add(ClientboundEntityEventPacket.class);
        IGNORED_CLASSES.add(ClientboundRotateHeadPacket.class);
        IGNORED_CLASSES.add(ClientboundSetEntityMotionPacket.class);
        IGNORED_CLASSES.add(ClientboundLevelChunkWithLightPacket.class);
        IGNORED_CLASSES.add(ClientboundSetEntityDataPacket.class);
        IGNORED_CLASSES.add(ClientboundBlockUpdatePacket.class);
        IGNORED_CLASSES.add(ClientboundSetTimePacket.class);
        IGNORED_CLASSES.add(ClientboundRemoveEntitiesPacket.class);

        IGNORED_CLASSES.add(ServerboundMovePlayerPacket.class);
        IGNORED_CLASSES.add(ServerboundMovePlayerPacket.Pos.class);
        IGNORED_CLASSES.add(ServerboundMovePlayerPacket.PosRot.class);
        IGNORED_CLASSES.add(ServerboundMovePlayerPacket.Rot.class);
        IGNORED_CLASSES.add(ServerboundMovePlayerPacket.StatusOnly.class);

        IGNORED_CLASSES.add(ClientboundAddMobPacket.class);
        IGNORED_CLASSES.add(ClientboundSectionBlocksUpdatePacket.class);
        IGNORED_CLASSES.add(ClientboundUpdateAttributesPacket.class);
        IGNORED_CLASSES.add(ClientboundSetChunkCacheCenterPacket.class);
        IGNORED_CLASSES.add(ClientboundLightUpdatePacket.class);
        IGNORED_CLASSES.add(ClientboundKeepAlivePacket.class);
        IGNORED_CLASSES.add(ServerboundKeepAlivePacket.class);
        IGNORED_CLASSES.add(ClientboundForgetLevelChunkPacket.class);
    }

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
    public void showExactItemStackCount(ItemTooltipEvent event)
    {
        if (!ClientConfig.enableNumberShortening.get())
            return;

        var stack = event.getItemStack();

        if (stack.getCount() > Constants.ONE_THOUSAND)
        {
            event.getToolTip()
                 .add(1,
                      new TranslatableComponent("biggerstacks.exact.count",
                                                new TextComponent(TOOLTIP_NUMBER_FORMAT.format(stack.getCount())).withStyle(
                                                        ChatFormatting.DARK_AQUA)).withStyle(ChatFormatting.GRAY));
        }
    }
}
