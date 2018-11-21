package com.wallet.parser.util;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * Created by Liodegar on 11/20/2018.
 */
public class ParserCollectors {

    /**
     * Adapts a Collector to one accepting elements of the same type T by applying the predicate to each input element
     * and only accumulating if the predicate returns true.  This functionality is already in Java 9
     * @param predicate a predicate to be applied to the input elements
     * @param downstream a collector which will accept values that match the predicate
     * @param <T> T - the type of the input elements
     * @param <A>  A - intermediate accumulation type of the downstream collector
     * @param <R>  R - result type of collector
     * @return  a collector which applies the predicate to the input elements and provides matching elements to the downstream collector
     */
    public static <T, A, R> Collector<T, ?, R> filtering(Predicate<? super T> predicate, Collector<? super T, A, R> downstream) {
        BiConsumer<A, ? super T> accumulator = downstream.accumulator();
        return Collector.of(downstream.supplier(),
                (r, t) -> {
                    if (predicate.test(t)) accumulator.accept(r, t);
                },
                downstream.combiner(), downstream.finisher(),
                downstream.characteristics().toArray(new Collector.Characteristics[0]));
    }

}
