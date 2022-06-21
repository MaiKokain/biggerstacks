package portb.biggerstacks.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.BiggerStacks;
import portb.biggerstacks.util.ItemExtension;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(Item.class)
public class ItemMixin implements ItemExtension
{
    private Integer originalMaxStackSize = null;

    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        //Store the original stack size of the item
        originalMaxStackSize = returnInfo.getReturnValue();

        Item item = ((Item) (Object) this);

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

    @Override
    public int getOriginalMaxStackSize()
    {
        if(originalMaxStackSize == null)
            //noinspection deprecation,ResultOfMethodCallIgnored
            ((Item)(Object)this).getMaxStackSize();

        //noinspection ConstantConditions
        return originalMaxStackSize;
    }

    @Override
    public boolean hasStackSizeBeenIncreased() {
        try
        {
            @SuppressWarnings("ConstantConditions") Item item = (Item)(Object)this;

            //if whitelist is enabled and the item isn't whitelisted, don't increase its stack size
            if (AutoSidedConfig.isUsingWhitelist() && !item.is(BiggerStacks.WHITELIST_TAG))
                return false;
                //check if this item has the blacklist tag, and if it does, don't increase its stack size
            else if (item.is(BiggerStacks.BLACKLIST_TAG))
                return false;
        }
        catch (IllegalStateException e)
        {
            System.err.println("Tags are not bound at this time! Assuming all items are whitelisted");
        }

        return true;
    }
}
