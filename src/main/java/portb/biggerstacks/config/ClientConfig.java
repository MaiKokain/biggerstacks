/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.config;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Config used for client-side only things, such as how stack numbers are rendered.
 */
@OnlyIn(Dist.CLIENT)
public class ClientConfig
{
    private static final Map<String, TextFormatting> NUMBER_FORMATTING_COLOURS = new HashMap<>();
    
    public static final  ForgeConfigSpec                     SPEC;
    public static final  ForgeConfigSpec.BooleanValue        enableNumberShortening;
    public static final  ForgeConfigSpec.BooleanValue        stfuWarning;
    private static final ForgeConfigSpec.ConfigValue<String> numberColour;
    
    static
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        EnumSet.complementOf(EnumSet.of(
                TextFormatting.STRIKETHROUGH,
                TextFormatting.BOLD,
                TextFormatting.RESET,
                TextFormatting.UNDERLINE,
                TextFormatting.ITALIC,
                TextFormatting.OBFUSCATED
        )).forEach(chatFormatting -> NUMBER_FORMATTING_COLOURS.put(chatFormatting.getName(), chatFormatting));
        
        builder.comment("Client configs");
        enableNumberShortening = builder.comment("Enable number shortening. E.g. 1000000 becomes 1M.")
                                        .define("Enable number shortening", true);
        stfuWarning = builder.comment(
                "Disables warning for stack size that is potentially wrong due to behaviour changes.").define(
                "STFU Warning",
                false
        );
        
        numberColour = builder.comment("The colour of the exact count tooltip shown on items.",
                                       "Available colours (case insensitive):",
                                       NUMBER_FORMATTING_COLOURS.keySet().stream().sorted().collect(Collectors.joining(
                                               ",\n"))
        ).define("Exact count number colour", TextFormatting.DARK_AQUA.getName(),
                 value -> NUMBER_FORMATTING_COLOURS.containsKey((value != null ? (String) value : "").toLowerCase())
        );
        
        SPEC = builder.build();
    }
    
    public static TextFormatting getNumberColour()
    {
        return NUMBER_FORMATTING_COLOURS.get(numberColour.get().toLowerCase());
    }
}
