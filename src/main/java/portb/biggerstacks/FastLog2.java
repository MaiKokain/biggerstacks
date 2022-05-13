package portb.biggerstacks;

public class FastLog2
{
    public static int fastLog2(int value)
    {
        //https://stackoverflow.com/a/3305710
        if (value == 0)
            return 0;
        return 31 - Integer.numberOfLeadingZeros(value);
    }
}
