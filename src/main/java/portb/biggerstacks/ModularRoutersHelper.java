package portb.biggerstacks;

import portb.biggerstacks.config.ServerConfig;

public class ModularRoutersHelper
{
    public static int getMaxStackUpgrades()
    {
        return (int) Math.ceil(FastLog2.fastLog2(ServerConfig.INSTANCE.maxStackCount.get()));
    }
}
