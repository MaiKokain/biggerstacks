package portb.biggerstacks.mixin.compat.tinkers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.config.AutoSidedConfig;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipe;

@Mixin(IncrementalModifierRecipe.class)
public class IncrementalModifierRecipeMixin
{
    /**
     * I'm not sure what the function where this is located actually does, but it is related to the maximum stack size.
     */
    @ModifyConstant(method = "addIngredients", constant = @Constant(intValue = 64), remap = false)
    private static int increaseStackLimit(int val){
        return AutoSidedConfig.getMaxStackSize();
    }
}
