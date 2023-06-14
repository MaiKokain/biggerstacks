/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
    
    private static String getStringForBigStackCount(int count)
    {
        if (ClientConfig.enableNumberShortening.get())
        {
            var decimal = new BigDecimal(count).round(new MathContext(3)); //pinnacle of over engineering
            
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
    
    private static double calculateStringScale(Font font, String countString)
    {
        var width = font.width(countString);
        
        if (width < 16)
            return 1.0;
        else
            return 16.0 / width;
    }
    
    // region "delete" all the vanilla item count rendering
    @Redirect(method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
              at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void doNothing1(PoseStack instance, float p_254202_, float p_253782_, float p_254238_)
    {
    
    }
    
    @Redirect(method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/renderer/MultiBufferSource;immediate(Lcom/mojang/blaze3d/vertex/BufferBuilder;)Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;"))
    private MultiBufferSource.BufferSource doNothing2(BufferBuilder p_109899_)
    {
        return null;
    }
    
    @Redirect(method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I"))
    private int doNothing3(Font instance, String p_272751_, float p_272661_, float p_273129_, int p_273272_, boolean p_273209_, Matrix4f p_272940_, MultiBufferSource p_273017_, Font.DisplayMode p_272608_, int p_273365_, int p_272755_)
    {
        return 0;
    }
    
    @Redirect(method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
              at = @At(value = "INVOKE",
                       target = "Lcom/mojang/blaze3d/vertex/Tesselator;getBuilder()Lcom/mojang/blaze3d/vertex/BufferBuilder;"))
    private BufferBuilder doNothing4(Tesselator instance)
    {
        return null;
    }
    
    @Redirect(method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V"))
    private void doNothing5(MultiBufferSource.BufferSource _unused)
    {
    }
    //endregion
    
    //Inject the new text rendering
    @Inject(method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I"))
    private void renderText(PoseStack poseStack, Font font, ItemStack itemStack, int x, int y, String alternateCount, CallbackInfo ci)
    {
        String text = alternateCount == null ? getStringForBigStackCount(itemStack.getCount()) : alternateCount;
        
        float scale        = (float) calculateStringScale(font, text);
        float inverseScale = 1 / scale;
        
        poseStack.scale(scale, scale, 1);
        
        poseStack.translate((x + 16) * inverseScale - font.width(text), (y + 16) * inverseScale - font.lineHeight, 200);
        
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch(
                text,
                /*pX*/ 0, //translation is done by poseStack. Doing it here just makes it harder.
                /*pY*/ 0,
                /*pColor*/ 16777215,
                /*pDropShadow*/ true,
                /*pMatrix*/ poseStack.last().pose(),
                /*pBufferSource*/ bufferSource,
                /*pTransparent*/ Font.DisplayMode.NORMAL,
                /*pBackgroundColour*/ 0,
                /*pPackedLightCoords*/ 15728880
        );
        
        bufferSource.endBatch();
    }
}
