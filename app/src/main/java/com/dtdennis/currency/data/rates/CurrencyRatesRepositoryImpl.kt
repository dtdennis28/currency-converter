package com.dtdennis.currency.data.rates

import com.dtdennis.currency.core.rates.CurrencyRatesRepository
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.dtdennis.currency.data.rates.storage.MemCurrencyRatesStorage
import com.dtdennis.currency.data.rates.storage.PrefsCurrencyRatesStorage
import com.dtdennis.currency.data.util.Logger
import com.dtdennis.currency.data.util.SchedulerProvider
import io.reactivex.*
import java.util.concurrent.TimeUnit

private const val TAG = "CurrRatesRepo"
private const val SERVICE_PING_INTERVAL = 1L
private val PING_TIME_UNIT = TimeUnit.SECONDS

class CurrencyRatesRepositoryImpl(
    private val logger: Logger,
    private val schedulerProvider: SchedulerProvider,
    private val service: CurrencyRatesService,
    private val diskStorage: PrefsCurrencyRatesStorage,
    private val memoryStorage: MemCurrencyRatesStorage
) : CurrencyRatesRepository {
    /**
     * Stream the rates manifest based on the base currency provided
     *
     * In order of importance, prefer:
     * - Network
     * - Cache
     * - Memory
     *
     * Start w/ in memory or
     * Always ping server every 1 sec
     * Upon each sec, if server fails,
     * attempt in-memory; if null, attempt cache,
     * if still null, throw error but keep trying
     */
    override fun streamRates(baseCurrency: String): Flowable<CurrencyRatesManifest> {
        return Flowable
            .interval(SERVICE_PING_INTERVAL, PING_TIME_UNIT, schedulerProvider.io)
            .flatMapSingle {
                service.getRates(baseCurrency)
            }
            .flatMap(::storeNewRates)
            .onErrorResumeNext { error: Throwable ->
                // "Report error"
                logger.e(TAG, error)

                memoryThenStorageRates(baseCurrency).toFlowable()
            }
    }

    /**
     * Take the new rates and store them, then return a flowable emitting these new rates
     */
    private fun storeNewRates(newRates: CurrencyRatesManifest): Flowable<CurrencyRatesManifest> {
        return diskStorage
            .setRates(newRates)
            .andThen(memoryStorage.setRates(newRates))
            .andThen(Flowable.just(newRates))
    }

    private fun currentRates(base: String) =
        memoryStorage
            .getRates()
            .filter {
                it.base == base
            }

    private fun storageRates(base: String) =
        diskStorage
            .getRates()
            .filter {
                it.base == base
            }

    private fun memoryThenStorageRates(base: String): Maybe<CurrencyRatesManifest> {
        return currentRates(base)
            .switchIfEmpty(storageRates(base))
    }
}