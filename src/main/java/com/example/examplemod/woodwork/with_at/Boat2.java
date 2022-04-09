package com.example.examplemod.woodwork.with_at;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Boat2 extends Boat {

    public static final EntityDataAccessor<String> DATA_WOODWORK_TYPE = SynchedEntityData.defineId(Boat2.class, EntityDataSerializers.STRING);
    private static final String NBT_KEY_WOODWORK = "woodwork_type";

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

    @Nullable
    @Override
    public ItemEntity spawnAtLocation(ItemLike pItem) {
        if (pItem == getBoatType().getPlanks()) {
            Optional<Woodwork> optional = getWoodwork();
            if (optional.isPresent()) {
                return super.spawnAtLocation(optional.get().planks());
            }
        }
        return super.spawnAtLocation(pItem);
    }
}
