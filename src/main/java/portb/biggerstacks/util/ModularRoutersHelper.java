/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.util;

public class ModularRoutersHelper
{
    public static int getMaxStackUpgrades()
    {
        //this needs precision, and is only done once per opening the gui
        //Avoid potentially returning 0 with math.max
        return (int) Math.max(1, Math.ceil(Math.log(SlotLimitHelper.getNewStackSize()) / Math.log(2)));
    }
}
