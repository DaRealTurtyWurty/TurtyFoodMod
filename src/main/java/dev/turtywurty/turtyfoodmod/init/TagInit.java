package dev.turtywurty.turtyfoodmod.init;

import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class TagInit {
    public static class Items {
        public static final TagKey<Item> BLENDABLE = create("blendable");

        private static TagKey<Item> create(@NotNull String id) {
            return ItemTags.create(new ResourceLocation(TurtyFoodMod.MOD_ID, id));
        }
    }
}
