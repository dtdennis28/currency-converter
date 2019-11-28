package com.dtdennis.revolut.util

import com.dtdennis.revolut.data.util.SchedulerProvider
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider : SchedulerProvider {
    override val io = TestScheduler()
}