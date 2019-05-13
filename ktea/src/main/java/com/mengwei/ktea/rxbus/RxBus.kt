package com.mengwei.ktea.rxbus

import com.mengwei.ktea.rxbus.rxrelay.PublishRelay
import com.mengwei.ktea.rxbus.rxrelay.Relay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.ConcurrentHashMap


/**
 * Create by MengWei at 2018/8/17
 */
class RxBus
private constructor() {

    private var bus: Relay<Any> = PublishRelay.create<Any>().toSerialized()
    private val mStickyEventMap: MutableMap<Class<*>, Any> = ConcurrentHashMap()

    companion object {

        val BUS by lazy {
            RxBus()
        }
    }

    fun post(event: Any) {
        bus.accept(event)
    }

    fun postSticky(event: Any) {
        synchronized(mStickyEventMap) {
            mStickyEventMap.put(event.javaClass, event)
        }
        bus.accept(event)
    }

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return bus.ofType(eventType)
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    fun <T> toObservableSticky(eventType: Class<T>): Observable<T> {
        synchronized(mStickyEventMap) {
            val observable = bus.ofType(eventType)
            val event = mStickyEventMap[eventType]

            return if (event != null) {
                observable.mergeWith(Observable.create { it.onNext(eventType.cast(event)) })
            } else {
                observable
            }
        }
    }

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    fun <T> removeStickyEvent(eventType: Class<T>): T {
        synchronized(mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType))
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    fun removeAllStickyEvents() {
        synchronized(mStickyEventMap) {
            mStickyEventMap.clear()
        }
    }

    fun unregister(disposable: Disposable?) {
        disposable?.run {
            if (!isDisposed) dispose()
        }
    }


}