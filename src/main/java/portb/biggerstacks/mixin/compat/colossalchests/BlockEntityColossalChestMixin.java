package portb.biggerstacks.mixin.compat.colossalchests;

import org.cyclops.colossalchests.tileentity.TileColossalChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(TileColossalChest.class)
public class BlockEntityColossalChestMixin
{
    @ModifyConstant(method = {"constructInventory", "constructInventoryDebug"},
                    constant = @Constant(intValue = 64),
                    remap = false,
                    require = 0)
    private int increaseStackLimit(int value)
    {
        return StackSizeHelper.getNewSlotLimit();
    }
}
