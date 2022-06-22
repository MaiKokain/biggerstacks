package portb.biggerstacks.mixin.compat.tinkers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import slimeknights.tconstruct.tools.recipe.CreativeSlotRecipe;
import slimeknights.tconstruct.tools.recipe.ModifierRemovalRecipe;

@Mixin({ModifierRemovalRecipe.class, CreativeSlotRecipe.class})
public class ModifierRemovalRecipeAndCreativeSlotRecipeMixin
{
    /**
     * I think this is the amount of items to remove from the tinker slot in the tinker station when the user clicks on the result item.
     * I'm not sure what it does, but it's probably good to make it consistent with the larger stack size.
     */
    @ModifyConstant(
            method = "shrinkToolSlotBy",
            constant = @Constant(intValue = 64),
            remap = false
    )
    private int increaseStackLimit(int val) {
        return AutoSidedConfig.getMaxStackSize();
    }

}
