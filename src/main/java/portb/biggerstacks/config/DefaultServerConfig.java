package portb.biggerstacks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DefaultServerConfig
{
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue maxStackCount;

    static{
        builder.comment("This file serves as the default config for new worlds/(server) when they are created");
        maxStackCount = builder.comment("Maximum stack size for items. Items that are able to stack more than 1 item " +
                "(i.e. swords, tools, etc) are not effected, stack size for everything else is raised to this value.", " While you *can* go up to " + Integer.MAX_VALUE +
                ", that doesn't mean you *should*").defineInRange("Max stack size", 999, 1, Integer.MAX_VALUE);

        SPEC = builder.build();
    }
}
