package com.dtdennis.currency.util

import com.dtdennis.currency.data.util.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class AppSchedulerProvider : SchedulerProvider {
    override val io: Scheduler
        get() = Schedulers.io()
}