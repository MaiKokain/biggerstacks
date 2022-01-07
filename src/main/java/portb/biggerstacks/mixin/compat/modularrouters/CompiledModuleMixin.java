package portb.biggerstacks.mixin.compat.modularrouters;

import me.desht.modularrouters.logic.compiled.CompiledModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.ServerConfig;

@Mixin(CompiledModule.class)
public class CompiledModuleMixin
{
    @ModifyConstant(method = "getItemsPerTick", constant = @Constant(intValue = 64), remap = false, require = 0)
    private int increaseMaxTransferRate(int value)
    {
        if(ServerConfig.INSTANCE.increaseTransferRate.get())
            return ServerConfig.INSTANCE.maxStackCount.get();
        else
            return value;
    }
}

