/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.wwizardry;

import dev.sweetberry.wwizardry.block.WanderingWizardryBlocks;
import dev.sweetberry.wwizardry.data.WanderingWizardryComponents;
import dev.sweetberry.wwizardry.data.scroll.ScrollRecipeTypes;
import dev.sweetberry.wwizardry.item.WanderingWizardryItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WanderingWizardry implements ModInitializer {
	public static final String MOD_ID = "wandering_wizardry";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("*tips altar* w'wizardry");

		WanderingWizardryBlocks.register();
		WanderingWizardryItems.register();
		ScrollRecipeTypes.register();
		WanderingWizardryComponents.register();
	}

	public static <T> ResourceKey<T> createKey(ResourceKey<Registry<T>> registry, String path) {
		return ResourceKey.create(registry, Identifier.fromNamespaceAndPath(MOD_ID, path));
	}
}
