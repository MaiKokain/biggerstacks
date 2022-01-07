package portb.biggerstacks.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import portb.biggerstacks.config.ClientConfig;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

import static portb.biggerstacks.Constants.*;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin
{
    private static final DecimalFormat BILLION_FORMAT = new DecimalFormat("#.##B");
    private static final DecimalFormat MILLION_FORMAT = new DecimalFormat("#.##M");
    private static final DecimalFormat THOUSAND_FORMAT = new DecimalFormat("#.##K");

    @Redirect(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(I)Ljava/lang/String;"))
    private String getStringForBigStackCount(int count)
    {
        if (ClientConfig.enableNumberShortening.get())
        {
            BigDecimal decimal = new BigDecimal(count).round(new MathContext(4)); //pinnacle of over engineering

            var value = decimal.doubleValue();

            if (value >= ONE_BILLION)
                return BILLION_FORMAT.format(value / ONE_BILLION);
            else if (value >= ONE_MILLION)
                return MILLION_FORMAT.format(value / ONE_MILLION);
            else if (value > ONE_THOUSAND)
                return THOUSAND_FORMAT.format(value / ONE_THOUSAND);
        }

        return String.valueOf(count);
    }

    //scale down fonts to fit
    @Surrogate
    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void pushStack(Font font, ItemStack tesselator, int x, int y, String j, CallbackInfo ci, PoseStack posestack, String countString)
    {
        posestack.pushPose();
        float scale = (float) calculateStringScale(font, countString);

        posestack.scale(scale, scale, 1);
    }

    //move the text to the correct place
    @Surrogate
    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLcom/mojang/math/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;ZII)I"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void translateStackBack(Font font, ItemStack itemStack, int x, int y, String _a, CallbackInfo ci, PoseStack posestack, String countString, MultiBufferSource.BufferSource multibuffersource$buffersource)
    {
        //don't ask wtf this shit is.

        int width = font.width(countString);
        double scale = calculateStringScale(font, countString);
        double extraXOffset = scale == 1 ? 0 : 1 / (scale * 2);
        double extraYOffset = scale == 1 ? 0 : 1.5 / (scale);

        posestack.translate(-(x + 19 - 2 - width), -(y + 6 + 3), 0); //translate back to 0,0 for easier accounting for scaling

        posestack.translate(
                (x + 19 - 2 - extraXOffset - width * scale) / scale,
                (y + 6 + 3) / scale - (9 - 9 / scale) - extraYOffset, //this is stupid
                0
        );
    }

    private double calculateStringScale(Font font, String countString)
    {
        int width = font.width(countString);

        if (width < 16)
            return 1.0;
        else
            return 16.0 / width;
    }

    @Surrogate
    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLcom/mojang/math/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;ZII)I", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void popStack(Font font, ItemStack itemStack, int _a, int _b, String _c, CallbackInfo callbackInfo, PoseStack posestack, String s, MultiBufferSource.BufferSource bufferSource)
    {
        posestack.popPose();
    }
}
