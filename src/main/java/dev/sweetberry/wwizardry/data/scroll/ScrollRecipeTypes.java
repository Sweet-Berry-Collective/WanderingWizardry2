package dev.sweetberry.wwizardry.data.scroll;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.registry.RegistryContext;
import dev.sweetberry.wwizardry.registry.WanderingWizardryRegistries;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ScrollRecipeTypes {
    private static final RegistryContext<ScrollRecipeType<?>> SCROLL_RECIPE_TYPES = new RegistryContext<>(WanderingWizardryRegistries.SCROLL_RECIPE_TYPE, WanderingWizardry.MOD_ID);

    public static final RegistryContext.Value<ScrollRecipeType<DirectScrollRecipe>> DIRECT = SCROLL_RECIPE_TYPES.defer("direct", withCodec(DirectScrollRecipe.CODEC));

    public static void register() {
        SCROLL_RECIPE_TYPES.register();
    }

    private static <T extends ScrollRecipe> Function<ResourceKey<ScrollRecipeType<T>>, ScrollRecipeType<T>> withCodec(@NotNull MapCodec<T> codec) {
        return key -> new ScrollRecipeType<>(key, codec);
    }
}
