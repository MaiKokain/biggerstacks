package portb.biggerstacks;

import com.mojang.realmsclient.util.task.WorldCreationTask;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.commands.GiveCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.DefaultServerConfig;
import portb.biggerstacks.config.ServerConfig;

import java.text.DecimalFormat;
import java.util.ArrayList;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BiggerStacks.MOD_ID)
public class BiggerStacks
{
    public static final String MOD_ID = "biggerstacks";
    public static final ArrayList<Class> IGNORED_CLASSES = new ArrayList<>();
    //public static final int MAX_STACK_SIZE = Integer.MAX_VALUE;

    private static final DecimalFormat TOOLTIP_NUMBER_FORMAT = new DecimalFormat("###,###,###,###,###,###");

    //for debugging every packet sent ever
    static{
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
        IGNORED_CLASSES.add(ClientboundAddMobPacket.class);
    }

    public BiggerStacks()
    {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, MOD_ID + "-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, MOD_ID + "-server.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DefaultServerConfig.SPEC, MOD_ID + "-default.toml");
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void showExactItemStackCount(ItemTooltipEvent event)
    {
        if(!ClientConfig.enableNumberShortening.get())
            return;

        var stack = event.getItemStack();

        if(stack.getCount() > Constants.ONE_THOUSAND)
        {
            event.getToolTip().add(1, new TextComponent(ChatFormatting.DARK_GRAY + "Exact count: " + TOOLTIP_NUMBER_FORMAT.format(stack.getCount())));
        }
    }
}
