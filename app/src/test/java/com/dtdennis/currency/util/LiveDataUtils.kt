package com.dtdennis.currency.util

import androidx.lifecycle.*

/**
 * https://gist.github.com/cdmunoz/3d20720b145bfedc13bd2a76f7f2bf70#file-onetimeobserver-kt
 */
class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
    private val lifecycle = LifecycleRegistry(this)

    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun onChanged(t: T) {
        handler(t)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}

/**
 * https://gist.github.com/cdmunoz/63ee9004cf9f1bbf4c50dbc63939ee45#file-testextensions-kt
 */
fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}