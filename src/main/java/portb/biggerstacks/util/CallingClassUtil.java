package portb.biggerstacks.util;

import net.minecraft.world.item.Item;

public class CallingClassUtil
{
    public static String getCallerClassName()
    {
        for(StackTraceElement element : Thread.currentThread().getStackTrace())
        {
            if(!element.getClassName().equals(Item.class.getName()) && !element.getClassName().startsWith("java.lang.Thread"))
                return element.getClassName();
        }
        
        return null;
    }
}
