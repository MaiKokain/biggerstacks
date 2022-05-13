package portb.biggerstacks;

import portb.biggerstacks.config.AutoSidedConfig;

public class ModularRoutersHelper
{
    public static int getMaxStackUpgrades()
    {
        //this needs precision, and is only done once per opening the gui
        return (int) Math.ceil(Math.log(AutoSidedConfig.maxStackSize()) / Math.log(2));
    }
}
