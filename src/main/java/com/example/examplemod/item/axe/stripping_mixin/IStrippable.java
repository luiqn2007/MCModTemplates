package com.example.examplemod.item.axe.stripping_mixin;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Custom strip recipe base on mixin
 * <p>A block implement this interface, the axe can make it stripped</p>
 *
 * <p>You should implement {@link IStrippable} to your block</p>
 *
 * @see com.example.examplemod.mixin.item.axe.stripping.MixinAxeItem
 */
public interface IStrippable {

    /**
     * Check if the block can be stripped
     *
     * @return true if stripped
     */
    boolean canStripped();

    /**
     * Get stripped block
     *
     * @return stripped block
     */
    BlockState getStrippedBlock();
}
