package portb.biggerstacks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.WorldSelectionList;
import net.minecraft.world.storage.WorldSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldSelectionList.Entry.class)
public interface WorldSelectionList$WorldListEntryInvoker
{
    @Invoker("queueLoadScreen")
    void invokeQueueLoadScreen();
    
    @Accessor("minecraft")
    Minecraft getMinecraft();
    
    @Accessor("summary")
    WorldSummary getSummary();
}

