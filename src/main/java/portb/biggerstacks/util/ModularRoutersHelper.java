package portb.biggerstacks.util;

import portb.biggerstacks.config.AutoSidedConfig;

public class ModularRoutersHelper
{
    public static int getMaxStackUpgrades()
    {
        //this needs precision, and is only done once per opening the gui
        return (int) Math.ceil(Math.log(AutoSidedConfig.getMaxStackSize()) / Math.log(2));
    }
}
