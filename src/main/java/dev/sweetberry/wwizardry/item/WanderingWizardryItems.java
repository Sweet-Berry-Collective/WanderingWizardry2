package dev.sweetberry.wwizardry.item;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.block.WanderingWizardryBlocks;
import dev.sweetberry.wwizardry.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class WanderingWizardryItems {
    private static final RegistryContext<Item> ITEMS = new RegistryContext<>(BuiltInRegistries.ITEM, WanderingWizardry.MOD_ID);

    public static final RegistryContext.Value<BlockItem> ALTAR = ITEMS.defer("altar", withProperties(it -> new BlockItem(WanderingWizardryBlocks.CENTER_ALTAR.get(), it)));
    public static final RegistryContext.Value<ScrollItem> SCROLL = ITEMS.defer("scroll", withProperties(ScrollItem::new));

    public static void register() {
        ITEMS.register();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Item> Function<ResourceKey<T>, T> withProperties(Function<Item.Properties, T> callback) {
        return key -> callback.apply(new Item.Properties().setId((ResourceKey<Item>) key));
    }
}
