package com.example.examplemod.item.axe.stripping.with_at;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class StrippingRegister {

    public static void register(Block block, Block strippedBlock) {
        if (AxeItem.STRIPPABLES.containsKey(block)) {
            throw new RuntimeException("Duplicated block " + block + " in strippable map");
        }
        ImmutableMap.Builder<Block, Block> builder = ImmutableMap.builder();
        builder.putAll(AxeItem.STRIPPABLES);
        builder.put(block, strippedBlock);
        AxeItem.STRIPPABLES = builder.build();
    }

    public static void registerAll(Map<Block, Block> strippableMap) {
        for (Block block : strippableMap.keySet()) {
            if (AxeItem.STRIPPABLES.containsKey(block)) {
                throw new RuntimeException("Duplicated block " + block + " in strippable map");
            }
        }
        ImmutableMap.Builder<Block, Block> builder = ImmutableMap.builder();
        builder.putAll(AxeItem.STRIPPABLES);
        builder.putAll(strippableMap);
        AxeItem.STRIPPABLES = builder.build();
    }
}
