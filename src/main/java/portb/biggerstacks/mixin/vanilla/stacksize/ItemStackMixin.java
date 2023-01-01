package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.util.CallingClassUtil;
import portb.biggerstacks.util.StackSizeHelper;
import portb.configlib.ItemProperties;

import static portb.biggerstacks.BiggerStacks.LOGGER;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
    /**
     * Increases the maximum stack size
     */
    @Inject(method = "getMaxStackSize",
            at = @At("RETURN"),
            cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        @SuppressWarnings("ConstantConditions") var itemstack = ((ItemStack) (Object) this);
        var                                         item      = itemstack.getItem();
        
        if (StackSizeRules.getRuleSet() != null)
        {
            var itemKey = ForgeRegistries.ITEMS.getKey(item);
            
            StackSizeRules.getRuleSet().determineStackSizeForItem(
                                  new ItemProperties(
                                          itemKey.getNamespace(),
                                          itemKey.toString(),
                                          item.getItemCategory() != null ? item.getItemCategory().toString() : null,
                                          returnInfo.getReturnValue(),
                                          item.isEdible(),
                                          (item instanceof BlockItem),
                                          item.canBeDepleted(),
                                          item instanceof BucketItem,
                                          itemstack.getTags().map((tag) -> tag.location().toString()).toList(),
                                          item.getClass()
                                  )
                          )
                          .ifPresent((stackSize) -> {
                              returnInfo.cancel();
                              returnInfo.setReturnValue(stackSize);
                          });
        }
        else
        {
            LOGGER.warn("Stack size ruleset is somehow null, using fallback logic. Called from " +
                                CallingClassUtil.getCallerClassName());
            
            if (returnInfo.getReturnValue() > 1)
            {
                returnInfo.cancel();
                returnInfo.setReturnValue(StackSizeHelper.getNewStackSize());
            }
        }
    }
    
    /**
     * Saves the stack size as an int instead of a byte.
     * Attempts to maintain some vanilla compatibility
     */
    @Redirect(method = "save",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putByte(Ljava/lang/String;B)V"))
    private void saveBigStack(CompoundTag tag, String key, byte p_128346_)
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
    @Redirect(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V",
              at = @At(value = "FIELD",
                       target = "Lnet/minecraft/world/item/ItemStack;count:I",
                       opcode = Opcodes.PUTFIELD))
    private void readBigStack(ItemStack instance, int value, CompoundTag tag)
    {
        ItemStackAccessor accessor = ((ItemStackAccessor) (Object) instance);
        
        if (tag.contains("BigCount"))
            accessor.accessSetCount(tag.getInt("BigCount"));
        else if (tag.getTagType("Count") == Tag.TAG_INT)
            accessor.accessSetCount(tag.getInt("Count"));
        else
            accessor.accessSetCount(tag.getByte("Count"));
    }
    
}
