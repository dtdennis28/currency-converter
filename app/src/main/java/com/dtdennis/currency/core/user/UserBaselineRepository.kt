package com.dtdennis.currency.core.user

import io.reactivex.Completable
import io.reactivex.Single

/**
 * Represents a storage of the user's baseline selection
 *
 * E.g. which currency the user has selected to convert from
 */
interface UserBaselineRepository {
    fun set(userBaseline: UserBaseline): Completable

    /**
     * Should never error. Should return a default baseline
     */
    fun get(): Single<UserBaseline>
}