/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.wwizardry.client;

import dev.sweetberry.wwizardry.block.WanderingWizardryBlocks;
import dev.sweetberry.wwizardry.client.block.entity.render.AltarBlockEntityModel;
import dev.sweetberry.wwizardry.client.block.entity.render.AltarBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class WanderingWizardryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRenderers.register(
				WanderingWizardryBlocks.ALTAR_ENTITY.get(),
				AltarBlockEntityRenderer::new
		);

		ModelLayerRegistry.registerModelLayer(AltarBlockEntityModel.LAYER_LOCATION, AltarBlockEntityModel::createBodyLayer);
	}
}
