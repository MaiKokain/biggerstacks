package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin(ItemEntity.class)
public class ItemEntityMixin
{
    /**
     * Allows items on the ground larger than 64 to merge into 1 stack.
     */
    @ModifyConstant(method = "merge(Lnet/minecraft/entity/item/ItemEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V",
                    constant = @Constant(intValue = 64))
    private static int increaseStackLimit(int val)
    {
        return StackSizeHelper.getNewStackSize();
    }
}
