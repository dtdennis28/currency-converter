package com.dtdennis.revolut.data.util

import io.reactivex.Scheduler

interface SchedulerProvider {
    val io: Scheduler
}