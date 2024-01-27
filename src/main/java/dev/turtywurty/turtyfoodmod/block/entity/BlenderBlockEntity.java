package dev.turtywurty.turtyfoodmod.block.entity;

import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import dev.turtywurty.turtyfoodmod.block.entity.util.SyncedBlockEntity;
import dev.turtywurty.turtyfoodmod.block.entity.util.SyncedEnergyStorage;
import dev.turtywurty.turtyfoodmod.block.entity.util.SyncedItemStackHandler;
import dev.turtywurty.turtyfoodmod.block.entity.util.TickableBlockEntity;
import dev.turtywurty.turtyfoodmod.client.screen.BlenderScreen;
import dev.turtywurty.turtyfoodmod.init.BlockEntityTypeInit;
import dev.turtywurty.turtyfoodmod.menu.BlenderMenu;
import dev.turtywurty.turtyfoodmod.recipe.BlenderRecipe;
import mcp.client.Start;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlenderBlockEntity extends SyncedBlockEntity implements TickableBlockEntity, MenuProvider {
    private final SyncedItemStackHandler input0 = SyncedItemStackHandler.create(this).build();
    private final SyncedItemStackHandler input1 = SyncedItemStackHandler.create(this).build();
    private final SyncedItemStackHandler input2 = SyncedItemStackHandler.create(this).build();
    private final SyncedItemStackHandler output = SyncedItemStackHandler.create(this).build();

    private final SyncedEnergyStorage energy = SyncedEnergyStorage.create(this)
            .capacity(10000)
            .maxReceive(1000)
            .build();

    private static final int ENERGY_COST = 100;
    private int progress = 0, maxProgress = 0;

    private ResourceLocation previousRecipe = null;
    private @Nullable RecipeHolder<BlenderRecipe> currentRecipe = null;

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> BlenderBlockEntity.this.progress;
                case 1 -> BlenderBlockEntity.this.maxProgress;
                case 2 -> BlenderBlockEntity.this.energy.getEnergyStored();
                case 3 -> BlenderBlockEntity.this.energy.getMaxEnergyStored();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public BlenderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityTypeInit.BLENDER.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide)
            return;

        if (this.currentRecipe == null) {
            this.currentRecipe = getRecipe().orElse(null);
            if (this.currentRecipe == null) {
                // start decrementing progress if there is no recipe
                if (this.progress > 0)
                    this.progress--;
                else {
                    this.maxProgress = 0;
                    this.previousRecipe = null;
                }

                return;
            }

            this.previousRecipe = this.currentRecipe.id();
            this.maxProgress = this.currentRecipe.value().getProcessTime();
        } else if (this.previousRecipe != null) {
            // Check if the recipe has changed by seeing what the new recipe would be
            RecipeHolder<BlenderRecipe> newRecipe = getRecipe().orElse(null);
            if (newRecipe == null || !newRecipe.id().equals(this.previousRecipe)) {
                this.currentRecipe = null;
                return;
            }
        }

        if (this.energy.getEnergyStored() >= ENERGY_COST) {
            if (this.progress < this.maxProgress) {
                this.progress++;
                this.energy.takeEnergy(ENERGY_COST);
            } else {
                this.progress = 0;
                this.maxProgress = 0;

                this.currentRecipe.value().assemble(this);

                this.currentRecipe = null;
                this.previousRecipe = null;
            }
        }
    }

    private SimpleContainer createRecipeContainer() {
        var inventory = new SimpleContainer(3);
        inventory.setItem(0, this.input0.getStackInSlot(0).copy());
        inventory.setItem(1, this.input1.getStackInSlot(0).copy());
        inventory.setItem(2, this.input2.getStackInSlot(0).copy());
        return inventory;
    }

    private Optional<RecipeHolder<BlenderRecipe>> getRecipe() {
        if (this.level == null || this.level.isClientSide)
            return Optional.empty();

        return this.level.getRecipeManager()
                .getRecipeFor(
                        BlenderRecipe.Type.INSTANCE,
                        createRecipeContainer(),
                        this.level);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        var turtyFoodModTag = new CompoundTag();

        var inventoryTag = new CompoundTag();
        inventoryTag.put("input0", this.input0.serializeNBT());
        inventoryTag.put("input1", this.input1.serializeNBT());
        inventoryTag.put("input2", this.input2.serializeNBT());
        inventoryTag.put("output", this.output.serializeNBT());
        turtyFoodModTag.put("inventory", inventoryTag);

        turtyFoodModTag.put("energy", this.energy.serializeNBT());

        turtyFoodModTag.putInt("progress", this.progress);
        turtyFoodModTag.putInt("maxProgress", this.maxProgress);

        turtyFoodModTag.putString("currentRecipe", this.currentRecipe == null ? "" : this.currentRecipe.id().toString());

        pTag.put(TurtyFoodMod.MOD_ID, turtyFoodModTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        var turtyFoodModTag = pTag.getCompound(TurtyFoodMod.MOD_ID);

        var inventoryTag = turtyFoodModTag.getCompound("inventory");
        this.input0.deserializeNBT(inventoryTag.getCompound("input0"));
        this.input1.deserializeNBT(inventoryTag.getCompound("input1"));
        this.input2.deserializeNBT(inventoryTag.getCompound("input2"));
        this.output.deserializeNBT(inventoryTag.getCompound("output"));

        this.energy.deserializeNBT(turtyFoodModTag.get("energy"));

        this.progress = turtyFoodModTag.getInt("progress");
        this.maxProgress = turtyFoodModTag.getInt("maxProgress");

        String currentRecipe = turtyFoodModTag.getString("currentRecipe");
        this.currentRecipe = currentRecipe.isBlank() ? null : getRecipe(ResourceLocation.tryParse(currentRecipe));
    }

    @SuppressWarnings("unchecked")
    private RecipeHolder<BlenderRecipe> getRecipe(ResourceLocation currentRecipe) {
        if (this.level == null || this.level.isClientSide)
            return null;

        return (RecipeHolder<BlenderRecipe>) this.level.getRecipeManager()
                .byKey(currentRecipe)
                .filter(holder -> holder.value() instanceof BlenderRecipe)
                .orElse(null);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        this.input0.invalidate();
        this.input1.invalidate();
        this.input2.invalidate();
        this.output.invalidate();

        this.energy.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) {
                return this.input0.getLazyOptional().cast();
            } else if (side == Direction.EAST) {
                return this.input1.getLazyOptional().cast();
            } else if (side == Direction.WEST) {
                return this.input2.getLazyOptional().cast();
            } else if (side == Direction.DOWN) {
                return this.output.getLazyOptional().cast();
            }
        }

        if (cap == ForgeCapabilities.ENERGY)
            return this.energy.getLazyOptional().cast();

        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return BlenderScreen.TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new BlenderMenu(pContainerId, pPlayerInventory, this, this.containerData);
    }

    public void dropContents() {
        dropItemHandler(this.level, this.input0, this.worldPosition);
        dropItemHandler(this.level, this.input1, this.worldPosition);
        dropItemHandler(this.level, this.input2, this.worldPosition);
        dropItemHandler(this.level, this.output, this.worldPosition);
    }

    private static void dropItemHandler(Level level, IItemHandler handler, BlockPos pos) {
        for (int index = 0; index < handler.getSlots(); index++) {
            ItemStack stack = handler.getStackInSlot(index);
            if (!stack.isEmpty()) {
                Block.popResource(level, pos, stack);
            }
        }
    }

    public SyncedEnergyStorage getEnergy() {
        return this.energy;
    }

    public SyncedItemStackHandler getInput0() {
        return this.input0;
    }

    public SyncedItemStackHandler getInput1() {
        return this.input1;
    }

    public SyncedItemStackHandler getInput2() {
        return this.input2;
    }

    public SyncedItemStackHandler getOutput() {
        return this.output;
    }
}
