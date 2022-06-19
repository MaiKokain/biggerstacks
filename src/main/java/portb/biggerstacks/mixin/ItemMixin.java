package portb.biggerstacks.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.BiggerStacks;
import portb.biggerstacks.config.AutoSidedConfig;

@Mixin(Item.class)
public class ItemMixin
{
    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void biggerMaxStackSize(CallbackInfoReturnable<Integer> returnInfo)
    {
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
}
