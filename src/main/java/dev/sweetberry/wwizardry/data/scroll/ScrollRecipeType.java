package dev.sweetberry.wwizardry.data.scroll;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceKey;

public record ScrollRecipeType<T extends ScrollRecipe>(
        ResourceKey<ScrollRecipeType<T>> resourceKey,
        MapCodec<T> codec
) {}
