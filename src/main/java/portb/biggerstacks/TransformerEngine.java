package portb.biggerstacks;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import portb.biggerstacks.config.StackSizeRules;
import portb.slw.MyLoggerFactory;
import portb.transformerlib.TransformerLib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformerEngine implements IMixinConfigPlugin
{
    private static final Logger  LOGGER                        = LoggerFactory.getLogger(TransformerEngine.class);
    private static final Pattern MOD_ID_PACKAGE_TARGET_PATTERN = Pattern.compile("mixin\\.compat\\.([^.]+)\\.[^.]+$");
    
    static
    {
        EnumSet<ILaunchPluginService.Phase> NONE          = EnumSet.noneOf(ILaunchPluginService.Phase.class);
        EnumSet<ILaunchPluginService.Phase> BEFORE        = EnumSet.of(ILaunchPluginService.Phase.BEFORE);
        LaunchPluginHandler                 launchPlugins = getPrivateField(Launcher.INSTANCE, "launchPlugins");
        Map<String, ILaunchPluginService>   plugins       = getPrivateField(launchPlugins, "plugins");
        
        //don't create the logger in BiggerStacks class constructor. It is called way too late.
        TransformerLib.LOGGER = MyLoggerFactory.createMyLogger(LOGGER);
        //library needs to know how to get the maximum stack size
        TransformerLib.setGlobalStackLimitSupplier(StackSizeRules::getMaxStackSize);
        
        try (InputStream stream = TransformerEngine.class.getResourceAsStream("/transformer.xml"))
        {
            TransformerLib.loadTransformers(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        
        plugins.put("biggerstacks_transformer", new ILaunchPluginService()
        {
            @Override
            public String name()
            {
                return "biggerstacks_transformer";
            }
            
            @Override
            public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty)
            {
                //don't touch mixin classes or "empty" classes (i'm not sure what an "empty" class is)
                //todo maybe filter out some of the vanilla classes we're not interested in?
                
                return isEmpty || classType.getClassName().contains("/mixin/") ? NONE : BEFORE;
            }
            
            @Override
            public int processClassWithFlags(Phase phase, ClassNode classNode, Type classType, String reason)
            {
                if (phase == Phase.AFTER)
                    return ComputeFlags.NO_REWRITE;
                
                return TransformerLib.handleTransformation(classNode) ? ComputeFlags.COMPUTE_MAXS : ComputeFlags.NO_REWRITE;
            }
            
        });
    }
    
    private static <T> T getPrivateField(Object obj, String fieldName)
    {
        try
        {
            var field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void onLoad(String mixinPackage)
    {
    }
    
    @Override
    public String getRefMapperConfig()
    {
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        Matcher matcher = MOD_ID_PACKAGE_TARGET_PATTERN.matcher(mixinClassName);
    
        if (matcher.find())
        {
            String  modId       = matcher.group(1);
            boolean isModLoaded = FMLLoader.getLoadingModList().getModFileById(modId) != null;
        
            if (isModLoaded)
                LOGGER.info(modId + " is installed, applying patches");
            else
                LOGGER.debug(modId + " is NOT installed");
        
            return isModLoaded;
        }
        else
        {
            return true;
        }
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
    {
    
    }
    
    @Override
    public List<String> getMixins()
    {
        return null;
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
    {
    
    }
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
    {
    
    }
}