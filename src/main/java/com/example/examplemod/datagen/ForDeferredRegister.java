package com.example.examplemod.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ForDeferredRegister<T extends IForgeRegistryEntry<T>> {

    private final DeferredRegister<T> register;
    private final List<Pair<Predicate<T>, Consumer<RegistryObject<T>>>> adders = new ArrayList<>();
    @Nullable
    private Consumer<RegistryObject<T>> defaultAdder = null;
    private final Set<T> skips = new HashSet<>();

    public ForDeferredRegister(DeferredRegister<T> register) {
        this.register = register;
    }

    public ForDeferredRegister<T> forElement(Predicate<T> filter, Consumer<T> adder) {
        adders.add(Pair.of(filter, b -> adder.accept(b.get())));
        return this;
    }

    public ForDeferredRegister<T> forDefault(@Nullable Consumer<RegistryObject<T>> adder) {
        defaultAdder = adder;
        return this;
    }

    public ForDeferredRegister<T> skip(T block) {
        skips.add(block);
        return this;
    }

    public ForDeferredRegister<T> skip(Supplier<? extends T> block) {
        skips.add(block.get());
        return this;
    }

    public ForDeferredRegister<T> skipAll(Collection<? extends T> blocks) {
        skips.addAll(blocks);
        return this;
    }

    public ForDeferredRegister<T> skipAllSup(Collection<? extends Supplier<? extends T>> blocks) {
        skips.addAll(blocks.stream().map(Supplier::get).toList());
        return this;
    }

    public void addAll() {
        Optional<Consumer<RegistryObject<T>>> defaultAdder = Optional.ofNullable(this.defaultAdder);
        for (RegistryObject<T> entry : register.getEntries()) {
            T block = entry.get();
            if (skips.contains(block)) {
                continue;
            }
            adders.stream()
                    .filter(p -> p.getFirst().test(block))
                    .findFirst()
                    .map(Pair::getSecond)
                    .or(() -> defaultAdder)
                    .ifPresent(c -> c.accept(entry));
        }
    }
}
