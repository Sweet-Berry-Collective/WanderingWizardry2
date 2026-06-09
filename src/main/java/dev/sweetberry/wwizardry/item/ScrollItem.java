package dev.sweetberry.wwizardry.item;

import dev.sweetberry.wwizardry.data.WanderingWizardryComponents;
import dev.sweetberry.wwizardry.data.scroll.DirectScrollRecipe;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ScrollItem extends Item {
    public ScrollItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (stack.has(WanderingWizardryComponents.SCROLL_RECIPE.get())) {
            return InteractionResult.PASS;
        }

        stack.set(WanderingWizardryComponents.SCROLL_RECIPE.get(), new DirectScrollRecipe(0, 0, List.of(Ingredient.of(Items.DIRT)), Items.DIRT.getDefaultInstance()));

        return InteractionResult.SUCCESS;
    }
}
