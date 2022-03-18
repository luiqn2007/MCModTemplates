package com.example.examplemod.item.axe.stripping.with_mixin;

import net.minecraft.world.level.block.state.BlockState;

/**
 * An interface for block.
 * <p>A block implement this interface, the axe can make it stripped</p>
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
