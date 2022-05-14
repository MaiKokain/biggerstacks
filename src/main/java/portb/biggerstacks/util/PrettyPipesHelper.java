package portb.biggerstacks.util;

import portb.biggerstacks.config.AutoSidedConfig;

public class PrettyPipesHelper
{
    public static int calculateExtractionRate(int originalRate)
    {
        return (int) Math.round(originalRate * (AutoSidedConfig.getMaxStackSize() / 64.0));
    }
}
