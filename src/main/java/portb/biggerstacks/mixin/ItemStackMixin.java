package portb.biggerstacks.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.BiggerStacks;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void biggerMaxStackSize(CallbackInfoReturnable<Integer> returnInfo)
    {
        //check if this item has the blacklist tag, and if it does, don't increase its stack size
        if(((ItemStack)(Object) this).is(BiggerStacks.BLACKLIST_TAG))
            return;

        if (returnInfo.getReturnValue() != 1)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(AutoSidedConfig.maxStackSize());
        }
    }

    @Redirect(method = "save",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putByte(Ljava/lang/String;B)V"))
    private void saveBigStack(CompoundTag tag, String key, byte p_128346_)
    {
        int count = ((ItemStack) (Object) this).getCount();
        tag.putInt(key, count);
    }

    @Redirect(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V",
              at = @At(value = "FIELD",
                       target = "Lnet/minecraft/world/item/ItemStack;count:I",
                       opcode = Opcodes.PUTFIELD))
    private void readBigStack(ItemStack instance, int value, CompoundTag tag)
    {
        ((ItemStackAccessor) (Object) instance).accessSetCount(tag.getInt("Count"));
    }

}
