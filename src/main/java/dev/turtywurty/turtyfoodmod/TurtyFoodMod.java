package dev.turtywurty.turtyfoodmod;

import com.mojang.logging.LogUtils;
import dev.turtywurty.turtyfoodmod.init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TurtyFoodMod.MOD_ID)
public class TurtyFoodMod {
    public static final String MOD_ID = "turtyfoodmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TurtyFoodMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(bus);
        CreativeTabInit.CREATIVE_TABS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockEntityTypeInit.BLOCK_ENTITY_TYPES.register(bus);
        MenuTypeInit.MENU_TYPES.register(bus);
        RecipeSerializerInit.RECIPE_SERIALIZERS.register(bus);

        LOGGER.info("Turty Food Mod has been loaded!");
    }
}
