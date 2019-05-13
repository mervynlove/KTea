

package com.mengwei.ktea.rxbus.rxrelay;

import android.support.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ReplayRelay<T> extends Relay<T> {
    final ReplayBuffer<T> buffer;

    final AtomicReference<ReplayDisposable<T>[]> observers;

    @SuppressWarnings("rawtypes")
    static final ReplayDisposable[] EMPTY = new ReplayDisposable[0];


    public static <T> ReplayRelay<T> create() {
        return new ReplayRelay<T>(new UnboundedReplayBuffer<T>(16));
    }

    public static <T> ReplayRelay<T> create(int capacityHint) {
        return new ReplayRelay<T>(new UnboundedReplayBuffer<T>(capacityHint));
    }

    public static <T> ReplayRelay<T> createWithSize(int maxSize) {
        return new ReplayRelay<T>(new SizeBoundReplayBuffer<T>(maxSize));
    }

    static <T> ReplayRelay<T> createUnbounded() {
        return new ReplayRelay<T>(new SizeBoundReplayBuffer<T>(Integer.MAX_VALUE));
    }

    public static <T> ReplayRelay<T> createWithTime(long maxAge, TimeUnit unit, Scheduler scheduler) {
        return new ReplayRelay<T>(new SizeAndTimeBoundReplayBuffer<T>(Integer.MAX_VALUE, maxAge, unit, scheduler));
    }

    public static <T> ReplayRelay<T> createWithTimeAndSize(long maxAge, TimeUnit unit, Scheduler scheduler, int maxSize) {
        return new ReplayRelay<T>(new SizeAndTimeBoundReplayBuffer<T>(maxSize, maxAge, unit, scheduler));
    }


    @SuppressWarnings("unchecked") ReplayRelay(ReplayBuffer<T> buffer) {
        this.buffer = buffer;
        this.observers = new AtomicReference<ReplayDisposable<T>[]>(EMPTY);
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        ReplayDisposable<T> rs = new ReplayDisposable<T>(observer, this);
        observer.onSubscribe(rs);

        if (!rs.cancelled) {
            if (add(rs)) {
                if (rs.cancelled) {
                    remove(rs);
                    return;
                }
            }
            buffer.replay(rs);
        }
    }

    @Override
    public void accept(T value) {
        if (value == null) throw new NullPointerException("value == null");

        ReplayBuffer<T> b = buffer;
        b.add(value);

        for (ReplayDisposable<T> rs : observers.get()) {
            b.replay(rs);
        }
    }

    @Override
    public boolean hasObservers() {
        return observers.get().length != 0;
    }

    int observerCount() {
        return observers.get().length;
    }

    @Nullable
    public T getValue() {
        return buffer.getValue();
    }


    private static final Object[] EMPTY_ARRAY = new Object[0];

    public Object[] getValues() {
        @SuppressWarnings("unchecked")
        T[] a = (T[])EMPTY_ARRAY;
        T[] b = getValues(a);
        if (b == EMPTY_ARRAY) {
            return new Object[0];
        }
        return b;

    }

    public T[] getValues(T[] array) {
        return buffer.getValues(array);
    }

    public boolean hasValue() {
        return buffer.size() != 0; // NOPMD
    }

    /* test*/ int size() {
        return buffer.size();
    }

    boolean add(ReplayDisposable<T> rs) {
        for (;;) {
            ReplayDisposable<T>[] a = observers.get();
            int len = a.length;
            @SuppressWarnings("unchecked")
            ReplayDisposable<T>[] b = new ReplayDisposable[len + 1];
            System.arraycopy(a, 0, b, 0, len);
            b[len] = rs;
            if (observers.compareAndSet(a, b)) {
                return true;
            }
        }
    }

    @SuppressWarnings("unchecked")
    void remove(ReplayDisposable<T> rs) {
        for (;;) {
            ReplayDisposable<T>[] a = observers.get();
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
            ReplayDisposable<T>[] b;
            if (len == 1) {
                b = EMPTY;
            } else {
                b = new ReplayDisposable[len - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(a, j + 1, b, j, len - j - 1);
            }
            if (observers.compareAndSet(a, b)) {
                return;
            }
        }
    }

    interface ReplayBuffer<T> {

        void add(T value);

        void replay(ReplayDisposable<T> rs);

        int size();

        @Nullable
        T getValue();

        T[] getValues(T[] array);
    }

    static final class ReplayDisposable<T> extends AtomicInteger implements Disposable {

        private static final long serialVersionUID = 466549804534799122L;
        final Observer<? super T> actual;
        final ReplayRelay<T> state;

        Object index;

        volatile boolean cancelled;

        ReplayDisposable(Observer<? super T> actual, ReplayRelay<T> state) {
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
    }

    static final class UnboundedReplayBuffer<T>
    extends AtomicReference<Object>
    implements ReplayBuffer<T> {

        private static final long serialVersionUID = -733876083048047795L;

        final List<T> buffer;

        volatile int size;

        UnboundedReplayBuffer(int capacityHint) {
            if (capacityHint <= 0) throw new IllegalArgumentException("capacityHint <= 0");
            this.buffer = new ArrayList<T>(capacityHint);
        }

        @Override
        public void add(T value) {
            buffer.add(value);
            size++;
        }

        @Override
        @Nullable
        @SuppressWarnings("unchecked")
        public T getValue() {
            int s = size;
            if (s != 0) {
                return buffer.get(s - 1);
            }
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T[] getValues(T[] array) {
            int s = size;
            if (s == 0) {
                if (array.length != 0) {
                    array[0] = null;
                }
                return array;
            }

            if (array.length < s) {
                array = (T[]) Array.newInstance(array.getClass().getComponentType(), s);
            }
            List<T> b = buffer;
            for (int i = 0; i < s; i++) {
                array[i] = b.get(i);
            }
            if (array.length > s) {
                array[s] = null;
            }

            return array;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void replay(ReplayDisposable<T> rs) {
            if (rs.getAndIncrement() != 0) {
                return;
            }

            int missed = 1;
            final List<T> b = buffer;
            final Observer<? super T> a = rs.actual;

            Integer indexObject = (Integer)rs.index;
            int index;
            if (indexObject != null) {
                index = indexObject;
            } else {
                index = 0;
                rs.index = 0;
            }

            for (;;) {

                if (rs.cancelled) {
                    rs.index = null;
                    return;
                }

                int s = size;

                while (s != index) {

                    if (rs.cancelled) {
                        rs.index = null;
                        return;
                    }

                    T o = b.get(index);

                    a.onNext((T)o);
                    index++;
                }

                if (index != size) {
                    continue;
                }

                rs.index = index;

                missed = rs.addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }

        @Override
        public int size() {
            int s = size;
            return s != 0 ? s : 0;
        }
    }

    static final class Node<T> extends AtomicReference<Node<T>> {

        private static final long serialVersionUID = 6404226426336033100L;

        final T value;

        Node(T value) {
            this.value = value;
        }
    }

    static final class TimedNode<T> extends AtomicReference<TimedNode<T>> {

        private static final long serialVersionUID = 6404226426336033100L;

        final T value;
        final long time;

        TimedNode(T value, long time) {
            this.value = value;
            this.time = time;
        }
    }

    static final class SizeBoundReplayBuffer<T>
    extends AtomicReference<Object>
    implements ReplayBuffer<T> {

        private static final long serialVersionUID = 1107649250281456395L;

        final int maxSize;
        int size;

        volatile Node<T> head;

        Node<T> tail;

        SizeBoundReplayBuffer(int maxSize) {
            if (maxSize <= 0) {
                throw new IllegalArgumentException("maxSize > 0 required but it was " + maxSize);
            }
            this.maxSize = maxSize;
            Node<T> h = new Node<T>(null);
            this.tail = h;
            this.head = h;
        }

        void trim() {
            if (size > maxSize) {
                size--;
                Node<T> h = head;
                head = h.get();
            }
        }

        @Override
        public void add(T value) {
            Node<T> n = new Node<T>(value);
            Node<T> t = tail;

            tail = n;
            size++;
            t.set(n); // releases both the tail and size

            trim();
        }

        @Override
        @Nullable
        @SuppressWarnings("unchecked")
        public T getValue() {
            Node<T> h = head;

            for (;;) {
                Node<T> next = h.get();
                if (next == null) {
                    break;
                }
                h = next;
            }

            return h.value;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T[] getValues(T[] array) {
            Node<T> h = head;
            int s = size();

            if (s == 0) {
                if (array.length != 0) {
                    array[0] = null;
                }
            } else {
                if (array.length < s) {
                    array = (T[]) Array.newInstance(array.getClass().getComponentType(), s);
                }

                int i = 0;
                while (i != s) {
                    Node<T> next = h.get();
                    array[i] = (T)next.value;
                    i++;
                    h = next;
                }
                if (array.length > s) {
                    array[s] = null;
                }
            }

            return array;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void replay(ReplayDisposable<T> rs) {
            if (rs.getAndIncrement() != 0) {
                return;
            }

            int missed = 1;
            final Observer<? super T> a = rs.actual;

            Node<T> index = (Node<T>)rs.index;
            if (index == null) {
                index = head;
            }

            for (;;) {

                for (;;) {
                    if (rs.cancelled) {
                        rs.index = null;
                        return;
                    }

                    Node<T> n = index.get();

                    if (n == null) {
                        break;
                    }

                    T o = n.value;

                    a.onNext((T)o);

                    index = n;
                }

                if (index.get() != null) {
                    continue;
                }

                rs.index = index;

                missed = rs.addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }

        @Override
        public int size() {
            int s = 0;
            Node<T> h = head;
            while (s != Integer.MAX_VALUE) {
                Node<T> next = h.get();
                if (next == null) {
                    break;
                }
                s++;
                h = next;
            }

            return s;
        }
    }

    static final class SizeAndTimeBoundReplayBuffer<T>
    extends AtomicReference<Object>
    implements ReplayBuffer<T> {

        private static final long serialVersionUID = -8056260896137901749L;

        final int maxSize;
        final long maxAge;
        final TimeUnit unit;
        final Scheduler scheduler;
        int size;

        volatile TimedNode<T> head;

        TimedNode<T> tail;

        SizeAndTimeBoundReplayBuffer(int maxSize, long maxAge, TimeUnit unit, Scheduler scheduler) {
            if (maxSize <= 0) {
                throw new IllegalArgumentException("maxSize > 0 required but it was " + maxSize);
            }
            if (maxAge <= 0) {
                throw new IllegalArgumentException("maxAge > 0 required but it was " + maxAge);
            }
            if (unit == null) throw new NullPointerException("unit == null");
            if (scheduler == null) throw new NullPointerException("scheduler == null");
            this.maxSize = maxSize;
            this.maxAge = maxAge;
            this.unit = unit;
            this.scheduler = scheduler;
            TimedNode<T> h = new TimedNode<T>(null, 0L);
            this.tail = h;
            this.head = h;
        }

        void trim() {
            if (size > maxSize) {
                size--;
                TimedNode<T> h = head;
                head = h.get();
            }
            long limit = scheduler.now(unit) - maxAge;

            TimedNode<T> h = head;

            for (;;) {
                TimedNode<T> next = h.get();
                if (next == null) {
                    head = h;
                    break;
                }

                if (next.time > limit) {
                    head = h;
                    break;
                }

                h = next;
            }

        }

        @Override
        public void add(T value) {
            TimedNode<T> n = new TimedNode<T>(value, scheduler.now(unit));
            TimedNode<T> t = tail;

            tail = n;
            size++;
            t.set(n); // releases both the tail and size

            trim();
        }

        @Override
        @Nullable
        @SuppressWarnings("unchecked")
        public T getValue() {
            TimedNode<T> h = head;

            for (;;) {
                TimedNode<T> next = h.get();
                if (next == null) {
                    break;
                }
                h = next;
            }

            return h.value;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T[] getValues(T[] array) {
            TimedNode<T> h = head;
            int s = size();

            if (s == 0) {
                if (array.length != 0) {
                    array[0] = null;
                }
            } else {
                if (array.length < s) {
                    array = (T[]) Array.newInstance(array.getClass().getComponentType(), s);
                }

                int i = 0;
                while (i != s) {
                    TimedNode<T> next = h.get();
                    array[i] = next.value;
                    i++;
                    h = next;
                }
                if (array.length > s) {
                    array[s] = null;
                }
            }

            return array;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void replay(ReplayDisposable<T> rs) {
            if (rs.getAndIncrement() != 0) {
                return;
            }

            int missed = 1;
            final Observer<? super T> a = rs.actual;

            TimedNode<T> index = (TimedNode<T>)rs.index;
            if (index == null) {
                index = head;
                // skip old entries
                long limit = scheduler.now(unit) - maxAge;
                TimedNode<T> next = index.get();
                while (next != null) {
                    long ts = next.time;
                    if (ts > limit) {
                        break;
                    }
                    index = next;
                    next = index.get();
                }
            }

            for (;;) {

                if (rs.cancelled) {
                    rs.index = null;
                    return;
                }

                for (;;) {
                    if (rs.cancelled) {
                        rs.index = null;
                        return;
                    }

                    TimedNode<T> n = index.get();

                    if (n == null) {
                        break;
                    }

                    T o = n.value;

                    a.onNext(o);

                    index = n;
                }

                if (index.get() != null) {
                    continue;
                }

                rs.index = index;

                missed = rs.addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }

        @Override
        public int size() {
            int s = 0;
            TimedNode<T> h = head;
            while (s != Integer.MAX_VALUE) {
                TimedNode<T> next = h.get();
                if (next == null) {
                    break;
                }
                s++;
                h = next;
            }

            return s;
        }
    }
}
