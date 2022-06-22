package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.BiggerStacks;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
    /**
     * Increases the maximum stack size
     */
    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        Item item = ((ItemStack) (Object) this).getItem();

        try
        {
            //if whitelist is enabled and the item isn't whitelisted, don't increase its stack size
            if (AutoSidedConfig.isUsingWhitelist() && !item.is(BiggerStacks.WHITELIST_TAG))
                return;
                //check if this item has the blacklist tag, and if it does, don't increase its stack size
            else if (item.is(BiggerStacks.BLACKLIST_TAG))
                return;
        }
        catch (IllegalStateException e)
        {
            System.err.println("Tags are not bound at this time! Assuming all items are whitelisted");
        }

        if (returnInfo.getReturnValue() != 1)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(AutoSidedConfig.getMaxStackSize());
        }
    }

    /**
     * Saves the stack size as an int instead of a byte.
     * This will cause the stack to be deleted if the world is loaded without this mod installed.
     */
    @Redirect(method = "save",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundNBT;putByte(Ljava/lang/String;B)V"))
    private void saveBigStack(CompoundNBT tag, String key, byte p_128346_)
    {
        int count = ((ItemStack) (Object) this).getCount();
        tag.putInt(key, count);
    }

    /**
     * Reads the stack size as an int instead of a byte
     * This will cause the stack to be deleted if the world is loaded without this mod installed.
     */
    @Redirect(method = "<init>(Lnet/minecraft/nbt/CompoundNBT;)V",
              at = @At(value = "FIELD",
                       target = "Lnet/minecraft/item/ItemStack;count:I",
                       opcode = Opcodes.PUTFIELD))
    private void readBigStack(ItemStack instance, int value, CompoundNBT tag)
    {
        ((ItemStackAccessor) (Object) instance).accessSetCount(tag.getInt("Count"));
    }

}
