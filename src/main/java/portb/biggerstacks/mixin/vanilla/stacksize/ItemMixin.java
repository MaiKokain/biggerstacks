package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.util.CallingClassUtil;
import portb.biggerstacks.util.StackSizeHelper;
import portb.configlib.ItemProperties;

import static portb.biggerstacks.BiggerStacks.LOGGER;

//fixme i'm almost certain this isn't needed.
@Mixin(Item.class)
public class ItemMixin
{
    /**
     * Increases the maximum stack size
     */
    @Inject(method = "getMaxStackSize",
            at = @At("RETURN"),
            cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        //System.out.println("SOMEONE IS USING DEPRECATED API!!!!!!!!!!!!!!!!!!!!!!!!!!!!! REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        var                                         item      = ((Item) (Object) this);
        @SuppressWarnings("ConstantConditions") var itemstack = item.getDefaultInstance();
        
        if (StackSizeRules.getRuleSet() != null)
        {
            
            StackSizeRules.getRuleSet().determineStackSizeForItem(
                                  new ItemProperties(
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
                              returnInfo.setReturnValue(Math.min(stackSize, AutoSidedConfig.getGlobalMaxStackSize()));
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
    
    
}
