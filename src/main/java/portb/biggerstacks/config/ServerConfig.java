package portb.biggerstacks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig
{
    public final static ServerConfig INSTANCE = new ServerConfig();

    private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public final ForgeConfigSpec SPEC;

    public final ForgeConfigSpec.IntValue maxStackCount;

    ServerConfig()
    {
        maxStackCount = builder.comment("Maximum stack size for items. Items that are able to stack more than 1 item " +
                "(i.e. swords, tools, etc) are not effected, stack size for everything else is raised to this value.", " While you *can* go up to " + Integer.MAX_VALUE +
                        ", that doesn't mean you *should*").defineInRange("Max stack size", 999, 1, Integer.MAX_VALUE);

        SPEC = builder.build();
    }
}
