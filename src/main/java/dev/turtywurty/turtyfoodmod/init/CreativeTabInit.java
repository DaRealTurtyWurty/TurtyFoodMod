package dev.turtywurty.turtyfoodmod.init;

import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TurtyFoodMod.MOD_ID);

    private static final RegistryObject<CreativeModeTab> FOOD_TAB = CREATIVE_TABS.register(TurtyFoodMod.MOD_ID,
            () -> CreativeModeTab.builder()
                    .icon(ItemInit.BURGER.get()::getDefaultInstance)
                    .title(Component.translatable("itemGroup." + TurtyFoodMod.MOD_ID + ".food"))
                    .withSearchBar()
                    .displayItems((pParameters, pOutput) -> {
                        ItemInit.ITEMS.getEntries().stream()
                                .map(RegistryObject::get)
                                .forEach(pOutput::accept);
                    })
                    .build());
}
