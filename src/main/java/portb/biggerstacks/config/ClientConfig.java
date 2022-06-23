package portb.biggerstacks.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Config used for client-side only things, such as how stack numbers are rendered.
 */
public class ClientConfig
{
    public static final  ForgeConfigSpec              SPEC;
    public static final  ForgeConfigSpec.BooleanValue enableNumberShortening;
    private static final ForgeConfigSpec.Builder      builder = new ForgeConfigSpec.Builder();

    static
    {
        builder.comment("Client configs");
        enableNumberShortening = builder.comment("Enable number shortening. E.g. 1000000 becomes 1M.")
                                        .define("Enable number shortening", true);

        SPEC = builder.build();
    }
}
