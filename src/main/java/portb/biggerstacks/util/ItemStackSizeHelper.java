/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.StackSizeRules;
import portb.configlib.ItemProperties;
import portb.configlib.TagAccessor;

import static portb.biggerstacks.BiggerStacks.LOGGER;

public class ItemStackSizeHelper
{
    public static void applyStackSizeToItem(CallbackInfoReturnable<Integer> returnInfo, ItemStack itemstack, Item item)
    {
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
                                          new TagAccessorImpl(itemstack),
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
            LOGGER.debug("Stack size ruleset is somehow null, using fallback logic. Called from " +
                                CallingClassUtil.getCallerClassName());
            
            if (returnInfo.getReturnValue() > 1)
            {
                returnInfo.cancel();
                returnInfo.setReturnValue(SlotLimitHelper.getNewStackSize());
            }
        }
    }
    
    private record TagAccessorImpl(ItemStack item) implements TagAccessor
    {
        @Override
        public boolean doesItemHaveTag(String tag)
        {
            return item.is(new TagKey<>(Registry.ITEM_REGISTRY, new ResourceLocation(tag)));
        }
    }
}
