package dev.turtywurty.turtyfoodmod.init;

import net.minecraft.world.food.FoodProperties;

public class FoodInit {
    protected static final FoodProperties BURGER = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(0.6F)
            .build();
}
