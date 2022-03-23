package com.example.examplemod.mixin.item.axe.stripping;

import com.example.examplemod.item.axe.stripping.with_mixin.IStrippable;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public abstract class MixinAxeItem {

    /**
     * Add to getAxeStrippingState method, check {@link IStrippable} interface.
     *
     * @see IStrippable
     * @see AxeItem#getAxeStrippingState(BlockState)
     */
    @Inject(method = "getAxeStrippingState", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectGetAxeStrippingState(BlockState originalState, CallbackInfoReturnable<BlockState> cir) {
        Block block = originalState.getBlock();
        if (block instanceof IStrippable stripped && stripped.canStripped()) {
            cir.setReturnValue(stripped.getStrippedBlock());
            cir.cancel();
        }
    }
}
