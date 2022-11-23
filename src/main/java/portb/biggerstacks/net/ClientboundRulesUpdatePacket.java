package portb.biggerstacks.net;

import net.minecraft.network.FriendlyByteBuf;
import portb.biggerstacks.config.StackSizeRules;
import portb.configlib.xml.RuleSet;

/**
 * Packet sent to clients when the config file is updated
 */
public class ClientboundRulesUpdatePacket
{
    private final RuleSet rules;
    
    /**
     * Used for play to client
     */
    public ClientboundRulesUpdatePacket(RuleSet rules)
    {
        this.rules = rules;
    }
    
    /**
     * Decodes a packet from a byte buffer
     */
    public ClientboundRulesUpdatePacket(FriendlyByteBuf buf)
    {
        rules = RuleSet.fromBytes(buf.readByteArray());
    }
    
    public void encode(FriendlyByteBuf buf)
    {
        buf.writeByteArray(rules.toBytes());
    }
    
    public RuleSet getRules()
    {
        return rules;
    }
}
