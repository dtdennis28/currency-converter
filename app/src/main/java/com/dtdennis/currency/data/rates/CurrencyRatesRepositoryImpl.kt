package com.dtdennis.currency.data.rates

import com.dtdennis.currency.core.rates.CurrencyRatesRepository
import com.dtdennis.currency.core.rates.CurrencyRatesManifest
import com.dtdennis.currency.data.rates.storage.MemCurrencyRatesStorage
import com.dtdennis.currency.data.rates.storage.PrefsCurrencyRatesStorage
import com.dtdennis.currency.data.util.Logger
import com.dtdennis.currency.data.util.SchedulerProvider
import io.reactivex.*
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

/**
 * A triply-backed repository (API, disk, memory) for fetching the latest currency rates.
 *
 * Note: Will also return the bundled "default" currency rates if no other source is available.
 * So, no error should ever be emitted from the rates-streaming.
 *
 * Note 2: Always only streams via one base currency. It does not readjust the streaming for a new
 * base currency; rather, it makes the assumption that as long as EUR = 1.0 (is the base), we can
 * convert to and from any other currency.
 */
private const val TAG = "CurrRatesRepo"
private const val SERVICE_PING_INTERVAL = 1L
private val PING_TIME_UNIT = TimeUnit.SECONDS

private const val BASE_CURRENCY = "EUR"

class CurrencyRatesRepositoryImpl(
    private val logger: Logger,
    private val schedulerProvider: SchedulerProvider,
    private val service: CurrencyRatesService,
    private val diskStorage: PrefsCurrencyRatesStorage,
    private val memoryStorage: MemCurrencyRatesStorage,
    private val defaultService: DefaultCurrencyRatesService
) : CurrencyRatesRepository {
    /**
     * Stream the rates manifest based on the base currency provided
     *
     * In order of importance, prefer:
     * - Network
     * - Memory
     * - Disk cache
     * - Default / bundled
     *
     * Always ping server every 1 sec
     * Upon each sec, if server fails,
     * attempt in-memory; if null, attempt cache,
     * if still null, use bundled rates
     */
    override fun streamRates(): Observable<CurrencyRatesManifest> {
        return Observable
            .interval(SERVICE_PING_INTERVAL, PING_TIME_UNIT, schedulerProvider.io)
            .flatMap {
                service
                    .getRates(BASE_CURRENCY)
                    .map {
                        // Insert the base rate into the manifest so that it's usable regardless of which
                        // currency is being converted from
                        val ratesWithBaseIncluded = it.rates.toMutableMap()
                        ratesWithBaseIncluded[it.base] = 1.0

                        logger.d(TAG, "$ratesWithBaseIncluded")

                        return@map it.copy(rates = ratesWithBaseIncluded)
                    }
                    .flatMap(::storeNewRates)
                    .onErrorResumeNext { error: Throwable ->
                        // "Report error"
                        logger.e(TAG, error)

                        memoryStorage.getRates()
                            .switchIfEmpty(diskStorage.getRates())
                            .switchIfEmpty(Single.just(defaultService.read()))
                    }
                    .toObservable()
            }
    }


    /**
     * Take the new rates and store them, then return a Single emitting these new rates
     */
    private fun storeNewRates(newRates: CurrencyRatesManifest): Single<CurrencyRatesManifest> {
        return diskStorage
            .setRates(newRates)
            .andThen(memoryStorage.setRates(newRates))
            .andThen(Single.just(newRates))
    }
}