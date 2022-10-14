package portb.biggerstacks;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.LocalConfig;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.configlib.ConfigLib;
import portb.biggerstacks.configlib.MyLogger;
import portb.biggerstacks.event.ClientEvents;
import portb.biggerstacks.event.CommonEvents;
import portb.biggerstacks.event.ServerEvents;

import java.util.Optional;

@Mod(Constants.MOD_ID)
public class BiggerStacks
{
    public final static Logger LOGGER = LogManager.getLogger();

    public BiggerStacks()
    {
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(ClientEvents.class));
    
        ConfigLib.LOGGER = Optional.of(new MyLogger(){
        
            final Logger logger = LogManager.getLogger();
        
            @Override
            public void info(String s)
            {
                logger.info(s);
            }
        
            @Override
            public void debug(String s)
            {
                logger.debug(s);
            }
        
            @Override
            public void error(String s)
            {
                logger.error(s);
            }
        
            @Override
            public void error(String s, Throwable throwable)
            {
                logger.error(s, throwable);
            }
        
            @Override
            public void warn(String s)
            {
                logger.warn(s);
            }
        });

        registerConfigs();
    }
    
    private void registerConfigs()
    {
        ModLoadingContext context = ModLoadingContext.get();
    
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, Constants.MOD_ID + "-client.toml");
        context.registerConfig(ModConfig.Type.CLIENT,
                               LocalConfig.INSTANCE.SPEC,
                               Constants.MOD_ID + "-local.toml"
        );
        context.registerConfig(ModConfig.Type.SERVER,
                               ServerConfig.INSTANCE.SPEC,
                               Constants.MOD_ID + "-server.toml"
        );
    }
}
