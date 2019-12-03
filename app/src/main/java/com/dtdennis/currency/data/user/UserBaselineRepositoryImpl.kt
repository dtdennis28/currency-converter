package com.dtdennis.currency.data.user

import com.dtdennis.currency.core.user.UserBaseline
import com.dtdennis.currency.core.user.UserBaselineInteractor
import com.dtdennis.currency.core.user.UserBaselineRepository
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Prefs & memory backed implementation of [UserBaselineRepository]
 */
class UserBaselineRepositoryImpl(
    private val storage: UserBaselineStorage
) : UserBaselineRepository {
    // Keep memory ref for quicker access
    private var baseline: UserBaseline? = null

    override fun set(userBaseline: UserBaseline): Completable =
        Completable.fromAction {
            this.baseline = userBaseline
            storage.setBaseline(userBaseline)
        }

    override fun get(): Single<UserBaseline> =
        Single.fromCallable {
            if (baseline != null) baseline
            else storage.getBaseline(UserBaselineInteractor.DEFAULT_BASELINE)
        }
}