package dev.sweetberry.wwizardry.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.duck.Duck_SubmitNode;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(SubmitNodeCollection.class)
public class Mixin_SubmitNodeCollection implements Duck_SubmitNode {
    @Unique
    int wandering_wizardry$tint = 0xffffffff;

    @WrapMethod(method = "submitItem")
    void wrapItem(PoseStack poseStack, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, ItemStackRenderState.FoilType foilType, Operation<Void> original) {
        var newTintLayers = new int[tintLayers.length + 1];
        System.arraycopy(tintLayers, 0, newTintLayers, 0, tintLayers.length);

        newTintLayers[tintLayers.length] = wandering_wizardry$tint;

        var newQuads = quads.stream().map(it -> {
            var info = it.materialInfo();

            var type = info.itemRenderType();

            if (type == Sheets.cutoutItemSheet()) {
                type = Sheets.translucentItemSheet();
            }

            if (type == Sheets.cutoutBlockItemSheet()) {
                type = Sheets.translucentBlockItemSheet();
            }

            return new BakedQuad(
                    it.position0(),
                    it.position1(),
                    it.position2(),
                    it.position3(),
                    it.packedUV0(),
                    it.packedUV1(),
                    it.packedUV2(),
                    it.packedUV3(),
                    it.direction(),
                    new BakedQuad.MaterialInfo(
                            info.sprite(),
                            ChunkSectionLayer.TRANSLUCENT,
                            type,
                            tintLayers.length,
                            info.shade(),
                            info.lightEmission()
                    )
            );
        }).toList();

        original.call(poseStack, displayContext, lightCoords, overlayCoords, outlineColor, newTintLayers, newQuads, foilType);
    }

    @Override
    public void wandering_wizardry$setTint(int tint) {
        wandering_wizardry$tint = tint;
    }
}
