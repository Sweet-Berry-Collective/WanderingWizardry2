package dev.sweetberry.wwizardry.client.block.entity.render;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.render.AnimationHelper;
import dev.sweetberry.wwizardry.client.render.EasingFunction;
import dev.sweetberry.wwizardry.client.render.Keyframe;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class AltarBlockEntityModel extends Model<AltarBlockEntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WanderingWizardry.createId("altar"), "main");
    private final ModelPart layer1;
    private final ModelPart layer2;

    public static final Keyframe[] LAYER_1_FRAMES = new Keyframe[] {
            new Keyframe(EasingFunction::linear, 0, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
            new Keyframe(EasingFunction::linear, 0.75, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
            new Keyframe(EasingFunction::inOutBack, 1.5, new Vec3(0, 0, 0), new Vec3(0, -90 * Mth.DEG_TO_RAD, 0)),
            new Keyframe(EasingFunction::linear, 2.75, new Vec3(0, 0, 0), new Vec3(0, -90 * Mth.DEG_TO_RAD, 0)),
            new Keyframe(EasingFunction::inOutBack, 3.5, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
    };
    public static final Keyframe[] LAYER_2_FRAMES = new Keyframe[] {
            new Keyframe(EasingFunction::linear, 0, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
            new Keyframe(EasingFunction::linear, 4, new Vec3(0, 0, 0), new Vec3(0, -360 * Mth.DEG_TO_RAD, 0))
    };
    public static final Keyframe[] LAYER_2_FRAMES_FAST = new Keyframe[] {
            new Keyframe(EasingFunction::linear, 0, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
            new Keyframe(EasingFunction::linear, 4, new Vec3(0, 0, 0), new Vec3(0, -720 * Mth.DEG_TO_RAD, 0))
    };

    public AltarBlockEntityModel(ModelPart root) {
        super(root, RenderTypes::entityTranslucentEmissive);

        layer1 = root.getChild("layer1");
        layer2 = root.getChild("layer2");
    }

    public static LayerDefinition createBodyLayer() {
        var meshdefinition = new MeshDefinition();
        var partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("layer1", CubeListBuilder.create().texOffs(-80, 80).addBox(-40.0F, 0.0F, -40.0F, 80.0F, 0.1F, 80.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 20.0F, 8.0F));

        partdefinition.addOrReplaceChild("layer2", CubeListBuilder.create().texOffs(-80, 0).addBox(-40.0F, 0.0F, -40.0F, 80.0F, 0.1F, 80.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 20.1F, 8.0F));

        return LayerDefinition.create(meshdefinition, 80, 240);
    }

    @Override
    public void setupAnim(@NonNull AltarBlockEntityRenderState state) {
        super.setupAnim(state);

        var seconds = state.gameTime + state.partialTicks;
        seconds /= 20;
        seconds %= 4;

        AnimationHelper.applyKeyframes(LAYER_1_FRAMES, seconds, layer1);

        AnimationHelper.applyKeyframes(LAYER_2_FRAMES, seconds, layer2);
    }
}
