package com.example.examplemod.util;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Some utils for stream
 */
public class StreamUtils {

    /**
     * Use for {@link Stream#collect(Collector)}, select a random element in the stream

     * @param random random
     * @param <T> element type
     * @return collector to get a random element, or empty if the stream is empty
     */
    public static <T> Collector<T, ?, Optional<T>> random(Random random) {
        return Collector.<T, List<T>, Optional<T>>of(
                ArrayList::new, List::add, StreamUtils::combiner,
                list -> {
                    if (list.isEmpty()) {
                        return Optional.empty();
                    }
                    if (list.size() == 1) {
                        return Optional.of(list.get(0));
                    }
                    return Optional.of(list.get(random.nextInt(list.size())));
                }
        );
    }

    /**
     * Use for {@link Stream#collect(Collector)}, select a random element in the stream

     * @param random random
     * @param <T> element type
     * @return collector to get a random element, or empty if the stream is empty
     */
    public static <T> Collector<T, ?, Stream<T>> randomStream(Random random) {
        return Collector.<T, List<T>, Stream<T>>of(
                ArrayList::new, List::add, StreamUtils::combiner,
                list -> {
                    if (list.isEmpty()) {
                        return Stream.empty();
                    }
                    if (list.size() == 1) {
                        return Optional.of(list.get(0)).stream();
                    }
                    return Optional.of(list.get(random.nextInt(list.size()))).stream();
                }
        );
    }

    /**
     * Use for {@link Stream#collect(Collector)}, collect all nbt tag to a {@link ListTag}

     * @return collector to get a nbt tag list
     */
    public static <NBT extends Tag> Collector<NBT, ?, ListTag> toTagList() {
        return Collector.of(ListTag::new, ListTag::add, StreamUtils::combiner, Function.identity(),
                Collector.Characteristics.IDENTITY_FINISH);
    }

    public static <T, L extends Collection<T>> L combiner(L l1, L l2) {
        l1.addAll(l2);
        return l1;
    }
}
