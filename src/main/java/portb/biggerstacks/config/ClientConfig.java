package portb.biggerstacks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig
{
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue enableNumberShortening;
    public static final ForgeConfigSpec.BooleanValue enableFatStacks;
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    static
    {
        builder.comment("Client configs");
        enableNumberShortening = builder.comment("Enable number shortening. E.g. 1000000 becomes 1M.").define("Enable number shortening", true);
        enableFatStacks = builder.comment("Enable fat stacks. When stacks of items beyond 64 are dropped on the ground, they become logarithmically bigger (more items shown in the stack).", "Purley cosmetic").define("Enable fat stacks", true);

        SPEC = builder.build();
    }
}
