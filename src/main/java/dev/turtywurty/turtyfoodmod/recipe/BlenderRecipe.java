package dev.turtywurty.turtyfoodmod.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import dev.turtywurty.turtyfoodmod.block.entity.BlenderBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlenderRecipe implements Recipe<Container> {
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;
    private final int processTime;

    public BlenderRecipe(List<Ingredient> inputs, ItemStack output, int processTime) {
        this.inputs = NonNullList.withSize(3, Ingredient.EMPTY);
        for (int index = 0; index < inputs.size(); index++) {
            this.inputs.set(index, inputs.get(index));
        }

        this.output = output;
        this.processTime = processTime;
    }

    public int getProcessTime() {
        return this.processTime;
    }

    public NonNullList<Ingredient> getInputs() {
        return this.inputs;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        ItemStack input0 = pContainer.getItem(0);
        ItemStack input1 = pContainer.getItem(1);
        ItemStack input2 = pContainer.getItem(2);

        return matches(input0, input1, input2);
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        ItemStack input0 = pContainer.getItem(0);
        ItemStack input1 = pContainer.getItem(1);
        ItemStack input2 = pContainer.getItem(2);

        return matches(input0, input1, input2) ? this.output.copy() : ItemStack.EMPTY;
    }

    // check if it matches ignoring order
    // if the recipe has an empty ingredient, then the matching will allow for any item
    public boolean matches(ItemStack... inputs) {
        List<Boolean> matched = new ArrayList<>(inputs.length);

        for (int i = 0; i < inputs.length; i++) {
            matched.add(false);
        }

        for (Ingredient recipeIngredient : this.inputs) {
            for (int inputIndex = 0; inputIndex < inputs.length; inputIndex++) {
                ItemStack input = inputs[inputIndex];

                if (!matched.get(inputIndex) && recipeIngredient.test(input)) {
                    matched.set(inputIndex, true);
                    break; // Move to the next recipe ingredient
                }
            }
        }

        // Check if all ingredients were matched
        return matched.stream().allMatch(Boolean::valueOf);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 4;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public void assemble(BlenderBlockEntity blenderBlockEntity) {
        ItemStack input0 = blenderBlockEntity.getInput0().getStackInSlot(0);
        ItemStack input1 = blenderBlockEntity.getInput1().getStackInSlot(0);
        ItemStack input2 = blenderBlockEntity.getInput2().getStackInSlot(0);

        if (matches(input0, input1, input2)) {
            blenderBlockEntity.getOutput().insertItem(0, this.output.copy(), false);

            // Remove the items from the inputs that were used
            for (Ingredient input : this.inputs) {
                if(input.test(input0))
                    blenderBlockEntity.getInput0().extractItem(0, 1, false);
                else if(input.test(input1))
                    blenderBlockEntity.getInput1().extractItem(0, 1, false);
                else if(input.test(input2))
                    blenderBlockEntity.getInput2().extractItem(0, 1, false);
            }
        }
    }

    public static class Serializer implements RecipeSerializer<BlenderRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private static final Codec<BlenderRecipe> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.listOf().fieldOf("inputs")
                                .forGetter(recipe -> recipe.inputs),
                        ItemStack.CODEC.fieldOf("output")
                                .forGetter(recipe -> recipe.output),
                        Codec.INT.fieldOf("process_time")
                                .forGetter(recipe -> recipe.processTime)
                ).apply(instance, BlenderRecipe::new));

        @Override
        public Codec<BlenderRecipe> codec() {
            return CODEC;
        }

        @Override
        public @Nullable BlenderRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            int inputCount = pBuffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(3, Ingredient.EMPTY);
            for (int index = 0; index < inputCount; index++) {
                inputs.add(Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            int processTime = pBuffer.readInt();
            return new BlenderRecipe(inputs, output, processTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BlenderRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputs.size());
            for (Ingredient input : pRecipe.inputs) {
                input.toNetwork(pBuffer);
            }

            pBuffer.writeItem(pRecipe.output);
            pBuffer.writeInt(pRecipe.processTime);
        }
    }

    public static class Type implements RecipeType<BlenderRecipe> {
        public static final Type INSTANCE = new Type();

        private static final String ID = new ResourceLocation(TurtyFoodMod.MOD_ID, "blender").toString();

        @Override
        public String toString() {
            return ID;
        }
    }
}
