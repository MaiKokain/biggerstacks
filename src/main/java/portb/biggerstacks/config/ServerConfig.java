package portb.biggerstacks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig
{
    public final static ServerConfig INSTANCE = new ServerConfig();
    public final ForgeConfigSpec SPEC;
    public final ForgeConfigSpec.IntValue maxStackCount;
    public final ForgeConfigSpec.BooleanValue increaseTransferRate;

    ServerConfig()
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        maxStackCount = builder.comment(
                "Maximum stack size for items. Items that are able to stack more than 1 item (i.e. swords, tools, etc) are not effected, stack size for everything else is raised to this value.",
                "The maximum theoretical limit is " + Integer.MAX_VALUE + ". However, to prevent funny integer overflows, I have limited the max stack size to " + (Integer.MAX_VALUE / 2)  + ".",
                "I don't recommend setting it that high, as things could still break. You have been warned.",
                "Anything below 10 million should be pretty safe.",
                "Some things may need a world restart for this to take effect."
        ).defineInRange("Max stack size", 999, 1, Integer.MAX_VALUE / 2);

        increaseTransferRate = builder.comment("Whether to increase max transfer rate of some mods to the new stack limit/t.",
                "E.g. if max stack limit is 1000, it will become 1000 items per tick (where applicable).",
                "How this is done will vary for each mod",
                "Modular routers will require more stack upgrades",
                "Pipez does not need this option, it has a config for transfer rate, which you can set to anything",
                "Pretty pipes has its extract rate scaled up",
                "XNet can already extract a variable amount, but you will be able to go past 64 to the new maximum stack limit",
                "Cyclic still extracts 1 stack (more than 64 items) per tick, but the size of the stack is adjusted ").define("Increase transfer rate", true);

        SPEC = builder.build();
    }
}
