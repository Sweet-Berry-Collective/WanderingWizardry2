package dev.sweetberry.wwizardry.client.render;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;

public class AnimationHelper {
    public static void applyKeyframes(Keyframe[] frames, double time, ModelPart part) {
        var tuple = applyKeyframes(frames, time);
        var angle = tuple.getB();
        var pos = tuple.getA();

        part.xRot += (float)angle.x;
        part.yRot += (float)angle.y;
        part.zRot += (float)angle.z;
        part.y += (float)pos.y;
        part.z += (float)pos.z;
        part.x += (float)pos.x;
    }

    public static Tuple<Vec3, Vec3> applyKeyframes(Keyframe[] frames, double time) {
        if (frames.length == 0)
            return new Tuple<>(new Vec3(0, 0, 0), new Vec3(0, 0, 0));
        if (frames.length == 1)
            return new Tuple<>(frames[0].posModifier(), frames[0].posModifier());
        var lastIdx = Math.max(0, Mth.binarySearch(0, frames.length, it -> time <= frames[it].endTime()) - 1);
        var last = frames[lastIdx];
        var nextIdx = Math.min(frames.length - 1, lastIdx + 1);
        var next = frames[nextIdx];
        var timeOffset = (time - last.endTime()) / (next.endTime() - last.endTime());
        return next.apply(last, timeOffset);
    }
}
