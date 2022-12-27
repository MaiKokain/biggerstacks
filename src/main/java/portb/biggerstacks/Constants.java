package portb.biggerstacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class Constants
{
    public static final int ONE_BILLION  = 1_000_000_000;
    public static final int ONE_MILLION  = 1_000_000;
    public static final int ONE_THOUSAND = 1_000;
    
    public static final String MOD_ID            = "biggerstacks";
    public static final String RULESET_FILE_NAME = "biggerstacks-rules.xml";
    public static final Path   RULESET_FILE      = FMLPaths.CONFIGDIR.get().resolve(Constants.RULESET_FILE_NAME);
    
    public static final ResourceLocation CONFIG_GUI_BG = new ResourceLocation(Constants.MOD_ID,
                                                                              "textures/gui/config_background.png"
    );
}
