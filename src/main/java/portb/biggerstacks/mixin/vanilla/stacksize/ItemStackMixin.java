/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.util.ItemStackSizeHelper;

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
    
        ItemStackSizeHelper.applyStackSizeToItem(returnInfo, item);
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
        
        tag.putByte("Count", (byte) Math.min(count, Byte.MAX_VALUE));
        
        if (count > Byte.MAX_VALUE)
            tag.putInt("BigCount", count);
    }
    
    /**
     * Reads the stack size as an int instead of a byte
     * Attempts to maintain some vanilla compatibility
     */
    @SuppressWarnings("DataFlowIssue")
    @Redirect(method = "<init>(Lnet/minecraft/nbt/CompoundNBT;)V",
              at = @At(value = "FIELD",
                       target = "Lnet/minecraft/item/ItemStack;count:I",
                       opcode = Opcodes.PUTFIELD))
    private void readBigStack(ItemStack instance, int value, CompoundNBT tag)
    {
        ItemStackAccessor accessor = ((ItemStackAccessor) (Object) instance);
        
        if (tag.contains("BigCount"))
            accessor.accessSetCount(tag.getInt("BigCount"));
        else if (tag.getTagType("Count") == Constants.NBT.TAG_INT)
            accessor.accessSetCount(tag.getInt("Count"));
        else
            accessor.accessSetCount(tag.getByte("Count"));
    }
    
}
