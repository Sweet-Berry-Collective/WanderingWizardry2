package dev.sweetberry.wwizardry.block.altar;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.block.WanderingWizardryBlocks;
import dev.sweetberry.wwizardry.block.entity.AltarBlockEntity;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CenterAltarBlock extends AltarBlock implements EntityBlock {
    public static final VoxelShape SHAPE = Shapes.or(
            AltarBlock.ALTAR_BASE_SHAPE,

            box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
            box(3.0, 16.0, 3.0, 13.0, 17.0, 13.0)
    ).optimize();

    public static final MapCodec<CenterAltarBlock> CODEC = simpleCodec(CenterAltarBlock::new);

    public CenterAltarBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.getStateDefinition().any().setValue(WanderingWizardryBlocks.SCROLL, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NonNull Builder<Block, BlockState> builder) {
        builder.add(WanderingWizardryBlocks.SCROLL);
    }

    @Override
    protected @NonNull MapCodec<CenterAltarBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
        return new AltarBlockEntity(worldPosition, blockState);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        var pos = context.getClickedPos();

        for (var it = Direction.Plane.HORIZONTAL.stream().iterator(); it.hasNext(); ) {
            var direction = it.next();

            var relative = pos.relative(direction, 2);

            var state = context.getLevel().getBlockState(relative);

            if (state.is(WanderingWizardryBlocks.CENTER_ALTAR.get())) {
                return WanderingWizardryBlocks
                        .OUTER_ALTAR
                        .get()
                        .defaultBlockState()
                        .setValue(
                                BlockStateProperties.HORIZONTAL_FACING,
                                direction.getOpposite()
                        );
            }
        }

        return super.getStateForPlacement(context);
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack itemStack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }

        var entity = level.getBlockEntity(pos);
        if (!(entity instanceof AltarBlockEntity altarBlockEntity)) {
            return InteractionResult.PASS;
        }

        if (altarBlockEntity.tryInsertStack(itemStack)) {
            return InteractionResult.SUCCESS;
        }

        if (!itemStack.isEmpty() || hand == InteractionHand.OFF_HAND) {
            return InteractionResult.PASS;
        }

        if (altarBlockEntity.tryRemoveStack(player)) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
