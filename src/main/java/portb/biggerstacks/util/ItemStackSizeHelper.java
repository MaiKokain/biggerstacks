/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.util;

import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.StackSizeRules;
import portb.configlib.ItemProperties;

import java.util.stream.Collectors;

import static portb.biggerstacks.BiggerStacks.LOGGER;

public class ItemStackSizeHelper
{
    public static void applyStackSizeToItem(CallbackInfoReturnable<Integer> returnInfo, Item item)
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
                                          item.getTags().stream().map(ResourceLocation::toString).collect(Collectors.toList()),
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
                returnInfo.setReturnValue(SlotLimitHelper.getNewStackSize());
            }
        }
    }
}
