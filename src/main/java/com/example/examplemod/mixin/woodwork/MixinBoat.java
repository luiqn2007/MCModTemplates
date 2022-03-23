package com.example.examplemod.mixin.woodwork;

import com.example.examplemod.woodwork.with_mixin.Woodwork;
import com.example.examplemod.woodwork.with_mixin.IBoat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Boat.class)
public abstract class MixinBoat implements IBoat {

    private static EntityDataAccessor<String> DATA_ID_WOOD;
    private static final String NBT_KEY_WOODWORK = "mod_woodwork_type";

    /**
     * Inject DATA_ID_TREE data to Boat class
     */
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void injectCInit(CallbackInfo ci) {
        DATA_ID_WOOD = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.STRING);
    }

    /**
     * Initialize DATA_ID_TREE value to empty
     */
    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    protected void injectDefineSynchedData(CallbackInfo ci) {
        ((Boat) (Object) this).getEntityData().define(DATA_ID_WOOD, "");
    }

    /**
     * Drop boat item with tree in {@link Boat#getDropItem()}
     */
    @Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
    protected void injectGetDropItem(CallbackInfoReturnable<Item> cir) {
        getWoodwork().ifPresent(woodwork -> {
            cir.setReturnValue(woodwork.boat());
            cir.cancel();
        });
    }

    /**
     * Save tree name to boat data
     */
    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    protected void injectAddAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        getWoodwork().ifPresent(woodwork ->
                pCompound.putString(NBT_KEY_WOODWORK, woodwork.name.toString()));
    }

    /**
     * Read tree name from data
     */
    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void injectReadAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if (pCompound.contains(NBT_KEY_WOODWORK, Tag.TAG_STRING)) {
            Woodwork.getWoodwork(pCompound.getString(NBT_KEY_WOODWORK)).ifPresent(this::setWoodwork);
        }
    }

    /**
     * Modify dropped plank and stick from tree
     */
    @ModifyArg(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    protected ItemLike modifyCheckFallDamage(ItemLike item) {
        if (item instanceof Block) {
            return getWoodwork().<ItemLike>map(Woodwork::planks).orElse(item);
        }
        return item;
    }

    @Override
    public Optional<Woodwork> getWoodwork() {
        String value = ((Boat) (Object) this).getEntityData().get(DATA_ID_WOOD);
        return Woodwork.getWoodwork(value);
    }

    @Override
    public void setWoodwork(Woodwork woodwork) {
        ((Boat) (Object) this).getEntityData().set(DATA_ID_WOOD, woodwork.name.toString());
    }
}
