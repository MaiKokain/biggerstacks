/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkEvent;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.ClientConfig;
import portb.configlib.template.ConfigTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;

public class ServerboundCreateConfigTemplatePacket extends GenericTemplateOptionsPacket
{
    public ServerboundCreateConfigTemplatePacket(int normalStackLimit, int potionsStackLimit, int enchantedBooksStackLimit)
    {
        super(normalStackLimit, potionsStackLimit, enchantedBooksStackLimit);
    }
    
    public ServerboundCreateConfigTemplatePacket(ByteBuf buf)
    {
        super(buf);
    }
    
    static void handleCreateConfigTemplate(ServerboundCreateConfigTemplatePacket serverboundCreateConfigTemplatePacket, Supplier<NetworkEvent.Context> contextSupplier)
    {
        //If on a server, check that the player actually has permissions to do this
        if (FMLEnvironment.dist.isDedicatedServer())
        {
            ServerPlayerEntity sender = contextSupplier.get().getSender();
        
            if (sender == null || !sender.hasPermissions(Constants.CHANGE_STACK_SIZE_COMMAND_PERMISSION_LEVEL))
                return;
        }
        
        ConfigTemplate template = ConfigTemplate.generateTemplate(serverboundCreateConfigTemplatePacket);
        
        try
        {
            if (FMLEnvironment.dist.isClient())
                //don't display warning anymore
                ClientConfig.stfuWarning.set(true);
    
            Files.write(Constants.RULESET_FILE,
                        template.toXML().getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
            );
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void encode(ByteBuf buf)
    {
        super.encode(buf);
    }
}