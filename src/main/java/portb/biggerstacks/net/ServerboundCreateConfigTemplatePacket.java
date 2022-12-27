package portb.biggerstacks.net;

import net.minecraft.network.FriendlyByteBuf;

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
    
    public void encode(FriendlyByteBuf buf)
    {
        super.encode(buf);
    }
}