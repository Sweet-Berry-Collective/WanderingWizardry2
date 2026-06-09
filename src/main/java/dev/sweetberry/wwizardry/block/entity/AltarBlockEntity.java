package dev.sweetberry.wwizardry.block.entity;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PairCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.sweetberry.wwizardry.block.WanderingWizardryBlocks;
import dev.sweetberry.wwizardry.data.WanderingWizardryComponents;
import dev.sweetberry.wwizardry.data.scroll.ScrollRecipe;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class AltarBlockEntity extends BlockEntity {
    private static final String SCROLL_STACK = "scroll_stack";
    private static final String INGREDIENTS = "ingredients";

    public static final Codec<List<Pair<Ingredient, ItemStack>>> INGREDIENT_ITEM_STACK_CODEC_LIST = RecordCodecBuilder.<Pair<Ingredient, ItemStack>>create(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(Pair::getFirst),
            ItemStack.OPTIONAL_CODEC.fieldOf("stack").forGetter(Pair::getSecond)
    ).apply(inst, Pair::of)).listOf();

    // I hate hate HATE that entities don't use data components yet.
    // Mojang pls fix
    private ItemStack scrollStack = ItemStack.EMPTY;
    private ArrayList<Pair<Ingredient, ItemStack>> ingredients = new ArrayList<>();

    public AltarBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(WanderingWizardryBlocks.ALTAR_ENTITY.get(), worldPosition, blockState);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        output.store(SCROLL_STACK, ItemStack.OPTIONAL_CODEC, this.scrollStack);
        output.store(INGREDIENTS, INGREDIENT_ITEM_STACK_CODEC_LIST, this.ingredients);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        this.scrollStack = input.read(SCROLL_STACK, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);

        var ingredients = input.read(INGREDIENTS, INGREDIENT_ITEM_STACK_CODEC_LIST);

        if (ingredients.isPresent()) {
            this.ingredients = new ArrayList<>(ingredients.get());
        } else {
            updateScrollStack();
        }
    }

    void updateScrollStack() {
        var maybeRecipe = getScrollRecipe();
        if (maybeRecipe.isEmpty()) {
            this.ingredients = new ArrayList<>();

            setChanged();

            if (level == null) {
                return;
            }

            level.setBlock(this.getBlockPos(), this.getBlockState().setValue(WanderingWizardryBlocks.SCROLL, false), Block.UPDATE_ALL);

            return;
        }

        var recipe = maybeRecipe.get();

        var ingredients = recipe.ingredients(this.level);

        this.ingredients = new ArrayList<>(ingredients.stream().map(it -> Pair.of(it, ItemStack.EMPTY)).toList());

        setChanged();

        if (level == null) {
            return;
        }

        level.setBlock(this.getBlockPos(), this.getBlockState().setValue(WanderingWizardryBlocks.SCROLL, true), Block.UPDATE_ALL);
    }

    public boolean tryInsertStack(ItemStack stack) {
        if (stack.has(WanderingWizardryComponents.SCROLL_RECIPE.get()) && this.scrollStack.isEmpty()) {
            this.scrollStack = stack.split(1);

            updateScrollStack();

            setChanged();

            return true;
        }

        var first = IntStream.range(0, this.ingredients.size())
                .filter(i -> this.ingredients.get(i).getSecond().isEmpty())
                .filter(i -> this.ingredients.get(i).getFirst().test(stack))
                .findFirst();

        if (first.isPresent()) {
            var index = first.getAsInt();

            var ingredient = this.ingredients.get(index);

            this.ingredients.set(index, Pair.of(ingredient.getFirst(), stack.split(1)));

            setChanged();

            return true;
        }

        return false;
    }

    public boolean tryRemoveStack(Player player) {
        var first = IntStream.range(0, this.ingredients.size())
                .filter(i -> !this.ingredients.get(i).getSecond().isEmpty())
                .findFirst();

        if (first.isPresent()) {
            var index = first.getAsInt();

            var ingredient = this.ingredients.get(index);

            var stack = ingredient.getSecond();

            if (!player.addItem(stack)) {
                return false;
            }

            this.ingredients.set(index, new Pair<>(ingredient.getFirst(), ItemStack.EMPTY));

            setChanged();

            return true;
        }

        if (!this.scrollStack.isEmpty()) {
            if (!player.addItem(this.scrollStack)) {
                return false;
            }

            this.scrollStack = ItemStack.EMPTY;

            updateScrollStack();

            setChanged();

            return true;
        }

        return false;
    }

    public List<Pair<Ingredient, ItemStack>> getIngredients() {
        return this.ingredients;
    }

    public Optional<ScrollRecipe> getScrollRecipe() {
        return Optional.ofNullable(this.scrollStack.get(WanderingWizardryComponents.SCROLL_RECIPE.get()));
    }
}
