package portb.biggerstacks.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.gui.ConfigureScreen;
import portb.configlib.template.ConfigTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;

/**
 * Handles packets for rulset config
 */
public class PacketHandler
{
    public final static  ResourceLocation CHANNEL_NAME     = new ResourceLocation(Constants.MOD_ID, "rules");
    private static final String           PROTOCOL_VERSION = "1";
    public static final  SimpleChannel    INSTANCE         = NetworkRegistry.newSimpleChannel(
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
                .consumer(PacketHandler::handleHandshake)
                .add();
    
        INSTANCE.messageBuilder(ClientboundRulesUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientboundRulesUpdatePacket::encode)
                .decoder(ClientboundRulesUpdatePacket::new)
                .noResponse()
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
                .consumer(PacketHandler::handleCreateConfigTemplate)
                .add();
    }
    
    
    private static boolean handleHandshake(ClientboundRulesHandshakePacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        StackSizeRules.setRuleSet(packet.getRules());
        return true;
    }
    
    private static boolean handleUpdate(ClientboundRulesUpdatePacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        StackSizeRules.setRuleSet(packet.getRules());
        return true;
    }
    
    private static boolean handleOpenScreenPacket(ClientboundConfigureScreenOpenPacket clientboundConfigureScreenOpenPacket, Supplier<NetworkEvent.Context> contextSupplier)
    {
        //contextSupplier.get().getSender().level.isClientSide()
        //open the screen
        contextSupplier.get().enqueueWork(() -> ConfigureScreen.open(clientboundConfigureScreenOpenPacket));
        return true;
    }
    
    private static void handleCreateConfigTemplate(ServerboundCreateConfigTemplatePacket serverboundCreateConfigTemplatePacket, Supplier<NetworkEvent.Context> contextSupplier)
    {
        //ignore the packet if on dedicated server
        if (FMLEnvironment.dist.isDedicatedServer())
        {
            return;
        }
        
        ConfigTemplate template = ConfigTemplate.generateTemplate(serverboundCreateConfigTemplatePacket);
        
        //todo
        //ModList.get().isLoaded("ic2")
        
        try
        {
            Files.writeString(Constants.RULESET_FILE,
                              template.toXML(),
                              StandardOpenOption.CREATE,
                              StandardOpenOption.TRUNCATE_EXISTING
            );
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
