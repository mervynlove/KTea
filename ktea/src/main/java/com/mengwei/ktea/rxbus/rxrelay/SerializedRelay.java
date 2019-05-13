
package com.mengwei.ktea.rxbus.rxrelay;

import io.reactivex.Observer;


final class SerializedRelay<T> extends Relay<T> {

    private final Relay<T> actual;

    private boolean emitting;

    private AppendOnlyLinkedArrayList<T> queue;

    SerializedRelay(final Relay<T> actual) {
        this.actual = actual;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        actual.subscribe(observer);
    }


    @Override
    public void accept(T value) {
        synchronized (this) {
            if (emitting) {
                AppendOnlyLinkedArrayList<T> q = queue;
                if (q == null) {
                    q = new AppendOnlyLinkedArrayList<T>(4);
                    queue = q;
                }
                q.add(value);
                return;
            }
            emitting = true;
        }
        actual.accept(value);
        emitLoop();
    }

    private void emitLoop() {
        for (;;) {
            AppendOnlyLinkedArrayList<T> q;
            synchronized (this) {
                q = queue;
                if (q == null) {
                    emitting = false;
                    return;
                }
                queue = null;
            }
            q.accept(actual);
        }
    }

    @Override
    public boolean hasObservers() {
        return actual.hasObservers();
    }
}
