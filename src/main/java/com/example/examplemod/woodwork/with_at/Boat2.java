package com.example.examplemod.woodwork.with_at;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class Boat2 extends Boat {

    public static final EntityDataAccessor<String> DATA_WOODWORK_TYPE = SynchedEntityData.defineId(Boat2.class, EntityDataSerializers.STRING);
    private static final String NBT_KEY_WOODWORK = "mod_woodwork_type";

    public Boat2(EntityType<? extends Boat2> entityType, Level level) {
        super(entityType, level);
    }

    public Boat2(Level level, double x, double y, double z) {
        this(Woodwork.boatEntityType(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    public void setWoodwork(Woodwork woodwork) {
        entityData.set(DATA_WOODWORK_TYPE, woodwork.name.toString());
    }

    public Optional<Woodwork> getWoodwork() {
        return Woodwork.getWoodwork(entityData.get(DATA_WOODWORK_TYPE));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_WOODWORK_TYPE, "");
    }

    @Override
    public Item getDropItem() {
        return getWoodwork().map(Woodwork::boat).orElseGet(() -> (BoatItem) super.getDropItem());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString(NBT_KEY_WOODWORK, entityData.get(DATA_WOODWORK_TYPE));

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(DATA_WOODWORK_TYPE, pCompound.getString(NBT_KEY_WOODWORK));
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
        Optional<Woodwork> optional = getWoodwork();
        if (optional.isPresent()) {
            this.lastYd = this.getDeltaMovement().y;
            if (!this.isPassenger()) {
                if (pOnGround) {
                    if (this.fallDistance > 3.0F) {
                        if (this.status != Boat.Status.ON_LAND) {
                            this.resetFallDistance();
                            return;
                        }

                        this.causeFallDamage(this.fallDistance, 1.0F, DamageSource.FALL);
                        if (!this.level.isClientSide && !this.isRemoved()) {
                            this.kill();
                            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                for(int i = 0; i < 3; ++i) {
                                    this.spawnAtLocation(optional.get().planks());
                                }

                                for(int j = 0; j < 2; ++j) {
                                    this.spawnAtLocation(Items.STICK);
                                }
                            }
                        }
                    }

                    this.resetFallDistance();
                } else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && pY < 0.0D) {
                    this.fallDistance = (float)((double)this.fallDistance - pY);
                }

            }
        } else {
            super.checkFallDamage(pY, pOnGround, pState, pPos);
        }

    }
}
