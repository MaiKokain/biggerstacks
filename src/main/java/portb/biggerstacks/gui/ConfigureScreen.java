/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.glfw.GLFW;
import portb.biggerstacks.Constants;
import portb.biggerstacks.net.ClientboundConfigureScreenOpenPacket;
import portb.biggerstacks.net.PacketHandler;
import portb.biggerstacks.net.ServerboundCreateConfigTemplatePacket;
import portb.configlib.template.TemplateOptions;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class ConfigureScreen extends Screen
{
    private static final int             WIDTH  = 200;
    private static final int             HEIGHT = 180;
    private final        TemplateOptions previousOptions;
    private final        boolean         isAlreadyUsingCustomFile;
    private              TextFieldWidget potionsBox;
    private              TextFieldWidget enchBooksBox;
    private              TextFieldWidget normalItemsBox;
    private              Button          confirmButton;
    
    protected ConfigureScreen(ClientboundConfigureScreenOpenPacket options)
    {
        super(new StringTextComponent("Configure stack size"));
        
        previousOptions = options;
        isAlreadyUsingCustomFile = options.isAlreadyUsingCustomFile();
    }
    
    public static void open(ClientboundConfigureScreenOpenPacket packet)
    {
        Minecraft.getInstance().setScreen(new ConfigureScreen(packet));
    }
    
    private static boolean isEditBoxInputValid(String inputString)
    {
        if (inputString.matches("[0-9]+"))
        {
            try
            {
                int value = Integer.parseInt(inputString);
                
                return value > 0 && value <= (Integer.MAX_VALUE / 2);
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override
    protected void init()
    {
        int relX = (this.width - WIDTH) / 2, relY = (this.height - HEIGHT) / 2;
        
        int editBoxStartX = 120, editBoxStartY = 30;
        
        normalItemsBox = new EditBoxWithADifferentBorderColour(
                font,
                relX + editBoxStartX,
                relY + editBoxStartY,
                60,
                20,
                new TranslationTextComponent("biggerstacks.normalbox.alt")
        );
        
        potionsBox = new EditBoxWithADifferentBorderColour(
                font,
                relX + editBoxStartX,
                relY + editBoxStartY + 30,
                60,
                20,
                new TranslationTextComponent("biggerstacks.potsbox.alt")
        );
        
        enchBooksBox = new EditBoxWithADifferentBorderColour(
                font,
                relX + editBoxStartX,
                relY + editBoxStartY + 60,
                60,
                20,
                new TranslationTextComponent("biggerstacks.enchbox.alt")
        );
        
        enchBooksBox.setValue("" + previousOptions.getEnchBookLimit());
        potionsBox.setValue("" + previousOptions.getPotionStackLimit());
        normalItemsBox.setValue("" + previousOptions.getNormalStackLimit());
        
        enchBooksBox.setResponder(verifyInputBoxNumber(enchBooksBox));
        potionsBox.setResponder(verifyInputBoxNumber(potionsBox));
        normalItemsBox.setResponder(verifyInputBoxNumber(normalItemsBox));
        
        confirmButton = new Button(relX + (WIDTH - 80) / 2,
                                   relY + HEIGHT - 30,
                                   80,
                                   20,
                                   new TranslationTextComponent("biggerstacks.save"),
                                   this::onConfirmButtonClicked
        );
        
        addButton(normalItemsBox);
        addButton(potionsBox);
        addButton(enchBooksBox);
        addButton(confirmButton);
        
        super.init();
    }
    
    @Override
    public void render(MatrixStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        renderBackground(pPoseStack, 0);
        
        int relX = (this.width - WIDTH) / 2, relY = (this.height - HEIGHT) / 2;
        
        drawCenteredString(pPoseStack,
                           font,
                           new TranslationTextComponent("biggerstacks.config.title"),
                           width / 2,
                           relY + 10,
                           0xffffff
        );
        
        int centreOffset = (20 - font.lineHeight) / 2;
        int labelStartX  = 20, labelStartY = 30;
        
        drawString(pPoseStack,
                   font,
                   new TranslationTextComponent("biggerstacks.normalbox.label"),
                   relX + labelStartX,
                   centreOffset + relY + labelStartY,
                   0xffffff
        );
        
        drawString(pPoseStack,
                   font,
                   new TranslationTextComponent("biggerstacks.potsbox.label"),
                   relX + labelStartX,
                   centreOffset + relY + labelStartY + 30,
                   0xffffff
        );
        
        drawString(pPoseStack,
                   font,
                   new TranslationTextComponent("biggerstacks.enchbox.label"),
                   relX + labelStartX,
                   centreOffset + relY + labelStartY + 60,
                   0xffffff
        );
        
        if (isAlreadyUsingCustomFile)
            renderCentered(
                    pPoseStack,
                    font,
                    new TranslationTextComponent("biggerstacks.overwrite.warn").withStyle(
                            Style.EMPTY.withColor(Color.fromRgb(0xffaaaa))),
                    WIDTH,
                    width / 2,
                    relY + 125
            );
        
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
    
    public void renderCentered(MatrixStack matrixStack, FontRenderer pFont, IFormattableTextComponent pFormattedText, int maxWidth, int pWidth, int pY)
    {
        
        List<TextWithWidth> widthList = pFont.split(pFormattedText,
                                                    maxWidth
        ).stream().map((text) -> new TextWithWidth(
                text,
                pFont.width(text)
        )).collect(
                Collectors.toList());
        
        for (TextWithWidth textWithWidth : widthList)
        {
            pFont.drawShadow(matrixStack,
                             textWithWidth.text,
                             (float) (pWidth - textWithWidth.width / 2),
                             (float) pY,
                             0xffffff
            );
    
            pY += 9;
        }
    }
    
    @Override
    public void renderBackground(MatrixStack pPoseStack, int pVOffset)
    {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        
        GuiUtils.drawContinuousTexturedBox(pPoseStack,
                                           Constants.CONFIG_GUI_BG,
                                           relX,
                                           relY,
                                           0,
                                           0,
                                           WIDTH,
                                           HEIGHT,
                                           256,
                                           256,
                                           4,
                                           0
        );
    }
    
    private void onConfirmButtonClicked(Button button)
    {
        if (isStateValid())
            submit();
    }
    
    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
    {
        if (pKeyCode == GLFW.GLFW_KEY_ENTER && isStateValid())
        {
            submit();
            
            return true;
        }
        
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
    
    private boolean isStateValid()
    {
        return isEditBoxInputValid(normalItemsBox.getValue())
                       && isEditBoxInputValid(enchBooksBox.getValue())
                       && isEditBoxInputValid(potionsBox.getValue());
    }
    
    private void submit()
    {
        //send packet to server
        PacketHandler.INSTANCE.sendToServer(new ServerboundCreateConfigTemplatePacket(
                Integer.parseInt(normalItemsBox.getValue()),
                Integer.parseInt(potionsBox.getValue()),
                Integer.parseInt(enchBooksBox.getValue())
        ));
        
        //close screen
        Minecraft.getInstance().setScreen(null);
    }
    
    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
    
    private Consumer<String> verifyInputBoxNumber(TextFieldWidget editBox)
    {
        return inputString -> {
            if (isEditBoxInputValid(inputString))
            {
                editBox.setTextColor(0xffffff);
                confirmButton.active = true;
            }
            else
            {
                editBox.setTextColor(0xff0000);
                confirmButton.active = false;
            }
        };
    }
    
    @OnlyIn(Dist.CLIENT)
    static class TextWithWidth
    {
        final IReorderingProcessor text;
        final int                  width;
        
        TextWithWidth(IReorderingProcessor pText, int pWidth)
        {
            this.text = pText;
            this.width = pWidth;
        }
    }
}
