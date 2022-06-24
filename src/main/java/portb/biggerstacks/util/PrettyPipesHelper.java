package portb.biggerstacks.util;

import portb.biggerstacks.config.AutoSidedConfig;

public class PrettyPipesHelper
{
    public static int calculateExtractionRate(int originalRate)
    {
        //Avoid potentially returning 0 with math.max
        return (int) Math.max(1, Math.round(originalRate * (AutoSidedConfig.getMaxStackSize() / 64.0)));
    }
}
