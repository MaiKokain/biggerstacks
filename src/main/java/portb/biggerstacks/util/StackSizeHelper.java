package portb.biggerstacks.util;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.config.AutoSidedConfig;

public class StackSizeHelper
{
    /**
     * Scales the slot limit based on the original value
     */
    public static void scaleSlotLimit(CallbackInfoReturnable<Integer> callbackInfoReturnable){
        callbackInfoReturnable.cancel();
        callbackInfoReturnable.setReturnValue(scaleSlotLimit(callbackInfoReturnable.getReturnValue()));
    }
    
    public static void scaleMaxStackSize(CallbackInfoReturnable<Integer> callbackInfoReturnable){
        callbackInfoReturnable.cancel();
        callbackInfoReturnable.setReturnValue(scaleMaxStackSize(callbackInfoReturnable.getReturnValue()));
    }
    
    public static int scaleMaxStackSize(int original){
        return scaleSlotLimit(original);
    }
    
    /**
     * Scales the slot limit based on the original limit
     * @param original The original stack size
     * @return 64 if stack size has been lowered (to account for possible blacklisted/whitelisted items) or the scaled stack size. If the original slot has a limit of 1, 1 is returned.
     */
    public static int scaleSlotLimit(int original){
        int newStackSize = AutoSidedConfig.getGlobalMaxStackSize();
        
        //don't scale slots that are only meant to hold a single item
        if(original == 1)
            return 1;
        else if(newStackSize < 64) //can't trust original to be the actual original value
            return 64;
            
        return Math.max(original, original * newStackSize / 64);
    }
    
    public static int getNewStackSize(){
        return getNewSlotLimit();
    }
    
    /**
     * Increases slot limit without regard for the original size
     * @return The new stack size. If the new stack size would be lower than
     */
    public static int getNewSlotLimit(){
        return Math.max(AutoSidedConfig.getGlobalMaxStackSize(), 64);
    }
  
    public static void scaleTransferRate(CallbackInfoReturnable<Integer> callbackInfoReturnable, boolean respectSingle){
        callbackInfoReturnable.cancel();
        callbackInfoReturnable.setReturnValue(scaleTransferRate(callbackInfoReturnable.getReturnValue(), respectSingle));
    }
    
    public static int scaleTransferRate(int originalRate, boolean respectSingle){
        if(originalRate == 1 && respectSingle)
            return 1;
        
        return Math.max(1, originalRate * AutoSidedConfig.getGlobalMaxStackSize() / 64);
    }
    
    public static int increaseTransferRate(int originalRate){
        if(originalRate == 1)
            return 1;
        
        return Math.max(1, AutoSidedConfig.getGlobalMaxStackSize());
    }
}
