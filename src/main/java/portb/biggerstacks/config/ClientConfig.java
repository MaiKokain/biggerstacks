/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Config used for client-side only things, such as how stack numbers are rendered.
 */
@OnlyIn(Dist.CLIENT)
public class ClientConfig
{
    public static final ForgeConfigSpec              SPEC;
    public static final ForgeConfigSpec.BooleanValue enableNumberShortening;
    public static final ForgeConfigSpec.BooleanValue stfuWarning;
    
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    
    
    static
    {
        builder.comment("Client configs");
        
        enableNumberShortening = builder.comment("Enable number shortening. E.g. 1000000 becomes 1M.")
                                        .define("Enable number shortening", true);
        
        stfuWarning = builder.comment(
                "Disables warning for stack size that is potentially wrong due to behaviour changes.").define(
                "STFU Warning",
                false
        );
        
        SPEC = builder.build();
    }
}

