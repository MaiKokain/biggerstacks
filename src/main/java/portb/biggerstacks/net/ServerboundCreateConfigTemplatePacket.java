package portb.biggerstacks.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;
import portb.configlib.template.ConfigTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;

public class ServerboundCreateConfigTemplatePacket extends GenericTemplateOptionsPacket
{
    public ServerboundCreateConfigTemplatePacket(int normalStackLimit, int potionsStackLimit, int enchantedBooksStackLimit)
    {
        super(normalStackLimit, potionsStackLimit, enchantedBooksStackLimit);
    }
    
    public ServerboundCreateConfigTemplatePacket(FriendlyByteBuf buf)
    {
        super(buf);
    }
    
    static void handleCreateConfigTemplate(ServerboundCreateConfigTemplatePacket serverboundCreateConfigTemplatePacket, Supplier<NetworkEvent.Context> contextSupplier)
    {
        //ignore the packet if on dedicated server
        if (FMLEnvironment.dist.isDedicatedServer())
            return;
        
        ConfigTemplate template = ConfigTemplate.generateTemplate(serverboundCreateConfigTemplatePacket);
        
        try
        {
            //don't display warning anymore
            ClientConfig.stfuWarning.set(true);
            
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
    
    public void encode(FriendlyByteBuf buf)
    {
        super.encode(buf);
    }
}