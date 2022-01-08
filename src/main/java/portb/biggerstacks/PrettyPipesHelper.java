package portb.biggerstacks;

import portb.biggerstacks.config.ServerConfig;

public class PrettyPipesHelper
{
    public static int calculateExtractionRate(int originalRate)
    {
        return (int)Math.round(originalRate * (ServerConfig.INSTANCE.maxStackCount.get() / 64.0));
    }
}
