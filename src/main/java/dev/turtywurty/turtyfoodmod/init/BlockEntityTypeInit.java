package dev.turtywurty.turtyfoodmod.init;

import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import dev.turtywurty.turtyfoodmod.block.entity.BlenderBlockEntity;
import dev.turtywurty.turtyfoodmod.block.entity.CreativeEnergyGeneratorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityTypeInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TurtyFoodMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<BlenderBlockEntity>> BLENDER =
            BLOCK_ENTITY_TYPES.register("blender",
                    () -> BlockEntityType.Builder.of(BlenderBlockEntity::new, BlockInit.BLENDER.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<CreativeEnergyGeneratorBlockEntity>> CREATIVE_ENERGY_GENERATOR =
            BLOCK_ENTITY_TYPES.register("creative_energy_generator",
                    () -> BlockEntityType.Builder.of(CreativeEnergyGeneratorBlockEntity::new, BlockInit.CREATIVE_ENERGY_GENERATOR.get())
                            .build(null));
}
