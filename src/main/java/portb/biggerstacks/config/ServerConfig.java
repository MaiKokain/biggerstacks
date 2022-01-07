package portb.biggerstacks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig
{
    public final static ServerConfig INSTANCE = new ServerConfig();

    private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public final ForgeConfigSpec SPEC;

    public final ForgeConfigSpec.IntValue maxStackCount;
    public final ForgeConfigSpec.BooleanValue increaseTransferRate;

    ServerConfig()
    {
        maxStackCount = builder.comment("Maximum stack size for items. Items that are able to stack more than 1 item " +
                "(i.e. swords, tools, etc) are not effected, stack size for everything else is raised to this value.", " While you *can* go up to " + Integer.MAX_VALUE +
                        ", that doesn't mean you *should*").defineInRange("Max stack size", 999, 1, Integer.MAX_VALUE);
        increaseTransferRate = builder.comment("Whether to increase max transfer rate of some mods to the new stack limit/t.",
                "E.g. if max stack limit is 1000, it will become 1000 items per tick (where applicable).",
                "How this is done will vary for each mod",
                "Modular routers will require more stack upgrades",
                "Pipez does not need this option, it has a config for transfer rate, which you can set to anything").define("Increase transfer rate", true);

        SPEC = builder.build();
    }
}
