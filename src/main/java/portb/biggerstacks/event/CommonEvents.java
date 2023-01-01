/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import portb.biggerstacks.Constants;
import portb.biggerstacks.net.PacketHandler;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents
{
    /**
     * Registers network packets
     *
     * @param event
     */
    @SubscribeEvent
    public static void serverStarting(FMLCommonSetupEvent event)
    {
        PacketHandler.register();
    }
}
