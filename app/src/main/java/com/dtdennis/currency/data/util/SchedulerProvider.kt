package com.dtdennis.currency.data.util

import io.reactivex.Scheduler

interface SchedulerProvider {
    val io: Scheduler
}