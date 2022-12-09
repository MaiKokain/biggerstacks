package portb.biggerstacks;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.LocalConfig;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.event.ClientEvents;
import portb.biggerstacks.event.CommonEvents;
import portb.biggerstacks.event.ServerEvents;
import portb.configlib.ConfigLib;
import portb.slw.MyLoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.MOD_ID)
public class BiggerStacks
{
    public final static Logger LOGGER = LogUtils.getLogger();
    
    public BiggerStacks()
    {
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(ClientEvents.class));
    
        ConfigLib.LOGGER = MyLoggerFactory.createMyLogger(LoggerFactory.getLogger(ConfigLib.class));
        
        registerConfigs();
    }
    
    private static void registerConfigs()
    {
        ModLoadingContext context = ModLoadingContext.get();
    
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, Constants.MOD_ID + "-client.toml");
        context.registerConfig(ModConfig.Type.CLIENT,
                                         LocalConfig.INSTANCE.SPEC,
                                         Constants.MOD_ID + "-local.toml");
        context.registerConfig(ModConfig.Type.SERVER,
                                         ServerConfig.INSTANCE.SPEC,
                                         Constants.MOD_ID + "-server.toml");
    }
}
