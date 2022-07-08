package portb.biggerstacks.mixin.compat.industrialforegoing;

import com.buuz135.industrial.block.generator.mycelial.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin({CrimedGeneratorType.class,
        CulinaryGeneratorType.class,
        DeathGeneratorType.class,
        EnderGeneratorType.class,
        ExplosiveGeneratorType.class,
        FireworkGeneratorType.class,
        FrostyGeneratorType.class,
        FurnaceGeneratorType.class,
        HalitosisGeneratorType.class,
        MagmaGeneratorType.class,
        MeatallurgicGeneratorType.class,
        NetherStarGeneratorType.class,
        PinkGeneratorType.class,
        SlimeyGeneratorType.class})
public class IndustrialForegoingCompat
{
    @ModifyConstant(constant = @Constant(intValue = 64), method = "getSlotSize", remap = false)
    private int increaseStackLimit(int value)
    {
        return StackSizeHelper.getNewSlotLimit();
    }
}
