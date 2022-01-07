package portb.biggerstacks;

import portb.biggerstacks.config.ServerConfig;

public class ModularRoutersHelper
{
    public static int getMaxStackUpgrades()
    {
        return (int) Math.ceil(Math.log(ServerConfig.INSTANCE.maxStackCount.get()) / Math.log(2));
    }
}
