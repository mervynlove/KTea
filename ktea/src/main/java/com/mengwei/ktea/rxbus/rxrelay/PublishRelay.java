

package com.mengwei.ktea.rxbus.rxrelay;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public final class PublishRelay<T> extends Relay<T> {

    @SuppressWarnings("rawtypes")
    private static final PublishDisposable[] EMPTY = new PublishDisposable[0];


    private final AtomicReference<PublishDisposable<T>[]> subscribers;


    public static <T> PublishRelay<T> create() {
        return new PublishRelay<T>();
    }


    @SuppressWarnings("unchecked") private PublishRelay() {
        subscribers = new AtomicReference<PublishDisposable<T>[]>(EMPTY);
    }


    @Override
    public void subscribeActual(Observer<? super T> t) {
        PublishDisposable<T> ps = new PublishDisposable<T>(t, this);
        t.onSubscribe(ps);
        add(ps);
        // if cancellation happened while a successful add, the remove() didn't work
        // so we need to do it again
        if (ps.isDisposed()) {
            remove(ps);
        }
    }


    private void add(PublishDisposable<T> ps) {
        for (;;) {
            PublishDisposable<T>[] a = subscribers.get();
            int n = a.length;
            @SuppressWarnings("unchecked")
            PublishDisposable<T>[] b = new PublishDisposable[n + 1];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = ps;

            if (subscribers.compareAndSet(a, b)) {
                return;
            }
        }
    }


    @SuppressWarnings("unchecked")
    void remove(PublishDisposable<T> ps) {
        for (;;) {
            PublishDisposable<T>[] a = subscribers.get();
            if (a == EMPTY) {
                return;
            }

            int n = a.length;
            int j = -1;
            for (int i = 0; i < n; i++) {
                if (a[i] == ps) {
                    j = i;
                    break;
                }
            }

            if (j < 0) {
                return;
            }

            PublishDisposable<T>[] b;

            if (n == 1) {
                b = EMPTY;
            } else {
                b = new PublishDisposable[n - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(a, j + 1, b, j, n - j - 1);
            }
            if (subscribers.compareAndSet(a, b)) {
                return;
            }
        }
    }

    @Override
    public void accept(T value) {
        if (value == null) throw new NullPointerException("value == null");
        for (PublishDisposable<T> s : subscribers.get()) {
            s.onNext(value);
        }
    }

    @Override
    public boolean hasObservers() {
        return subscribers.get().length != 0;
    }


    static final class PublishDisposable<T> extends AtomicBoolean implements Disposable {

        private static final long serialVersionUID = 3562861878281475070L;

        final Observer<? super T> actual;

        final PublishRelay<T> parent;


        PublishDisposable(Observer<? super T> actual, PublishRelay<T> parent) {
            this.actual = actual;
            this.parent = parent;
        }

        void onNext(T t) {
            if (!get()) {
                actual.onNext(t);
            }
        }

        @Override
        public void dispose() {
            if (compareAndSet(false, true)) {
                parent.remove(this);
            }
        }

        @Override
        public boolean isDisposed() {
            return get();
        }
    }
}
