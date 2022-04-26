package com.example.examplemod.woodwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public abstract class ModBoatDispenseBehavior<T extends Boat> extends DefaultDispenseItemBehavior {

    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

    @Override
    protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
        Direction direction = pSource.getBlockState().getValue(DispenserBlock.FACING);
        Level level = pSource.getLevel();
        double d0 = pSource.x() + direction.getStepX() * 1.125F;
        double d1 = pSource.y() + direction.getStepY() * 1.125F;
        double d2 = pSource.z() + direction.getStepZ() * 1.125F;
        BlockPos blockpos = pSource.getPos().relative(direction);
        double d3;
        if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
            d3 = 1.0D;
        } else {
            if (!level.getBlockState(blockpos).isAir() || !level.getFluidState(blockpos.below()).is(FluidTags.WATER)) {
                return this.defaultDispenseItemBehavior.dispense(pSource, pStack);
            }

            d3 = 0.0D;
        }

        T boat = newBoat(level, d0, d1 + d3, d2, direction.toYRot());
        level.addFreshEntity(boat);
        return afterBoatPlaced(pStack);
    }

    protected abstract T newBoat(Level level, double x, double y, double z, float yRot);

    protected ItemStack afterBoatPlaced(ItemStack stack) {
        stack.shrink(1);
        return stack;
    }
}
