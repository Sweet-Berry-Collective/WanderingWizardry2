package dev.sweetberry.wwizardry.block;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.block.altar.CenterAltarBlock;
import dev.sweetberry.wwizardry.block.altar.OuterAltarBlock;
import dev.sweetberry.wwizardry.block.entity.AltarBlockEntity;
import dev.sweetberry.wwizardry.registry.RegistryContext;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

public class WanderingWizardryBlocks {
    public static final BooleanProperty SCROLL = BooleanProperty.create("scroll");

    private static final RegistryContext<Block> BLOCKS = new RegistryContext<>(BuiltInRegistries.BLOCK, WanderingWizardry.MOD_ID);
    private static final RegistryContext<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new RegistryContext<>(BuiltInRegistries.BLOCK_ENTITY_TYPE, WanderingWizardry.MOD_ID);

    public static final RegistryContext.Value<CenterAltarBlock> CENTER_ALTAR = BLOCKS.defer("center_altar", withProperties(properties -> new CenterAltarBlock(
            properties
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(1.25F, 4.2F)
                    .sound(SoundType.BASALT)
    )));

    public static final RegistryContext.Value<OuterAltarBlock> OUTER_ALTAR = BLOCKS.defer("outer_altar", withProperties(properties -> new OuterAltarBlock(
            properties
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(1.25F, 4.2F)
                    .sound(SoundType.BASALT)
    )));

    public static final RegistryContext.Value<BlockEntityType<AltarBlockEntity>> ALTAR_ENTITY = BLOCK_ENTITY_TYPES.defer("altar", _ -> FabricBlockEntityTypeBuilder
            .create(AltarBlockEntity::new, CENTER_ALTAR.get())
            .build()
    );

    public static void register() {
        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Block> Function<ResourceKey<T>, T> withProperties(Function<BlockBehaviour.Properties, T> callback) {
        return key -> callback.apply(BlockBehaviour.Properties.of().setId((ResourceKey<Block>) key));
    }
}
