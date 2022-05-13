package portb.biggerstacks;

import portb.biggerstacks.config.AutoSidedConfig;

public class PrettyPipesHelper
{
    public static int calculateExtractionRate(int originalRate)
    {
        return (int) Math.round(originalRate * (AutoSidedConfig.maxStackSize() / 64.0));
    }
}
