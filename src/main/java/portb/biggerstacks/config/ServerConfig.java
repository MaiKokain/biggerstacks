package portb.biggerstacks.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Server side config for stack size and transfer rate tweaks.
 */
public class ServerConfig
{
    public final static ServerConfig                 INSTANCE = new ServerConfig(true);
    public final        ForgeConfigSpec              SPEC;
    public final        ForgeConfigSpec.IntValue     globalMaxStackSize;
    public final        ForgeConfigSpec.BooleanValue increaseTransferRate;
    
    ServerConfig(boolean isOnlyForDedicatedServer)
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        if (isOnlyForDedicatedServer && !FMLEnvironment.dist.isDedicatedServer())
        {
            builder.comment(
                    "IGNORE THIS CONFIG FILE!!!!",
                    "IGNORE THIS CONFIG FILE!!!!",
                    "IGNORE THIS CONFIG FILE!!!!",
                    "IT IS ONLY USED WHEN HOSTING ON LAN"
            );
        }
    
        builder.push("biggerstacks");
    
        globalMaxStackSize = builder.comment(
                "Maximum global stack size for items.",
                "Rules will not increase the stack size higher than this limit",
                "Don't set this ridiculously high, as things could break. You have been warned.",
                "Anything below 10 million should be pretty safe."
        ).defineInRange("Max global stack size", 999, 1, Integer.MAX_VALUE / 2);
    
        increaseTransferRate = builder.comment(
                "Whether to increase max transfer rate of some mods to the new stack limit/t.",
                "E.g. if max stack limit is 1000, it will become 1000 items per tick (where applicable).",
                "How this is done will vary for each mod",
                "- Modular routers will require more stack upgrades",
                "- Pipez does not need this option, it has a config for transfer rate, which you can set to anything",
                "- Mekanism also has its own config value, though the logistical sorter has its extract rate increased",
                "- Pretty pipes has its extract rate scaled up",
                "- XNet can already extract a variable amount, but you will be able to go past 64 to the new maximum stack limit",
                "- Cyclic still extracts 1 stack (more than 64 items) per tick, but the size of the stack is adjusted"
        ).define("Increase transfer rate", true);

        builder.pop();

        SPEC = builder.build();
    }
}
