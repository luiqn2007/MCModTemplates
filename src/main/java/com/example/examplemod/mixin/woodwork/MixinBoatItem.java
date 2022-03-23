package com.example.examplemod.mixin.woodwork;

import com.example.examplemod.woodwork.with_mixin.Woodwork;
import com.example.examplemod.woodwork.with_mixin.IBoat;
import com.example.examplemod.woodwork.with_mixin.IBoatItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BoatItem.class)
public abstract class MixinBoatItem implements IBoatItem {

    private Woodwork modWoodwork = null;

    @Override
    public void setWoodwork(Woodwork woodwork) {
        this.modWoodwork = woodwork;
    }

    /**
     * Insert after {@link Boat#setType(Boat.Type)}, bind tree to boat if exist
     */
    @Inject(method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;setYRot(F)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    protected void injectUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
                             ItemStack itemstack, HitResult hitresult, Vec3 vec3, double d0, List<Entity> list, Boat boat) {
        if (modWoodwork != null) {
            ((IBoat) boat).setWoodwork(modWoodwork);
        }
    }
}
