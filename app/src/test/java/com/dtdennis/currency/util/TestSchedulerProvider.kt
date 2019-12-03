package com.dtdennis.currency.util

import com.dtdennis.currency.data.util.SchedulerProvider
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(useTrampoline: Boolean = false) : SchedulerProvider {
    override val io = if (useTrampoline) Schedulers.trampoline()
    else TestScheduler()
}