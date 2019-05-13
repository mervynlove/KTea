
package com.mengwei.ktea.rxbus.rxrelay;



import android.support.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public final class BehaviorRelay<T> extends Relay<T> {


    private static final Object[] EMPTY_ARRAY = new Object[0];

    final AtomicReference<T> value;

    private final AtomicReference<BehaviorDisposable<T>[]> subscribers;

    @SuppressWarnings("rawtypes")
    private static final BehaviorDisposable[] EMPTY = new BehaviorDisposable[0];

    final Lock readLock;
    private final Lock writeLock;

    long index;


    public static <T> BehaviorRelay<T> create() {
        return new BehaviorRelay<T>();
    }


    public static <T> BehaviorRelay<T> createDefault(T defaultValue) {
        return new BehaviorRelay<T>(defaultValue);
    }


    @SuppressWarnings("unchecked") private BehaviorRelay() {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
        this.subscribers = new AtomicReference<BehaviorDisposable<T>[]>(EMPTY);
        this.value = new AtomicReference<T>();
    }


    private BehaviorRelay(T defaultValue) {
        this();
        if (defaultValue == null) throw new NullPointerException("defaultValue == null");
        value.lazySet(defaultValue);
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        BehaviorDisposable<T> bs = new BehaviorDisposable<T>(observer, this);
        observer.onSubscribe(bs);
        add(bs);
        if (bs.cancelled) {
            remove(bs);
        } else {
            bs.emitFirst();
        }
    }

    @Override
    public void accept(T value) {
        if (value == null) throw new NullPointerException("value == null");

        setCurrent(value);
        for (BehaviorDisposable<T> bs : subscribers.get()) {
            bs.emitNext(value, index);
        }
    }

    @Override
    public boolean hasObservers() {
        return subscribers.get().length != 0;
    }

    int subscriberCount() {
        return subscribers.get().length;
    }


    @Nullable
    public T getValue() {
        return value.get();
    }

    public Object[] getValues() {
        @SuppressWarnings("unchecked")
        T[] a = (T[])EMPTY_ARRAY;
        T[] b = getValues(a);
        if (b == EMPTY_ARRAY) {
            return new Object[0];
        }
        return b;

    }

    @SuppressWarnings("unchecked")
    public T[] getValues(T[] array) {
        T o = value.get();
        if (o == null) {
            if (array.length != 0) {
                array[0] = null;
            }
            return array;
        }
        if (array.length != 0) {
            array[0] = o;
            if (array.length != 1) {
                array[1] = null;
            }
        } else {
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), 1);
            array[0] = o;
        }
        return array;
    }

    public boolean hasValue() {
        return value.get() != null;
    }

    private void add(BehaviorDisposable<T> rs) {
        for (;;) {
            BehaviorDisposable<T>[] a = subscribers.get();
            int len = a.length;
            @SuppressWarnings("unchecked")
            BehaviorDisposable<T>[] b = new BehaviorDisposable[len + 1];
            System.arraycopy(a, 0, b, 0, len);
            b[len] = rs;
            if (subscribers.compareAndSet(a, b)) {
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    void remove(BehaviorDisposable<T> rs) {
        for (;;) {
            BehaviorDisposable<T>[] a = subscribers.get();
            if (a == EMPTY) {
                return;
            }
            int len = a.length;
            int j = -1;
            for (int i = 0; i < len; i++) {
                if (a[i] == rs) {
                    j = i;
                    break;
                }
            }

            if (j < 0) {
                return;
            }
            BehaviorDisposable<T>[] b;
            if (len == 1) {
                b = EMPTY;
            } else {
                b = new BehaviorDisposable[len - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(a, j + 1, b, j, len - j - 1);
            }
            if (subscribers.compareAndSet(a, b)) {
                return;
            }
        }
    }

    private void setCurrent(T current) {
        writeLock.lock();
        try {
            index++;
            value.lazySet(current);
        } finally {
            writeLock.unlock();
        }
    }

    static final class BehaviorDisposable<T> implements Disposable, AppendOnlyLinkedArrayList.NonThrowingPredicate<T> {

        final Observer<? super T> actual;
        final BehaviorRelay<T> state;

        boolean next;
        boolean emitting;
        AppendOnlyLinkedArrayList<T> queue;

        boolean fastPath;

        volatile boolean cancelled;

        long index;

        BehaviorDisposable(Observer<? super T> actual, BehaviorRelay<T> state) {
            this.actual = actual;
            this.state = state;
        }

        @Override
        public void dispose() {
            if (!cancelled) {
                cancelled = true;

                state.remove(this);
            }
        }

        @Override
        public boolean isDisposed() {
            return cancelled;
        }

        void emitFirst() {
            if (cancelled) {
                return;
            }
            T o;
            synchronized (this) {
                if (cancelled) {
                    return;
                }
                if (next) {
                    return;
                }

                BehaviorRelay<T> s = state;
                Lock lock = s.readLock;

                lock.lock();
                index = s.index;
                o = s.value.get();
                lock.unlock();

                emitting = o != null;
                next = true;
            }

            if (o != null) {
                test(o);
                emitLoop();
            }
        }

        void emitNext(T value, long stateIndex) {
            if (cancelled) {
                return;
            }
            if (!fastPath) {
                synchronized (this) {
                    if (cancelled) {
                        return;
                    }
                    if (index == stateIndex) {
                        return;
                    }
                    if (emitting) {
                        AppendOnlyLinkedArrayList<T> q = queue;
                        if (q == null) {
                            q = new AppendOnlyLinkedArrayList<T>(4);
                            queue = q;
                        }
                        q.add(value);
                        return;
                    }
                    next = true;
                }
                fastPath = true;
            }

            test(value);
        }

        @Override
        public boolean test(T o) {
            if (!cancelled) {
                actual.onNext(o);
            }
            return false;
        }

        void emitLoop() {
            for (;;) {
                if (cancelled) {
                    return;
                }
                AppendOnlyLinkedArrayList<T> q;
                synchronized (this) {
                    q = queue;
                    if (q == null) {
                        emitting = false;
                        return;
                    }
                    queue = null;
                }

                q.forEachWhile(this);
            }
        }
    }
}
