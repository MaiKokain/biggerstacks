package portb.biggerstacks;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.LocalConfig;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.event.ClientEvents;
import portb.biggerstacks.event.CommonEvents;
import portb.biggerstacks.event.ServerEvents;
import portb.configlib.ConfigLib;
import portb.configlib.IMCAPI;
import portb.slw.MyLoggerFactory;

@Mod(Constants.MOD_ID)
public class BiggerStacks
{
    public final static Logger LOGGER = LogUtils.getLogger();
    
    public BiggerStacks()
    {
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
    
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(ClientEvents.class));
    
        ConfigLib.LOGGER = MyLoggerFactory.createMyLogger(LoggerFactory.getLogger(ConfigLib.class));
    
        registerConfigs();
    }
    
    void processIMC(final InterModProcessEvent event)
    {
        event.getIMCStream().forEach(imcMessage -> IMCAPI.addIMCRuleSupplier(imcMessage.senderModId(),
                                                                             imcMessage.messageSupplier()
        ));
    }
    
    private void registerConfigs()
    {
        var context = ModLoadingContext.get();
        
        context.registerConfig(ModConfig.Type.CLIENT,
                               ClientConfig.SPEC,
                               Constants.MOD_ID + "-client.toml"
        );
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
