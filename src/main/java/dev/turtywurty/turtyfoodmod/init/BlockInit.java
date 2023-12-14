package dev.turtywurty.turtyfoodmod.init;

import dev.turtywurty.turtyfoodmod.TurtyFoodMod;
import dev.turtywurty.turtyfoodmod.block.BlenderBlock;
import dev.turtywurty.turtyfoodmod.block.CreativeEnergyGeneratorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TurtyFoodMod.MOD_ID);

    public static final RegistryObject<BlenderBlock> BLENDER =
            BLOCKS.register("blender", () -> new BlenderBlock(
                    BlockBehaviour.Properties.of()
                            .strength(1.5F, 6.0F)
                            .requiresCorrectToolForDrops()
                            .lightLevel((state) -> state.getValue(BlenderBlock.RUNNING) ? 3 : 0)
                            .noOcclusion()
                            .mapColor(MapColor.COLOR_BLACK)
                            .sound(SoundType.GLASS)));

    public static final RegistryObject<CreativeEnergyGeneratorBlock> CREATIVE_ENERGY_GENERATOR =
            BLOCKS.register("creative_energy_generator", () -> new CreativeEnergyGeneratorBlock(
                    BlockBehaviour.Properties.of()
                            .strength(3F, 15.0F)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .mapColor(MapColor.COLOR_GRAY)
                            .sound(SoundType.METAL)));
}
