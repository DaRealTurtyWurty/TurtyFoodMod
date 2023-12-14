package dev.turtywurty.turtyfoodmod.init;

import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import dev.turtywurty.turtyfoodmod.menu.BlenderMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, TurtyFoodMod.MOD_ID);

    public static final RegistryObject<MenuType<BlenderMenu>> BLENDER =
            MENU_TYPES.register("blender", () -> IForgeMenuType.create(BlenderMenu::new));
}
