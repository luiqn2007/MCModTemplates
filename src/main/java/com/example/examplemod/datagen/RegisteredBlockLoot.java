package com.example.examplemod.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.*;

public class RegisteredBlockLoot extends BlockLoot {

    private final DeferredRegister<Block> register;
    private final Set<Block> addedBlocks = new HashSet<>();

    private final List<Pair<Predicate<Block>, Function<Block, LootTable.Builder>>> builders = new ArrayList<>();
    @Nullable
    private Function<Block, LootTable.Builder> defaultBuilder = BlockLoot::createSingleItemTable;
    private final Set<Block> skips = new HashSet<>();

    public RegisteredBlockLoot(DeferredRegister<Block> register) {
        this.register = register;
    }

    public RegisteredBlockLoot forElement(Predicate<Block> filter, Function<Block, LootTable.Builder> builder) {
        builders.add(Pair.of(filter, builder));
        return this;
    }

    public RegisteredBlockLoot forDefault(@Nullable Function<Block, LootTable.Builder> builder) {
        defaultBuilder = builder;
        return this;
    }

    public RegisteredBlockLoot skip(Block block) {
        skips.add(block);
        return this;
    }

    public RegisteredBlockLoot skip(Supplier<Block> block) {
        skips.add(block.get());
        return this;
    }

    public RegisteredBlockLoot skipAll(Collection<? extends Block> blocks) {
        skips.addAll(blocks);
        return this;
    }

    public RegisteredBlockLoot skipAllSup(Collection<? extends Supplier<? extends Block>> blocks) {
        skips.addAll(blocks.stream().map(Supplier::get).toList());
        return this;
    }

    /**
     * Return added blocks by the loot table, use for block filter
     *
     * @return blocks
     */
    public Set<Block> knownBlocks() {
        return Set.copyOf(addedBlocks);
    }

    public Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet> toPair() {
        return Pair.of(() -> this, LootContextParamSets.BLOCK);
    }

    @Override
    protected void addTables() {
        register.getEntries().stream()
                .map(Supplier::get)
                .filter(b -> !skips.contains(b))
                .flatMap(b -> builders.stream()
                        .filter(p -> p.getFirst().test(b))
                        .findFirst()
                        .map(Pair::getSecond)
                        .or(() -> Optional.ofNullable(defaultBuilder))
                        .map(p -> Pair.of(b, p))
                        .stream())
                .findFirst()
                .ifPresent(p -> add(p.getFirst(), p.getSecond()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return addedBlocks;
    }

    @Override
    protected void add(Block pBlock, LootTable.Builder pLootTableBuilder) {
        super.add(pBlock, pLootTableBuilder);
        addedBlocks.add(pBlock);
    }
}
