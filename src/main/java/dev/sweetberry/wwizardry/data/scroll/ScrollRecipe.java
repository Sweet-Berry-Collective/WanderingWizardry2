package dev.sweetberry.wwizardry.data.scroll;

import com.mojang.serialization.Codec;
import dev.sweetberry.wwizardry.registry.WanderingWizardryRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ScrollRecipe {
    Codec<ScrollRecipe> CODEC = WanderingWizardryRegistries.SCROLL_RECIPE_TYPE.byNameCodec().dispatch(ScrollRecipe::type, ScrollRecipeType::codec);

    int ticksToCraft(Level level);
    int sculkCharge(Level level);
    List<Ingredient> ingredients(Level level);
    ItemStack output(Level level);

    ScrollRecipeType<? extends ScrollRecipe> type();
}
