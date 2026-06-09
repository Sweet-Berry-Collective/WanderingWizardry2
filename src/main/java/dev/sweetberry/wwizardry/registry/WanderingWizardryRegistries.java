package dev.sweetberry.wwizardry.registry;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.data.scroll.ScrollRecipeType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class WanderingWizardryRegistries {
    public static final ResourceKey<Registry<ScrollRecipeType<?>>> SCROLL_RECIPE_TYPE_KEY = createRegistryKey("scroll_recipe_type");

    public static final Registry<ScrollRecipeType<?>> SCROLL_RECIPE_TYPE = FabricRegistryBuilder
            .create(SCROLL_RECIPE_TYPE_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static <T> ResourceKey<Registry<T>> createRegistryKey(String path) {
        return ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(WanderingWizardry.MOD_ID, path));
    }

    public static <T> ResourceKey<T> createKey(ResourceKey<Registry<T>> registry, String path) {
        return ResourceKey.create(registry, Identifier.fromNamespaceAndPath(WanderingWizardry.MOD_ID, path));
    }
}
