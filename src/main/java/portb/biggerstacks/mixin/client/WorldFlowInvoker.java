package portb.biggerstacks.mixin.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldOpenFlows.class)
public interface WorldFlowInvoker
{
    @Invoker("doLoadLevel")
    void invokeDoLoadLevel(Screen p_233146_, String p_233147_, boolean p_233148_, boolean p_233149_, boolean confirmExperimentalWarning);
}
