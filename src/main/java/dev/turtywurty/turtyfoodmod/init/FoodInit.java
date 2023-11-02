package dev.turtywurty.turtyfoodmod.init;

import net.minecraft.world.food.FoodProperties;

public class FoodInit {
    protected static final FoodProperties BURGER = new FoodProperties.Builder()
            .nutrition(14)
            .saturationMod(0.85F)
            .build();

    protected static final FoodProperties TOMATO = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.2F)
            .fast()
            .build();

    protected static final FoodProperties LETTUCE = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.6F)
            .build();
}
