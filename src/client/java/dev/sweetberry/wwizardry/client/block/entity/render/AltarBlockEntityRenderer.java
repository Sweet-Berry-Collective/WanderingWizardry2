package dev.sweetberry.wwizardry.client.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.block.entity.AltarBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class AltarBlockEntityRenderer implements BlockEntityRenderer<AltarBlockEntity, AltarBlockEntityRenderState> {
    public static final int CYCLE_TICKS = 20;
    private final ItemModelResolver itemModelResolver;

    public AltarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public @NonNull AltarBlockEntityRenderState createRenderState() {
        return new AltarBlockEntityRenderState();
    }

    private Pair<ItemStackRenderState, Boolean> mapStack(Pair<Ingredient, ItemStack> pair, Level level, int tickCount) {
        var state = new ItemStackRenderState();

        if (!pair.getSecond().isEmpty()) {
            itemModelResolver.updateForTopItem(state, pair.getSecond(), ItemDisplayContext.GROUND, null, null, 0);
        } else {
            var items = pair.getFirst().items().toList();

            int cycle = (tickCount / CYCLE_TICKS) % items.size();

            var item = items.get(cycle).value().getDefaultInstance();

            itemModelResolver.updateForTopItem(state, items.get(cycle).value().getDefaultInstance(), ItemDisplayContext.GROUND, level, null, 0);
        }

        return Pair.of(state, !pair.getSecond().isEmpty());
    }

    @Override
    public void extractRenderState(
            @NonNull AltarBlockEntity altarBlockEntity,
            @NonNull AltarBlockEntityRenderState state,
            float partialTicks,
            @NonNull Vec3 cameraPosition,
            ModelFeatureRenderer.CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(altarBlockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.ingredients = altarBlockEntity.getIngredients().stream().map(it -> mapStack(it, altarBlockEntity.getLevel(), 0)).toList();
    }

    @Override
    public void submit(
            @NonNull AltarBlockEntityRenderState state,
            @NonNull PoseStack poseStack,
            @NonNull SubmitNodeCollector submitNodeCollector,
            @NonNull CameraRenderState camera
    ) {
        int i = 0;
        for (var ingredient : state.ingredients) {
            var model = ingredient.getFirst();
            boolean solid = ingredient.getSecond();

            poseStack.pushPose();
            poseStack.translate(0.5, i + 1.25, 0.5);

            model.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.pack(15, 3), 0);

            poseStack.popPose();

            i += 1;
        }
    }
}
