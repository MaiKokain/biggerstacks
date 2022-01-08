package portb.biggerstacks;

import portb.biggerstacks.config.ServerConfig;

public class ModularRoutersHelper
{
    public static int getMaxStackUpgrades()
    {
        //this needs precision, and is only done once per opening the gui
        return (int) Math.ceil(Math.log(ServerConfig.INSTANCE.maxStackCount.get()) / Math.log(2));
    }
}
