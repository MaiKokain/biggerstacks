package portb.biggerstacks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig
{
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue enableNumberShortening;

    static
    {
        builder.comment("Client configs");
        enableNumberShortening = builder.comment("Enable number shortening. E.g. 1000000 becomes 1M.").define("Enable number shortening", true);

        SPEC = builder.build();
    }
}
