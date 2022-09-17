package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.BiggerStacks;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.configlib.ItemProperties;
import portb.biggerstacks.util.ItemExtension;
import portb.biggerstacks.util.StackSizeHelper;

import static portb.biggerstacks.BiggerStacks.LOGGER;

@Mixin(Item.class)
public class ItemMixin implements ItemExtension
{
    private Integer originalMaxStackSize = null;
    
    /**
     * I don't think it is necessary to mixin to Item AND ItemStack, since ItemStack (by default) just uses Item functions.
     * Item.getMaxStackSize is "deprecated" (I don't know what that's supposed to mean because forge sometimes just removes
     * apis instead of @Deprecating them). For now I will leave this here and just mixin to ItemStack because it's a final
     * class and because it's not deprecated.
     */
    //@Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        //Store the original stack size of the item before increasing it
        originalMaxStackSize = returnInfo.getReturnValue();

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
                                                                          itemstack.getTags().map((tag) -> tag.location().toString()).toList()
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

    @Override
    public int getOriginalMaxStackSize()
    {
        if (originalMaxStackSize == null)
            //noinspection deprecation,ResultOfMethodCallIgnored
            ((Item) (Object) this).getMaxStackSize();

        //noinspection ConstantConditions
        return originalMaxStackSize;
    }

    @Override
    public boolean hasStackSizeBeenIncreased()
    {
        @SuppressWarnings("ConstantConditions") var item = ((Item) (Object) this).getDefaultInstance();

        //if whitelist is enabled and the item isn't whitelisted, don't increase its stack size
        if (AutoSidedConfig.isUsingWhitelist() && !item.is(BiggerStacks.WHITELIST_TAG))
            return false;
            //check if this item has the blacklist tag, and if it does, don't increase its stack size
        else return !item.is(BiggerStacks.BLACKLIST_TAG);
    }
}
