package portb.biggerstacks.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin
{
    //make stacks of items dropped on the ground look extra thicc
    @Inject(method = "getRenderAmount", at=@At("HEAD"), cancellable = true)
    private void getBiggerStackRenderAmount(ItemStack stack, CallbackInfoReturnable<Integer> returnInfo)
    {
        returnInfo.cancel();
        returnInfo.setReturnValue(calculateDisplayStackAmount(stack.getCount()));
    }

    private int calculateDisplayStackAmount(int count)
    {
        return (int)Math.floor(Math.log(count) / Math.log(4)) + 1;
    }

    /*@ModifyArg(method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", ordinal = 4), index = 2)*/
    @ModifyConstant(method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
    constant = @Constant(doubleValue = 0.09375F))
    private double reduceItemSpread(double value, ItemEntity entity, float p_115037_, float p_115038_, PoseStack p_115039_, MultiBufferSource p_115040_, int p_115041_)
    {
        int displayCount = calculateDisplayStackAmount(entity.getItem().getCount());
        double distanceScale = displayCount < 5 ? value : value * ((double)5 / displayCount);

        return distanceScale;
    }

    @ModifyConstant(method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            constant = @Constant(floatValue = -0.09375F))
    private float reduceItemSpreadFloat(float value, ItemEntity entity, float p_115037_, float p_115038_, PoseStack p_115039_, MultiBufferSource p_115040_, int p_115041_)
    {
        int displayCount = calculateDisplayStackAmount(entity.getItem().getCount());
        float distanceScale = displayCount < 5 ? value : value * ((float)5 / displayCount);

        return distanceScale;
    }
}
