package com.dtdennis.currency.ui.rx

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter

class UserChangesFlowableWrapper<T : Any>(initialValue: T? = null) {
    private lateinit var emitter: FlowableEmitter<T>

    var latest: T? = initialValue
        private set

    val flowable = Flowable.create<T>({ emitter ->
        this.emitter = emitter

        if(latest != null) {
            emitter.onNext(latest!!)
        }
    }, BackpressureStrategy.LATEST)


    fun update(userChange: T) {
        emitter.onNext(userChange)
    }
}