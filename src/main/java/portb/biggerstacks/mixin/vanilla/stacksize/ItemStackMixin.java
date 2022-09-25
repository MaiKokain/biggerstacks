package portb.biggerstacks.mixin.vanilla.stacksize;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.BiggerStacks;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.configlib.ItemProperties;
import portb.biggerstacks.util.StackSizeHelper;

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
        
        if(StackSizeRules.getRuleSet() != null)
        {
    
            StackSizeRules.getRuleSet().determineStackSizeForItem(new ItemProperties(
                                                                          item.getRegistryName().getNamespace(),
                                                                          item.getRegistryName().toString(),
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
                              //cap max stack size to the global max
                              returnInfo.setReturnValue(Math.min(stackSize, AutoSidedConfig.getMaxStackSize()));
                          });
        }
        else
        {
            LOGGER.warn("Stack size ruleset is somehow null, using fallback logic");
            
            if(returnInfo.getReturnValue() > 1)
            {
                returnInfo.cancel();
                returnInfo.setReturnValue(StackSizeHelper.getNewStackSize());
            }
        }
    }

    /**
     * Saves the stack size as an int instead of a byte.
     * This will cause the stack to be deleted if the world is loaded without this mod installed.
     */
    @Redirect(method = "save",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putByte(Ljava/lang/String;B)V"))
    private void saveBigStack(CompoundTag tag, String key, byte p_128346_)
    {
        int count = ((ItemStack) (Object) this).getCount();
        tag.putInt(key, count);
    }

    /**
     * Reads the stack size as an int instead of a byte
     * This will cause the stack to be deleted if the world is loaded without this mod installed.
     */
    @Redirect(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V",
              at = @At(value = "FIELD",
                       target = "Lnet/minecraft/world/item/ItemStack;count:I",
                       opcode = Opcodes.PUTFIELD))
    private void readBigStack(ItemStack instance, int value, CompoundTag tag)
    {
        ((ItemStackAccessor) (Object) instance).accessSetCount(tag.getInt("Count"));
    }

}
