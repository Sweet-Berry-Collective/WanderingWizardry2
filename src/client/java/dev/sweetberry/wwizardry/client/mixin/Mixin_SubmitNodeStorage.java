package dev.sweetberry.wwizardry.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.sweetberry.wwizardry.client.duck.Duck_SubmitNode;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.OptionalInt;

@Mixin(SubmitNodeStorage.class)
public class Mixin_SubmitNodeStorage implements Duck_SubmitNode {
    @Shadow
    @Final
    private Int2ObjectAVLTreeMap<SubmitNodeCollection> submitsPerOrder;
    @Unique
    OptionalInt wandering_wizardry$translucency = OptionalInt.empty();

    @WrapMethod(method = "lambda$order$0")
    SubmitNodeCollection wrapCreateNode(int ignored, Operation<SubmitNodeCollection> original) {
        var value = original.call(ignored);

        if (!(value instanceof Duck_SubmitNode duck)) return value;

        duck.wandering_wizardry$setTranslucency(wandering_wizardry$translucency);

        return value;
    }

    @Override
    public void wandering_wizardry$setTranslucency(OptionalInt tint) {
        wandering_wizardry$translucency = tint;

        for (var value : this.submitsPerOrder.values()) {
            if (!(value instanceof Duck_SubmitNode duck)) continue;

            duck.wandering_wizardry$setTranslucency(wandering_wizardry$translucency);
        }
    }
}
