package com.dtdennis.currency.util

import com.dtdennis.currency.data.util.SchedulerProvider
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider : SchedulerProvider {
    override val io = TestScheduler()
}