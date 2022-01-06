package portb.biggerstacks.mixin.client;

import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin
{
    //make stacks of items dropped on the ground look extra thicc
    @Inject(method = "getRenderAmount", at=@At("HEAD"), cancellable = true)
    private void getBiggerStackRenderAmount(ItemStack stack, CallbackInfoReturnable<Integer> returnInfo)
    {
        returnInfo.cancel();
        returnInfo.setReturnValue((int)Math.floor(Math.log(stack.getCount()) / Math.log(4)) + 1);
    }
}
