package portb.biggerstacks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldSelectionList.WorldListEntry.class)
public interface WorldSelectionList$WorldListEntryInvoker
{
    @Invoker("queueLoadScreen")
    void invokeQueueLoadScreen();
    
    @Accessor("minecraft")
    Minecraft getMinecraft();
    
    @Accessor("summary")
    LevelSummary getSummary();
}

