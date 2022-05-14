package portb.biggerstacks.mixin.compat.colossalchests;

import org.cyclops.colossalchests.blockentity.BlockEntityColossalChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(BlockEntityColossalChest.class)
public class BlockEntityColossalChestMixin
{
    @ModifyConstant(method = {"constructInventory", "constructInventoryDebug"},
                    constant = @Constant(intValue = 64),
                    remap = false,
                    require = 0)
    private int increaseStackSize(int value)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}
