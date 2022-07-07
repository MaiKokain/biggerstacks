package portb.biggerstacks.util;

import portb.biggerstacks.config.AutoSidedConfig;

public class ModularRoutersHelper
{
    public static int getMaxStackUpgrades()
    {
        //this needs precision, and is only done once per opening the gui
        //Avoid potentially returning 0 with math.max
        return (int) Math.max(1, Math.ceil(Math.log(StackSizeHelper.getNewSlotLimit()) / Math.log(2)));
    }
}
