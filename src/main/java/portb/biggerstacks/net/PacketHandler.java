package portb.biggerstacks.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.StackSizeRules;

import java.util.function.Supplier;

/**
 * Handles packets for rulset config
 */
public class PacketHandler
{
    private static final String PROTOCOL_VERSION = "1";
    public final static ResourceLocation CHANNEL_NAME = new ResourceLocation(Constants.MOD_ID, "rules");
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            CHANNEL_NAME,
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    
    /**
     * Registers packets
     */
    public static void register()
    {
        int index = 0;
        INSTANCE.messageBuilder(ClientboundRulesHandshakePacket.class, index++, NetworkDirection.LOGIN_TO_CLIENT)
                .encoder(ClientboundRulesHandshakePacket::encode)
                .decoder(ClientboundRulesHandshakePacket::new)
                .markAsLoginPacket()
                .noResponse()
                .consumerMainThread(PacketHandler::handleHandshake)
                .add();
        
        INSTANCE.messageBuilder(ClientboundRulesUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientboundRulesUpdatePacket::encode)
                .decoder(ClientboundRulesUpdatePacket::new)
                .noResponse()
                .consumerMainThread(PacketHandler::handleUpdate)
                .add();
    }

    
    private static void handleHandshake(ClientboundRulesHandshakePacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        StackSizeRules.setRuleSet(packet.getRules());
        ctx.get().setPacketHandled(true);
    }
    
    private static void handleUpdate(ClientboundRulesUpdatePacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        StackSizeRules.setRuleSet(packet.getRules());
        ctx.get().setPacketHandled(true);
    }
}
