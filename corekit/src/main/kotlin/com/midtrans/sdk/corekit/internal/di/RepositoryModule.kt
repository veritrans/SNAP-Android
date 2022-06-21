package com.midtrans.sdk.corekit.internal.di

import com.midtrans.sdk.corekit.internal.network.restapi.SnapApi
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class RepositoryModule {

    @Provides
    @Singleton
    fun provideSnapRepository(
        snapApi: SnapApi
    ): SnapRepository {
        return SnapRepository(snapApi)
    }
}