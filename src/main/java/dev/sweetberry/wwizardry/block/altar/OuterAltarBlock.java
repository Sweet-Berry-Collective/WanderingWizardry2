package dev.sweetberry.wwizardry.block.altar;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.wwizardry.block.WanderingWizardryBlocks;
import dev.sweetberry.wwizardry.item.WanderingWizardryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public class OuterAltarBlock extends AltarBlock {
    public static final VoxelShape NORTH_SHAPE = Shapes.or(
            AltarBlock.ALTAR_BASE_SHAPE,

            box(0.0, 11.0, 10.0, 16.0, 15.0, 16.0),
            box(0.0, 13.0, 5.0, 16.0, 17.0, 11.0),
            box(0.0, 15.0, 0.0, 16.0, 19.0, 6.0)
    ).optimize();
    public static final VoxelShape SOUTH_SHAPE = Shapes.or(
            AltarBlock.ALTAR_BASE_SHAPE,

            box(0.0, 11.0, 0.0, 16.0, 15.0, 6.0),
            box(0.0, 13.0, 5.0, 16.0, 17.0, 11.0),
            box(0.0, 15.0, 10.0, 16.0, 19.0, 16.0)
    ).optimize();
    public static final VoxelShape EAST_SHAPE = Shapes.or(
            AltarBlock.ALTAR_BASE_SHAPE,

            box(0.0, 11.0, 0.0, 6.0, 15.0, 16.0),
            box(5.0, 13.0, 0.0, 11.0, 17.0, 16.0),
            box(10.0, 15.0, 0.0, 16.0, 19.0, 16.0)
    ).optimize();
    public static final VoxelShape WEST_SHAPE = Shapes.or(
            AltarBlock.ALTAR_BASE_SHAPE,

            box(10.0, 11.0, 0.0, 16.0, 15.0, 16.0),
            box(5.0, 13.0, 0.0, 11.0, 17.0, 16.0),
            box(0.0, 15.0, 0.0, 6.0, 19.0, 16.0)
    ).optimize();

    public static final MapCodec<OuterAltarBlock> CODEC = simpleCodec(OuterAltarBlock::new);

    public OuterAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull MapCodec<OuterAltarBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public @NonNull Item asItem() {
        return WanderingWizardryItems.ALTAR.get();
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack itemStack, BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
        var direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        var centerPos = pos.relative(direction.getOpposite(), 2);

        var centerState = level.getBlockState(centerPos);

        if (centerState.is(WanderingWizardryBlocks.CENTER_ALTAR.get())) {
            return WanderingWizardryBlocks.CENTER_ALTAR.get().useItemOn(itemStack, centerState, level, centerPos, player, hand, hitResult);
        }

        return InteractionResult.PASS;
    }
}
