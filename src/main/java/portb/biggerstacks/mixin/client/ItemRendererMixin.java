/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
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
    private static final DecimalFormat BILLION_FORMAT  = new DecimalFormat("#.##B");
    private static final DecimalFormat MILLION_FORMAT  = new DecimalFormat("#.##M");
    private static final DecimalFormat THOUSAND_FORMAT = new DecimalFormat("#.##K");
    
    @Redirect(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
              at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(I)Ljava/lang/String;"))
    private String getStringForBigStackCount(int count)
    {
        if (ClientConfig.enableNumberShortening.get())
        {
            BigDecimal decimal = new BigDecimal(count).round(new MathContext(3)); //pinnacle of over engineering
            
            double value = decimal.doubleValue();
            
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
    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE",
                     target = "Lcom/mojang/blaze3d/matrix/MatrixStack;translate(DDD)V",
                     shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void pushStack(FontRenderer font, ItemStack stack, int x, int y, String j, CallbackInfo ci, MatrixStack matrixStack, String countString)
    {
        matrixStack.pushPose();
        float scale = (float) calculateStringScale(font, countString);
        
        matrixStack.scale(scale, scale, 1);
    }
    
    //move the text to the correct place
    @Surrogate
    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/gui/FontRenderer;drawInBatch(Ljava/lang/String;FFIZLnet/minecraft/util/math/vector/Matrix4f;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ZII)I"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void translateStackBack(FontRenderer font, ItemStack itemStack, int x, int y, String _a, CallbackInfo ci, MatrixStack matrixStack, String countString, IRenderTypeBuffer.Impl renderTypeBuffer)
    {
        int    width        = font.width(countString);
        double scale        = calculateStringScale(font, countString);
        double extraXOffset = scale == 1 ? 0 : 1 / (scale * 2);
        double extraYOffset = scale == 1 ? 0 : 1.5 / (scale);
        
        //translate back to 0,0 for easier accounting for scaling
        matrixStack.translate(-(x + 19 - 2 - width),
                              -(y + 6 + 3),
                              0
        );
        
        //i just messed around until i found something that felt right
        matrixStack.translate(
                (x + 19 - 2 - extraXOffset - width * scale) / scale,
                (y + 6 + 3) / scale - (9 - 9 / scale) - extraYOffset, //this is stupid
                0
        );
    }
    
    private double calculateStringScale(FontRenderer font, String countString)
    {
        int width = font.width(countString);
        
        if (width < 16)
            return 1.0;
        else
            return 16.0 / width;
    }
    
    @Surrogate
    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/gui/FontRenderer;drawInBatch(Ljava/lang/String;FFIZLnet/minecraft/util/math/vector/Matrix4f;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ZII)I",
                     shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void popStack(FontRenderer font, ItemStack itemStack, int _a, int _b, String _c, CallbackInfo callbackInfo, MatrixStack matrixStack, String s, IRenderTypeBuffer.Impl bufferSource)
    {
        matrixStack.popPose();
    }
}
