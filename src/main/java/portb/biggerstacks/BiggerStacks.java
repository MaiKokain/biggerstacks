package portb.biggerstacks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.commands.GiveCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;
import java.util.ArrayList;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("biggerstacks")
public class BiggerStacks
{
    public static final ArrayList<Class> IGNORED_CLASSES = new ArrayList<>();
    public static final int MAX_STACK_SIZE = Integer.MAX_VALUE;

    private static final DecimalFormat TOOLTIP_NUMBER_FORMAT = new DecimalFormat("###,###,###,###,###,###");

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
    }

    @SubscribeEvent
    public void showExactItemStackCount(ItemTooltipEvent event)
    {
        var stack = event.getItemStack();

        if(stack.getCount() > Constants.ONE_THOUSAND)
        {
            event.getToolTip().add(1, new TextComponent(ChatFormatting.DARK_GRAY + "Exact count: " + TOOLTIP_NUMBER_FORMAT.format(stack.getCount())));
        }
    }
}
