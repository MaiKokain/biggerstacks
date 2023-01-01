/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import portb.biggerstacks.Constants;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ServerEvents
{
    private static ServerLifecycleHandler handler;
    
    /**
     * Creates a ServerLifecycleHandler that manages config updates
     *
     * @param event
     */
    @SubscribeEvent
    public static void serverStarting(FMLServerAboutToStartEvent event)
    {
        handler = new ServerLifecycleHandler();
        
        MinecraftForge.EVENT_BUS.register(handler);
    }
    
    /**
     * Unregisters the handler
     *
     * @param event
     */
    @SubscribeEvent
    public static void serverStopping(FMLServerStoppingEvent event)
    {
        MinecraftForge.EVENT_BUS.unregister(handler);
        
        handler.ensureStopped();
    }
    
}
