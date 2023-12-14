package dev.turtywurty.turtyfoodmod.client.screen;

import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import dev.turtywurty.turtyfoodmod.menu.BlenderMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlenderScreen extends AbstractContainerScreen<BlenderMenu> {
    public static final Component TITLE =
            Component.translatable("container." + TurtyFoodMod.MOD_ID + ".blender");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TurtyFoodMod.MOD_ID, "textures/gui/blender.png");

    public BlenderScreen(BlenderMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int scaledProgress = this.menu.getProgressScaled();
        pGuiGraphics.blit(TEXTURE, this.leftPos + 81, this.topPos + 28, 176, 0, scaledProgress, 20);

        int scaledEnergy = this.menu.getEnergyScaled();
        pGuiGraphics.blit(TEXTURE, this.leftPos + 27, this.topPos + 65, 0, 166, scaledEnergy, 5);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        if (isHovering(27, 65, 122, 5, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(getMinecraft().font, Component.literal("Energy: " + this.menu.getEnergy() + " / " + this.menu.getMaxEnergy()), pMouseX, pMouseY);
        }
    }
}
