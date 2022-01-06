package portb.biggerstacks.mixin.fixyourmods;

import blusunrize.immersiveengineering.common.blocks.generic.PoweredMultiblockBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.*;
import blusunrize.immersiveengineering.common.blocks.stone.*;
import blusunrize.immersiveengineering.common.blocks.wooden.*;
import blusunrize.immersiveengineering.common.gui.BlockEntityInventory;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.ServerConfig;

@Mixin(BlockEntityInventory.class)
class ImmersiveEngineeringFixer
{
    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true, remap = false, require = 0)
    private void fixMaxStackSize(CallbackInfoReturnable<Integer> returnInfo)
    {
        returnInfo.cancel();
        returnInfo.setReturnValue(ServerConfig.maxStackCount.get());
    }
}
