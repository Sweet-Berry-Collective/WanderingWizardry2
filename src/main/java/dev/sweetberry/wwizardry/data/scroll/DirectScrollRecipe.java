package dev.sweetberry.wwizardry.data.scroll;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public record DirectScrollRecipe(int ticksToCraft, int sculkCharge, List<Ingredient> ingredients, ItemStack output) implements ScrollRecipe {
    public static final MapCodec<DirectScrollRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("ticks_to_craft").forGetter(DirectScrollRecipe::ticksToCraft),
            Codec.INT.fieldOf("sculk_charge").forGetter(DirectScrollRecipe::sculkCharge),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(DirectScrollRecipe::ingredients),
            ItemStack.CODEC.fieldOf("output").forGetter(DirectScrollRecipe::output)
    ).apply(inst, DirectScrollRecipe::new));

    @Override
    public int ticksToCraft(Level level) {
        return ticksToCraft();
    }

    @Override
    public int sculkCharge(Level level) {
        return sculkCharge();
    }

    @Override
    public List<Ingredient> ingredients(Level level) {
        return ingredients();
    }

    @Override
    public ItemStack output(Level level) {
        return output();
    }

    public ScrollRecipeType<DirectScrollRecipe> type() {
        return ScrollRecipeTypes.DIRECT.get();
    }
}
