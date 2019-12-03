package com.dtdennis.currency.data.util

import io.reactivex.Scheduler

/**
 * Simple interface to provide common schedulers.
 *
 * Helpful with DI / testing.
 */
interface SchedulerProvider {
    val io: Scheduler
}