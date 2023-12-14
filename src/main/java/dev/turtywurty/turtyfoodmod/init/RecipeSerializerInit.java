package dev.turtywurty.turtyfoodmod.init;

import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import dev.turtywurty.turtyfoodmod.recipe.BlenderRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializerInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TurtyFoodMod.MOD_ID);

    public static final RegistryObject<BlenderRecipe.Serializer> BLENDER =
            RECIPE_SERIALIZERS.register("blender", () -> BlenderRecipe.Serializer.INSTANCE);
}
