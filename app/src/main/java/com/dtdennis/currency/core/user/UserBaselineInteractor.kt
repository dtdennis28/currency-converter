package com.dtdennis.currency.core.user

import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Use case for setting and getting the user-preferred base currency
 */
class UserBaselineInteractor @Inject constructor(private val repository: UserBaselineRepository) {
    companion object {
        /**
         * Default baseline that will be returned when none has been set
         */
        val DEFAULT_BASELINE = UserBaseline(
            "EUR",
            "Euro",
            1.0,
            mapOf(
                "EUR" to 0
            )
        )
    }

    fun setUserBaseline(userBaseline: UserBaseline): Completable = repository.set(userBaseline)
    fun getUserBaseline(): Single<UserBaseline> = repository.get()
}