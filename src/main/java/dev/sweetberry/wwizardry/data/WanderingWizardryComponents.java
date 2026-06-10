package dev.sweetberry.wwizardry.data;

import com.mojang.serialization.Codec;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.data.scroll.ScrollRecipe;
import dev.sweetberry.wwizardry.registry.RegistryContext;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;

import java.util.function.Function;

public class WanderingWizardryComponents {
    private static final RegistryContext<DataComponentType<?>> DATA_COMPONENT_TYPES = new RegistryContext<>(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            WanderingWizardry.MOD_ID
    );

    public static final RegistryContext.Value<DataComponentType<ScrollRecipe>> SCROLL_RECIPE = DATA_COMPONENT_TYPES.defer(
            "scroll_recipe",
            withBuilder(builder -> builder
                    .persistent(ScrollRecipe.CODEC)
                    .networkSynchronized(ByteBufCodecs.fromCodecWithRegistries(ScrollRecipe.CODEC))
                    .build()
            )
    );

    public static final RegistryContext.Value<DataComponentType<Integer>> GLOBAL_TINT = DATA_COMPONENT_TYPES.defer(
            "global_tint",
            withBuilder(builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .build()
            )
    );

    public static void register() {
        DATA_COMPONENT_TYPES.register();
    }

    private static <T> Function<ResourceKey<DataComponentType<T>>, DataComponentType<T>> withBuilder(
            Function<DataComponentType.Builder<T>, DataComponentType<T>> callback
    ) {
        return key -> callback.apply(DataComponentType.builder());
    }
}
