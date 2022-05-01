package com.example.examplemod.boat;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WoodenBoatEntity extends Boat {

    public static final EntityDataAccessor<String> DATA_WOOD_TYPE = SynchedEntityData.defineId(WoodenBoatEntity.class, EntityDataSerializers.STRING);
    private static final String NBT_KEY_TYPE = "wood_type";

    public WoodenBoatEntity(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    public WoodenBoatEntity(WoodenBoat boat, Level level, double x, double y, double z) {
        this(boat.entity(), level);
        this.setType(Boat.Type.OAK);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        setBoat(boat);
    }

    public WoodenBoatEntity(WoodenBoat boat, Level level, double x, double y, double z, float yRot) {
        this(boat, level, x, y, z);
        setYRot(yRot);
    }

    public Optional<Item> boatItem() {
        return Registry.ITEM.getOptional(new ResourceLocation(entityData.get(DATA_WOOD_TYPE)));
    }

    public Optional<WoodenBoat> boat() {
        return boatItem().map(i -> (WoodenBoat) i);
    }

    private void setBoat(WoodenBoat boat) {
        entityData.set(DATA_WOOD_TYPE, Registry.ITEM.getKey(boat).toString());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_WOOD_TYPE, "");
    }

    @Override
    public Item getDropItem() {
        return boatItem().orElse(super.getDropItem());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString(NBT_KEY_TYPE, entityData.get(DATA_WOOD_TYPE));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains(NBT_KEY_TYPE, Tag.TAG_STRING)) {
            entityData.set(DATA_WOOD_TYPE, pCompound.getString(NBT_KEY_TYPE));
        }
    }

    @Nullable
    @Override
    public ItemEntity spawnAtLocation(ItemLike pItem) {
        if (pItem == getBoatType().getPlanks()) {
            return boat()
                    .map(boat -> super.spawnAtLocation(boat.planks()))
                    .orElseGet(() -> super.spawnAtLocation(pItem));
        }
        return super.spawnAtLocation(pItem);
    }
}
