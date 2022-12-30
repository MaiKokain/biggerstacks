package portb.biggerstacks.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.client.gui.ScreenUtils;
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
    //private static final MultiLineLabel TITLE_LABEL    = MultiLineLabel.create(Minecraft.getInstance().font, Component.translatable("biggerstacks.config.title"));
    private              MultiLineLabel                    OVERWRITE_WARNING_LABEL;
    private              MultiLineLabel                    POTIONS_BOX_LABEL;
    private              MultiLineLabel                    ENCH_BOOKS_BOX_LABEL;
    private              MultiLineLabel                    NORMAL_ITEMS_BOX_LABEL;
    private              EditBoxWithADifferentBorderColour potionsBox;
    private              EditBoxWithADifferentBorderColour enchBooksBox;
    private              EditBoxWithADifferentBorderColour normalItemsBox;
    private              Button                            confirmButton;
    
    protected ConfigureScreen(ClientboundConfigureScreenOpenPacket options)
    {
        super(Component.literal("Configure stack size"));
        
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
        
        OVERWRITE_WARNING_LABEL = MultiLineLabel.create(font,
                                                        Component.translatable("biggerstacks.overwrite.warn").withStyle(
                                                                Style.EMPTY.withColor(0xffaaaa)),
                                                        WIDTH
        );
        POTIONS_BOX_LABEL = MultiLineLabel.create(font, Component.translatable("biggerstacks.potsbox.label"));
        ENCH_BOOKS_BOX_LABEL = MultiLineLabel.create(font, Component.translatable("biggerstacks.enchbox.label"));
        NORMAL_ITEMS_BOX_LABEL = MultiLineLabel.create(font, Component.translatable("biggerstacks.normalbox.label"));
        
        normalItemsBox = new EditBoxWithADifferentBorderColour(
                font,
                relX + editBoxStartX,
                relY + editBoxStartY,
                60,
                20,
                Component.translatable("biggerstacks.normalbox.alt")
        );
        
        potionsBox = new EditBoxWithADifferentBorderColour(
                font,
                relX + editBoxStartX,
                relY + editBoxStartY + 30,
                60,
                20,
                Component.translatable("biggerstacks.potsbox.alt")
        );
        
        enchBooksBox = new EditBoxWithADifferentBorderColour(
                font,
                relX + editBoxStartX,
                relY + editBoxStartY + 60,
                60,
                20,
                Component.translatable("biggerstacks.enchbox.alt")
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
                                   Component.translatable("biggerstacks.save"),
                                   this::onConfirmButtonClicked
        );
        
        addRenderableWidget(normalItemsBox);
        addRenderableWidget(potionsBox);
        addRenderableWidget(enchBooksBox);
        addRenderableWidget(confirmButton);
        
        super.init();
    }
    
    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        renderBackground(pPoseStack, 0);
        
        int relX = (this.width - WIDTH) / 2, relY = (this.height - HEIGHT) / 2;
        
        drawCenteredString(pPoseStack,
                           font,
                           Component.translatable("biggerstacks.config.title"),
                           width / 2,
                           relY + 10,
                           0xffffff
        );
        
        int centreOffset = (20 - font.lineHeight) / 2;
        int labelStartX  = 20, labelStartY = 30;
        NORMAL_ITEMS_BOX_LABEL.renderLeftAligned(pPoseStack,
                                                 relX + labelStartX,
                                                 centreOffset + relY + labelStartY,
                                                 font.lineHeight,
                                                 0xffffff
        );
        POTIONS_BOX_LABEL.renderLeftAligned(pPoseStack,
                                            relX + labelStartX,
                                            centreOffset + relY + labelStartY + 30,
                                            font.lineHeight,
                                            0xffffff
        );
        ENCH_BOOKS_BOX_LABEL.renderLeftAligned(pPoseStack,
                                               relX + labelStartX,
                                               centreOffset + relY + labelStartY + 60,
                                               font.lineHeight,
                                               0xffffff
        );
        
        if (isAlreadyUsingCustomFile)
            OVERWRITE_WARNING_LABEL.renderCentered(pPoseStack, width / 2, relY + 125);
        
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
    
    @Override
    public void renderBackground(PoseStack pPoseStack, int pVOffset)
    {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, Constants.CONFIG_GUI_BG);
        
        ScreenUtils.blitWithBorder(pPoseStack, relX, relY, 0, 0, WIDTH, HEIGHT, 256, 256, 4, 200);
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
