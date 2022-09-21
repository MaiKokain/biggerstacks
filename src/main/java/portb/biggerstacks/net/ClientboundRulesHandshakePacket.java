package portb.biggerstacks.net;

import io.netty.buffer.ByteBuf;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.configlib.xml.RuleSet;

/**
 * Packet sent to clients in login handshake to ensure that the client gets stack size rules before they are needed
 */
public class ClientboundRulesHandshakePacket
{
    private final RuleSet rules;
    
    /**
     * Decodes a packet from a byte buffer
     */
    public ClientboundRulesHandshakePacket(ByteBuf buf)
    {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        rules = RuleSet.fromBytes(bytes);
    }
    
    /**
     * Construct a packet which contains the loaded ruleset
     */
    @SuppressWarnings("unused")
    public ClientboundRulesHandshakePacket()
    {
        rules = StackSizeRules.getRuleSet();
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
