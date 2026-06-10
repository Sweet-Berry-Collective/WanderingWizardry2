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

@Mixin(SubmitNodeStorage.class)
public class Mixin_SubmitNodeStorage implements Duck_SubmitNode {
    @Shadow
    @Final
    private Int2ObjectAVLTreeMap<SubmitNodeCollection> submitsPerOrder;
    @Unique
    int wandering_wizardry$tint = 0xffffffff;

    @WrapMethod(method = "lambda$order$0")
    SubmitNodeCollection wrapCreateNode(int ignored, Operation<SubmitNodeCollection> original) {
        var value = original.call(ignored);

        if (!(value instanceof Duck_SubmitNode duck)) return value;

        duck.wandering_wizardry$setTint(wandering_wizardry$tint);

        return value;
    }

    @Override
    public void wandering_wizardry$setTint(int tint) {
        wandering_wizardry$tint = tint;

        for (var value : this.submitsPerOrder.values()) {
            if (!(value instanceof Duck_SubmitNode duck)) continue;

            duck.wandering_wizardry$setTint(wandering_wizardry$tint);
        }
    }
}
