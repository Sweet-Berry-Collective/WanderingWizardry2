package dev.sweetberry.wwizardry.client.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.block.entity.AltarBlockEntity;
import dev.sweetberry.wwizardry.client.duck.Duck_SubmitNode;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.jspecify.annotations.NonNull;

import java.util.OptionalInt;

public class AltarBlockEntityRenderer implements BlockEntityRenderer<AltarBlockEntity, AltarBlockEntityRenderState> {
    public static final Identifier TEXTURE = WanderingWizardry.createId("textures/entity/altar.png");

    public static final int CYCLE_TICKS = 20;
    public static final int FULL_ROTATION_TICKS = 160;
    public static final int LOCAL_ROTATION_TICKS = 80;
    public static final int BOB_TICKS = 120;
    public static final float BOB_MAGNITUDE = 0.125f;
    private final ItemModelResolver itemModelResolver;
    private final AltarBlockEntityModel model;

    public AltarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.model = new AltarBlockEntityModel(context.bakeLayer(AltarBlockEntityModel.LAYER_LOCATION));
    }

    @Override
    public @NonNull AltarBlockEntityRenderState createRenderState() {
        return new AltarBlockEntityRenderState();
    }

    private Pair<ItemStackRenderState, Boolean> mapStack(Pair<Ingredient, ItemStack> pair, Level level, long tickCount) {
        var state = new ItemStackRenderState();

        if (!pair.getSecond().isEmpty()) {
            itemModelResolver.updateForTopItem(state, pair.getSecond(), ItemDisplayContext.GROUND, null, null, 0);
        } else {
            var items = pair.getFirst().items().toList();

            long cycle = (tickCount / CYCLE_TICKS) % items.size();

            var item = items.get((int) cycle).value().getDefaultInstance();

            itemModelResolver.updateForTopItem(state, item, ItemDisplayContext.GROUND, level, null, 0);
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

        var level = altarBlockEntity.getLevel();

        long tickCount;

        if (level != null) {
            tickCount = level.getGameTime();
        } else {
            tickCount = 0;
        }

        itemModelResolver.updateForTopItem(state.result, altarBlockEntity.resultStack(), ItemDisplayContext.GROUND, altarBlockEntity.getLevel(), null, 0);

        state.ingredients = altarBlockEntity.getIngredients().stream().map(it -> mapStack(it, level, tickCount)).toList();
        state.gameTime = tickCount;
        state.partialTicks = partialTicks;
    }

    @Override
    public void submit(
            @NonNull AltarBlockEntityRenderState state,
            @NonNull PoseStack poseStack,
            @NonNull SubmitNodeCollector submitNodeCollector,
            @NonNull CameraRenderState camera
    ) {
        float full_rotation = Mth.TWO_PI * (((state.gameTime % FULL_ROTATION_TICKS) + state.partialTicks) / (float) FULL_ROTATION_TICKS);
        float local_rotation = Mth.TWO_PI * (((state.gameTime % LOCAL_ROTATION_TICKS) + state.partialTicks) / (float) LOCAL_ROTATION_TICKS);
        float bob_time = Mth.TWO_PI * (((state.gameTime % BOB_TICKS) + state.partialTicks) / (float) BOB_TICKS);

        poseStack.pushPose();

        poseStack.translate(0.5, 1.25, 0.5);

        float radiansPerItem = Mth.TWO_PI / state.ingredients.size();

        for (int i = 0; i < state.ingredients.size(); i++) {
            var ingredient = state.ingredients.get(i);

            var model = ingredient.getFirst();
            boolean solid = ingredient.getSecond();

            poseStack.pushPose();

            float randomOffset = Mth.TWO_PI * (((long) Integer.hashCode(i) - Integer.MIN_VALUE) / (float) ((long) Integer.MAX_VALUE - Integer.MIN_VALUE));

            float bob_height = (float) Math.sin(randomOffset + bob_time) * BOB_MAGNITUDE;

            poseStack.rotateAround(new Quaternionf(new AxisAngle4f(i * radiansPerItem + full_rotation, 0, 1, 0)), 0, 0, 0);
            poseStack.translate(2, bob_height, 0);
            poseStack.rotateAround(new Quaternionf(new AxisAngle4f(local_rotation + randomOffset, 0, -1, 0)), 0, 0, 0);

            if (submitNodeCollector instanceof Duck_SubmitNode duck) {
                if (solid) {
                    duck.wandering_wizardry$setTranslucency(OptionalInt.empty());
                } else {
                    duck.wandering_wizardry$setTranslucency(OptionalInt.of(0x80));
                }
            }

            model.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

            poseStack.popPose();
        }

        if (submitNodeCollector instanceof Duck_SubmitNode duck) {
            duck.wandering_wizardry$setTranslucency(OptionalInt.of(0x80));
        }

        float bob_height = (float) Math.sin(bob_time) * BOB_MAGNITUDE;

        poseStack.rotateAround(new Quaternionf(new AxisAngle4f(local_rotation, 0, 1, 0)), 0, 0, 0);
        poseStack.translate(0, bob_height + 0.25, 0);

        state.result.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        if (submitNodeCollector instanceof Duck_SubmitNode duck) {
            duck.wandering_wizardry$setTranslucency(OptionalInt.empty());
        }

        poseStack.popPose();

        submitNodeCollector.submitModel(model, state, poseStack, model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, 0x40FFFFFF, null, 0, null);
    }
}
