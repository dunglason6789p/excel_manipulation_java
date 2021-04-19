package util;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CollectionUtil {
    public static <T> void forEachIndexed(Iterable<T> iterable, BiConsumer<T, Integer> biConsumer) {
        if (iterable != null) {
            int index = 0;
            for (T item : iterable) {
                biConsumer.accept(item, index);
                index++;
            }
        }
    }
    public static <T> void forEachIndexed(
            Iterable<T> iterable,
            BiConsumer<T, Integer> biConsumer,
            AtomicBoolean flagBreak
    ) {
        if (iterable != null) {
            int index = 0;
            for (T item : iterable) {
                biConsumer.accept(item, index);
                if (flagBreak != null && flagBreak.get()) {
                    break;
                }
                index++;
            }
        }
    }
    public static <T> void forEachIndexed(
            Iterable<T> iterable,
            BiConsumer<T, Integer> biConsumer,
            AtomicBoolean flagBreakBefore,
            AtomicBoolean flagBreakAfter
    ) {
        if (iterable != null) {
            int index = 0;
            for (T item : iterable) {
                if (flagBreakBefore != null && flagBreakBefore.get()) {
                    break;
                }
                biConsumer.accept(item, index);
                if (flagBreakAfter != null && flagBreakAfter.get()) {
                    break;
                }
                index++;
            }
        }
    }

    /*public static <T> void forEachIndexed(
            List<T> list,
            AbstractMap.SimpleEntry<Integer, Integer> rangeInclusive,
            BiConsumer<T, Integer> biConsumer
    ) {
        Integer start = rangeInclusive.getKey();
        if (start == null) {
            start = 0;
        }
        Integer end = rangeInclusive.getValue();
        if (end == null) {
            end = list.size() - 1;
        }
        if (list != null) {
            for (int index = start; index <= end; index++) {
                biConsumer.accept(list.get(index), index);
            }
        }
    }*/
    public static <T> void forEachIndexed(
            Iterable<T> iterable,
            AbstractMap.SimpleEntry<Integer, Integer> rangeInclusive,
            BiConsumer<T, Integer> biConsumer
    ) {
        Integer start = rangeInclusive.getKey();
        Integer end = rangeInclusive.getValue();
        if (iterable instanceof List) {
            List<T> list = (List)iterable;
            if (start == null) {
                start = 0;
            }
            if (end == null) {
                end = list.size() - 1;
            }
            for (int index = start; index <= end; index++) {
                biConsumer.accept(list.get(index), index);
            }
        } else if (iterable != null) {
            int index = -1;
            for (T item : iterable) {
                index++;
                if (inRange(index, rangeInclusive, true)) {
                    biConsumer.accept(item, index);
                } else if (end != null && index > end) {
                    break;
                }
            }
        }
    }
    public static boolean inRange(Integer integer, AbstractMap.SimpleEntry<Integer, Integer> rangeInclusive) {
        Integer start = rangeInclusive.getKey();
        Integer end = rangeInclusive.getValue();
        if (start == null || end == null) {
            throw new IllegalArgumentException("Range cannot start or end with null!");
        }
        return start <= integer && integer <= end;
    }
    public static boolean inRange(
            Integer integer,
            AbstractMap.SimpleEntry<Integer, Integer> rangeInclusive,
            boolean defaultReturnValue
    ) {
        Integer start = rangeInclusive.getKey();
        Integer end = rangeInclusive.getValue();
        if (start == null || end == null) {
            return defaultReturnValue;
        }
        return start <= integer && integer <= end;
    }
}
