package portb.biggerstacks.event;

import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.net.ClientboundRulesUpdatePacket;
import portb.biggerstacks.net.PacketHandler;
import portb.configlib.ConfigFileWatcher;
import portb.configlib.ConfigLib;
import portb.configlib.xml.Rule;
import portb.configlib.xml.RuleSet;

import java.nio.file.Path;
import java.util.List;

public class ServerLifecycleHandler
{
    private static final Path xmlFile = FMLPaths.CONFIGDIR.get().resolve(Constants.RULESET_FILE_NAME);
    private       boolean           stopped = false;
    private final ConfigFileWatcher watcher = new ConfigFileWatcher(xmlFile);
    
    /**
     * Parses the config file and starts the file watcher
     * Called when server starts (ServerAboutToStart)
     */
    public ServerLifecycleHandler()
    {
        //read the ruleset file
        RuleSet ruleset = ConfigLib.readRuleset(xmlFile);
    
        //collect the rules from other mods which have provided a supplier over IMC and add them to the ruleset
        ruleset.addRules(collectIMCRules());
    
        StackSizeRules.setRuleSet(ruleset);
    
        //configure and start the watcher
        watcher.setOnUpdateAction(this::notifyClientsOfConfigChange);
        watcher.start();
    }
    
    
    private void notifyClientsOfConfigChange(RuleSet ruleSet)
    {
        //add any rules from other mods to the ruleset again
        ruleSet.addRules(collectIMCRules());
        
        //update our ruleset
        StackSizeRules.setRuleSet(ruleSet);
        
        //send new ruleset to clients
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientboundRulesUpdatePacket(ruleSet));
    }
    
    private List<Rule> collectIMCRules()
    {
        return StackSizeRules.IMC_ADD_RULES_MESSAGES
                       .stream()
                       .flatMap(imcMessage -> ConfigLib.convertIMCMessageToRule(imcMessage.senderModId(),
                                                                                imcMessage.messageSupplier().get()
                       ))
                       .toList();
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void serverStopping(ServerStoppingEvent event)
    {
        if (!stopped)
        {
            //stop the watcher
            watcher.stop();
            stopped = true;
        }
    }
    
    public void ensureStopped()
    {
        if(!stopped)
        {
            watcher.stop();
            stopped = true;
        }
    }
    
}
