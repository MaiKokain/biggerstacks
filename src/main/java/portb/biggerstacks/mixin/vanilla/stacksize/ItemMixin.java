package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.configlib.ItemProperties;
import portb.biggerstacks.util.StackSizeHelper;

import static portb.biggerstacks.BiggerStacks.LOGGER;

@Mixin(Item.class)
public class ItemMixin
{
    /**
     * I don't think it is necessary to mixin to Item AND ItemStack, since ItemStack (by default) just uses Item functions.
     * Item.getMaxStackSize is "deprecated" (I don't know what that's supposed to mean because forge sometimes just removes
     * apis instead of @Deprecating them). For now I will leave this here and just mixin to ItemStack because it's a final
     * class and because it's not deprecated.
     */
    //@Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        var item = ((Item) (Object) this);
        //not sure that this is necessary since the only reason I need it is to use tags, but .is() uses .getItem() anyway....
        var itemstack = item.getDefaultInstance();
    
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
}
