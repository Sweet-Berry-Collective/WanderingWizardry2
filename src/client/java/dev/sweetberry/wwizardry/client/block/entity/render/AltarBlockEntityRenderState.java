package dev.sweetberry.wwizardry.client.block.entity.render;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

import java.util.List;

public class AltarBlockEntityRenderState extends BlockEntityRenderState {
    List<Pair<ItemStackRenderState, Boolean>> ingredients = List.of();
    float partialTicks = 0;
    long gameTime = 0;
}
