package com.dtdennis.currency.core.rates

import com.dtdennis.currency.util.mothers.CurrencyRatesManifestMother
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

/**
 * The class-under-test is really simple. Just putting a simple test in here that will break
 * once functionality of the class changes
 */
class CurrencyRatesInteractorTest {
    private val testManifest = CurrencyRatesManifestMother.defaultTestCurrencyRatesManifest()

    private val testRepo = DummyCurrencyRatesRepository(testManifest)

    private lateinit var interactor: CurrencyRatesInteractor

    @Before
    fun setUp() {
        // Default repo state to a valid response
        testRepo.setManifest(testManifest)
        interactor = CurrencyRatesInteractor(testRepo)
    }

    @Test
    fun ShouldNot_ManipulateRepoResponse_When_GetRates() {
        // Given a repo that has a rates manifest available
        // When request rates
        // Then expect the same exact manifest
        interactor
            .streamRates()
            .test()
            .assertValue(testManifest)
    }

    @Test
    fun Should_PropagateError_When_RepoFails() {
        // Given a repo that will fail upon request for rates
        testRepo.setManifest(null)

        // When request rates
        // Then expect an error to propagate
        interactor
            .streamRates()
            .test()
            .assertError(Throwable::class.java)
    }
}

class DummyCurrencyRatesRepository(private var manifest: CurrencyRatesManifest? = null) :
    CurrencyRatesRepository {
    override fun streamRates(): Observable<CurrencyRatesManifest> {
        return Single.create<CurrencyRatesManifest> { emitter ->
            if (manifest !== null) {
                emitter.onSuccess(manifest!!)
            } else {
                emitter.onError(Error("No manifest available"))
            }
        }.toObservable()
    }

    fun setManifest(manifest: CurrencyRatesManifest? = null) {
        this.manifest = manifest
    }
}