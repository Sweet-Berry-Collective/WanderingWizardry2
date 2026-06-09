package dev.sweetberry.wwizardry.block.altar;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AltarBlock extends Block {
    public static final VoxelShape ALTAR_BASE_SHAPE = Shapes.or(
            Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
            Block.box(4.0, 2.0, 4.0, 12.0, 15.0, 12.0)
    ).optimize();

    public AltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected abstract MapCodec<? extends Block> codec();
}
