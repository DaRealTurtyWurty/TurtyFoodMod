package dev.turtywurty.turtyfoodmod.init;

import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TurtyFoodMod.MOD_ID);

    public static final RegistryObject<Item> BURGER = ITEMS.register("burger",
            () -> new Item(new Item.Properties().food(FoodInit.BURGER)));

    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",
            () -> new Item(new Item.Properties().food(FoodInit.TOMATO)));

    public static final RegistryObject<Item> LETTUCE = ITEMS.register("lettuce",
            () -> new Item(new Item.Properties().food(FoodInit.LETTUCE)));

    public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese",
            () -> new Item(new Item.Properties().food(FoodInit.CHEESE)));

    public static final RegistryObject<Item> TOMATO_SLICE = ITEMS.register("tomato_slice",
            () -> new Item(new Item.Properties().food(FoodInit.TOMATO_SLICE)));
}