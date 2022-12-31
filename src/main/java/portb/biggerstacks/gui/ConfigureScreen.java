package portb.biggerstacks.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import portb.biggerstacks.Constants;
import portb.biggerstacks.net.ClientboundConfigureScreenOpenPacket;
import portb.biggerstacks.net.PacketHandler;
import portb.biggerstacks.net.ServerboundCreateConfigTemplatePacket;
import portb.configlib.template.TemplateOptions;

import java.util.function.Consumer;

public class ConfigureScreen extends Screen
{
    private static final int                               WIDTH  = 200;
    private static final int                               HEIGHT = 180;
    private final        TemplateOptions                   previousOptions;
    private final        boolean                           isAlreadyUsingCustomFile;
    private              EditBoxWithADifferentBorderColour potionsBox;
    private              EditBoxWithADifferentBorderColour enchBooksBox;
    private              EditBoxWithADifferentBorderColour normalItemsBox;
    private              Button                            confirmButton;
    
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
            drawCenteredString(pPoseStack,
                               font,
                               new TranslationTextComponent("biggerstacks.overwrite.warn"),
                               width / 2,
                               relY + 125,
                               0xffaaaa
            );
        
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
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
    
    void onConfirmButtonClicked(Button button)
    {
        if (
                isEditBoxInputValid(normalItemsBox.getValue())
                        && isEditBoxInputValid(enchBooksBox.getValue())
                        && isEditBoxInputValid(potionsBox.getValue())
        )
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
    }
    
    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
    
    private Consumer<String> verifyInputBoxNumber(EditBoxWithADifferentBorderColour editBox)
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
}
