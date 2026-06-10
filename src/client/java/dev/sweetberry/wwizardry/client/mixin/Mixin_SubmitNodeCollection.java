package dev.sweetberry.wwizardry.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.client.duck.Duck_SubmitNode;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.OptionalInt;

@Mixin(SubmitNodeCollection.class)
public class Mixin_SubmitNodeCollection implements Duck_SubmitNode {
    @Unique
    OptionalInt wandering_wizardry$translucency = OptionalInt.empty();

    @WrapMethod(method = "submitItem")
    void wrapItem(PoseStack poseStack, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, ItemStackRenderState.FoilType foilType, Operation<Void> original) {
        if (wandering_wizardry$translucency.isEmpty()) {
            original.call(poseStack, displayContext, lightCoords, overlayCoords, outlineColor, tintLayers, quads, foilType);
            return;
        }

        var newTintLayers = new int[tintLayers.length + 1];

        var tint = wandering_wizardry$translucency.getAsInt() << 24;

        for (int i = 0; i < tintLayers.length; i++) {
            int value = tintLayers[i] & 0xffffff | tint;
            newTintLayers[i] = value;
        }

        newTintLayers[tintLayers.length] = tint | 0xffffff;

        var newQuads = quads.stream().map(it -> {
            var info = it.materialInfo();

            var type = info.itemRenderType();

            if (type == Sheets.cutoutItemSheet()) {
                type = Sheets.translucentItemSheet();
            }

            if (type == Sheets.cutoutBlockItemSheet()) {
                type = Sheets.translucentBlockItemSheet();
            }

            int index = info.tintIndex();
            if (index == -1) {
                index = tintLayers.length;
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
                            index,
                            info.shade(),
                            info.lightEmission()
                    )
            );
        }).toList();

        original.call(poseStack, displayContext, lightCoords, overlayCoords, outlineColor, newTintLayers, newQuads, foilType);
    }

    @Override
    public void wandering_wizardry$setTranslucency(OptionalInt tint) {
        wandering_wizardry$translucency = tint;
    }
}
