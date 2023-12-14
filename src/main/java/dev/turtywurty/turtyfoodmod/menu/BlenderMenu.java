package dev.turtywurty.turtyfoodmod.menu;

import dev.turtywurty.turtyfoodmod.block.entity.BlenderBlockEntity;
import dev.turtywurty.turtyfoodmod.init.BlockInit;
import dev.turtywurty.turtyfoodmod.init.MenuTypeInit;
import dev.turtywurty.turtyfoodmod.init.TagInit;
import dev.turtywurty.turtyfoodmod.menu.util.OutputSlotItemHandler;
import dev.turtywurty.turtyfoodmod.menu.util.TagSlotItemHandler;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class BlenderMenu extends AbstractContainerMenu {
    private final BlenderBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData containerData;

    public BlenderMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf additionalData) {
        this(pContainerId,
                pPlayerInventory,
                getBlockEntity(pPlayerInventory, additionalData),
                new SimpleContainerData(4));
    }

    public BlenderMenu(int pContainerId, Inventory pPlayerInventory, BlockEntity blockEntity, ContainerData containerData) {
        super(MenuTypeInit.BLENDER.get(), pContainerId);

        if (!(blockEntity instanceof BlenderBlockEntity blenderBlockEntity))
            throw new IllegalArgumentException("BlockEntity is not of type BlenderBlockEntity!");

        this.blockEntity = blenderBlockEntity;
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.containerData = containerData;
        addDataSlots(containerData);

        createPlayerHotbar(pPlayerInventory);
        createPlayerInventory(pPlayerInventory);
        createBeInventory();
    }

    private void createPlayerInventory(Inventory playerInv) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv,
                        9 + column + (row * 9),
                        8 + (column * 18),
                        84 + (row * 18)));
            }
        }
    }

    private void createPlayerHotbar(Inventory playerInv) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv,
                    column,
                    8 + (column * 18),
                    142));
        }
    }

    private void createBeInventory() {
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).ifPresent(handler ->
                addSlot(new TagSlotItemHandler(handler, 0, 29, 21, TagInit.Items.BLENDABLE)));

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.EAST).ifPresent(handler ->
                addSlot(new TagSlotItemHandler(handler, 0, 47, 21, TagInit.Items.BLENDABLE)));

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.WEST).ifPresent(handler ->
                addSlot(new TagSlotItemHandler(handler, 0, 29, 39, TagInit.Items.BLENDABLE)));

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(handler ->
                addSlot(new OutputSlotItemHandler(handler, 0, 132, 30)));
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot fromSlot = getSlot(pIndex);
        ItemStack fromStack = fromSlot.getItem();

        if (fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if (!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyFromStack = fromStack.copy();

        if (pIndex < 36) {
            // We are inside of the player's inventory
            if (!moveItemStackTo(fromStack, 36, 39, false))
                return ItemStack.EMPTY;
        } else if (pIndex < 39) {
            // We are inside of the block entity input slots
            if (!moveItemStackTo(fromStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else if (pIndex < 40) {
            // We are inside of the block entity output slot
            if (!moveItemStackTo(fromStack, 0, 39, false))
                return ItemStack.EMPTY;
        } else {
            System.err.println("Invalid slot index: " + pIndex);
            return ItemStack.EMPTY;
        }

        fromSlot.setChanged();
        fromSlot.onTake(pPlayer, fromStack);

        return copyFromStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.levelAccess, pPlayer, BlockInit.BLENDER.get());
    }

    public BlenderBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public int getProgress() {
        return this.containerData.get(0);
    }

    public int getMaxProgress() {
        return this.containerData.get(1);
    }

    public int getProgressScaled() {
        if (getMaxProgress() == 0)
            return 0;

        return getProgress() * 33 / getMaxProgress();
    }

    public int getEnergy() {
        return this.containerData.get(2);
    }

    public int getMaxEnergy() {
        return this.containerData.get(3);
    }

    public int getEnergyScaled() {
        if (getMaxEnergy() == 0)
            return 0;

        return getEnergy() * 124 / getMaxEnergy();
    }

    private static BlockEntity getBlockEntity(Inventory pPlayerInventory, FriendlyByteBuf additionalData) {
        return pPlayerInventory.player.level().getBlockEntity(additionalData.readBlockPos());
    }
}
