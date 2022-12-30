package portb.biggerstacks.net;

import io.netty.buffer.ByteBuf;
import portb.configlib.xml.RuleSet;

/**
 * Packet sent to clients when the config file is updated
 */
public class ClientboundRulesUpdatePacket
{
    private final RuleSet rules;
    
    /**
     * Used for play to client
     * @param rules
     */
    public ClientboundRulesUpdatePacket(RuleSet rules)
    {
        this.rules = rules;
    }
    
    /**
     * Decodes a packet from a byte buffer
     */
    public ClientboundRulesUpdatePacket(ByteBuf buf)
    {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        rules = RuleSet.fromBytes(bytes);
    }
    
    public void encode(ByteBuf buf)
    {
        buf.writeBytes(rules.toBytes());
    }
    
    public RuleSet getRules()
    {
        return rules;
    }
}
