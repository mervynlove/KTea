

package com.mengwei.ktea.rxbus.rxrelay;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


public abstract class Relay<T> extends Observable<T> implements Consumer<T> {

    @Override public abstract void accept(T value); // Redeclare without checked exception.


    public abstract boolean hasObservers();


    public final Relay<T> toSerialized() {
        if (this instanceof SerializedRelay) {
            return this;
        }
        return new SerializedRelay<T>(this);
    }
}
