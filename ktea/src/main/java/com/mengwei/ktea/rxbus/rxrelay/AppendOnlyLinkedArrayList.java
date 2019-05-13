
package com.mengwei.ktea.rxbus.rxrelay;

import io.reactivex.functions.Predicate;

class AppendOnlyLinkedArrayList<T> {
    private final int capacity;
    private final Object[] head;
    private Object[] tail;
    private int offset;

    AppendOnlyLinkedArrayList(int capacity) {
        this.capacity = capacity;
        this.head = new Object[capacity + 1];
        this.tail = head;
    }

    void add(T value) {
        final int c = capacity;
        int o = offset;
        if (o == c) {
            Object[] next = new Object[c + 1];
            tail[c] = next;
            tail = next;
            o = 0;
        }
        tail[o] = value;
        offset = o + 1;
    }


    public interface NonThrowingPredicate<T> extends Predicate<T> {
        @Override
        boolean test(T t);
    }

    @SuppressWarnings("unchecked")
    void forEachWhile(NonThrowingPredicate<? super T> consumer) {
        Object[] a = head;
        final int c = capacity;
        while (a != null) {
            for (int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null) {
                    break;
                }
                if (consumer.test((T)o)) {
                    break;
                }
            }
            a = (Object[])a[c];
        }
    }

    @SuppressWarnings("unchecked")
    boolean accept(Relay<? super T> observer) {
        Object[] a = head;
        final int c = capacity;
        while (a != null) {
            for (int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null) {
                    break;
                }

                observer.accept((T) o);
            }
            a = (Object[])a[c];
        }
        return false;
    }
}
