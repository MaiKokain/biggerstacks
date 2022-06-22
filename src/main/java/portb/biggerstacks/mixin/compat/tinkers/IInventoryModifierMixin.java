package portb.biggerstacks.mixin.compat.tinkers;

import org.spongepowered.asm.mixin.Mixin;
import portb.biggerstacks.config.AutoSidedConfig;
import slimeknights.tconstruct.library.tools.capability.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(ToolInventoryCapability.IInventoryModifier.class)
public interface IInventoryModifierMixin extends ToolInventoryCapability.IInventoryModifier
{
    /**
     * Increases slot size for modifiers that have an inventory, such as the tool belt for leggings
     */
    @Override
    default int getSlotLimit(IToolStackView tool, int slot)
    {
        return AutoSidedConfig.getMaxStackSize();
    }
}
