package portb.biggerstacks.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.configlib.ConfigFileWatcher;
import portb.biggerstacks.configlib.ConfigLib;
import portb.biggerstacks.configlib.xml.RuleSet;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ServerEvents
{
    private static ServerLifecycleHandler handler;
    
    /**
     * Creates a ServerLifecycleHandler that manages config updates
     * @param event
     */
    @SubscribeEvent
    public static void serverStarting(ServerAboutToStartEvent event)
    {
        handler = new ServerLifecycleHandler();
        
        MinecraftForge.EVENT_BUS.register(handler);
    }
    
    /**
     * Unregisters the handler
     * @param event
     */
    @SubscribeEvent
    public static void serverStopping(ServerStoppingEvent event)
    {
        MinecraftForge.EVENT_BUS.unregister(handler);
        
        handler.ensureStopped();
    }
    
}
