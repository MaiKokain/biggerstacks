package portb.biggerstacks.net;

import io.netty.util.AttributeKey;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.*;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.gui.ConfigureScreen;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Handles packets for ruleset config
 */
public class PacketHandler
{
    private static final String           PROTOCOL_VERSION = "1";
    public final static  ResourceLocation CHANNEL_NAME     = new ResourceLocation(Constants.MOD_ID, "rules");
    
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
                //.loginIndex(LoginIndexedMessage::getLoginIndex, LoginIndexedMessage::setLoginIndex)
                .encoder(ClientboundRulesHandshakePacket::encode)
                .decoder(ClientboundRulesHandshakePacket::new)
                .markAsLoginPacket()
                .consumer(PacketHandler::handleHandshake)
                .add();
        
        INSTANCE.messageBuilder(ReplyPacket.class, index++, NetworkDirection.LOGIN_TO_SERVER)
                .loginIndex(ReplyPacket::getLoginIndex, ReplyPacket::setLoginIndex)
                .decoder((packetBuffer -> new ReplyPacket()))
                .encoder((packet, packetBuffer) -> {})
                .consumer(PacketHandler::handleLoginPacketUsingReflectionHacks)
                .add();
    
        INSTANCE.messageBuilder(ClientboundRulesUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientboundRulesUpdatePacket::encode)
                .decoder(ClientboundRulesUpdatePacket::new)
                .consumer(PacketHandler::handleUpdate)
                .add();
    
        //these have to exist on the server or the mod will not be "compatible" with it according to forge
    
        INSTANCE.messageBuilder(ClientboundConfigureScreenOpenPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientboundConfigureScreenOpenPacket::encode)
                .decoder(ClientboundConfigureScreenOpenPacket::new)
                .consumer(PacketHandler::handleOpenScreenPacket)
                .add();
    
        INSTANCE.messageBuilder(ServerboundCreateConfigTemplatePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ServerboundCreateConfigTemplatePacket::encode)
                .decoder(ServerboundCreateConfigTemplatePacket::new)
                .consumer(ServerboundCreateConfigTemplatePacket::handleCreateConfigTemplate)
                .add();
    
    }
    
    private static void handleHandshake(ClientboundRulesHandshakePacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        StackSizeRules.setRuleSet(packet.getRules());
        
        ctx.get().setPacketHandled(true);
        
        INSTANCE.reply(new ReplyPacket(), ctx.get());
    }
    
    private static boolean handleUpdate(ClientboundRulesUpdatePacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        StackSizeRules.setRuleSet(packet.getRules());
        return true;
    }
    
    private static void handleLoginPacketUsingReflectionHacks(ReplyPacket replyPacket, Supplier<NetworkEvent.Context> contextSupplier)
    {
        //this is REALLY gross, but it works
        //removes the packet from the server's acknowledgement packet queue so the login process can continue
        //none of this is documented by forge and I have no idea how else to do it
        //does what FMLHandshakeHandler.handleIndexedMessage does, removing the packet from the sentMessages list
        try
        {
            contextSupplier.get().setPacketHandled(true);
            
            //get the FMLHandshakeHandler attribute key so we can use it to get the handler from the context object
            Field attr = FMLNetworkConstants.class.getDeclaredField("FML_HANDSHAKE_HANDLER");
            attr.setAccessible(true);
            @SuppressWarnings("unchecked")
            AttributeKey<FMLHandshakeHandler> FML_HANDSHAKE_HANDLER = (AttributeKey<FMLHandshakeHandler>) attr.get(null);
            
            //get the handler from the context object using the attribute key we just got
            FMLHandshakeHandler handler = contextSupplier.get().attr(FML_HANDSHAKE_HANDLER).get();
            
            //get the pending messages list from the handler so we can remove the message so the server thinks it's handled
            Field sentMessagesField = handler.getClass().getDeclaredField("sentMessages");
            sentMessagesField.setAccessible(true);
            @SuppressWarnings("unchecked") List<Integer> sentMessages = (List<Integer>) sentMessagesField.get(handler);
    
            //remove the packet from the acknowledgement queue
            sentMessages.removeIf(i -> i == replyPacket.getAsInt());
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private static boolean handleOpenScreenPacket(ClientboundConfigureScreenOpenPacket clientboundConfigureScreenOpenPacket, Supplier<NetworkEvent.Context> contextSupplier)
    {
        //open the screen
        contextSupplier.get().enqueueWork(() -> ConfigureScreen.open(clientboundConfigureScreenOpenPacket));
        return true;
    }
    
    static class ReplyPacket implements IntSupplier
    {
        //copied from FMLHandshakeMessages.LoginIndexedMessage
        private int loginIndex;
        
        void setLoginIndex(final int loginIndex)
        {
            this.loginIndex = loginIndex;
        }
        
        int getLoginIndex()
        {
            return loginIndex;
        }
        
        @Override
        public int getAsInt()
        {
            return getLoginIndex();
        }
    }
}
