package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.BiggerStacks;
import portb.biggerstacks.config.AutoSidedConfig;
import portb.biggerstacks.util.ItemExtension;

@Mixin(Item.class)
public class ItemMixin implements ItemExtension
{
    private Integer originalMaxStackSize = null;

    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo)
    {
        //Store the original stack size of the item before increasing it
        originalMaxStackSize = returnInfo.getReturnValue();

        var item = ((Item) (Object) this).getDefaultInstance();

        //if whitelist is enabled and the item isn't whitelisted, don't increase its stack size
        if (AutoSidedConfig.isUsingWhitelist() && !item.is(BiggerStacks.WHITELIST_TAG))
            return;
            //check if this item has the blacklist tag, and if it does, don't increase its stack size
        else if (item.is(BiggerStacks.BLACKLIST_TAG))
            return;

        if (returnInfo.getReturnValue() != 1)
        {
            returnInfo.cancel();
            returnInfo.setReturnValue(AutoSidedConfig.getMaxStackSize());
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
