package com.midtrans.sdk.corekit.internal.di

import com.midtrans.sdk.corekit.internal.data.repository.CoreApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.MerchantApiRepository
import com.midtrans.sdk.corekit.internal.network.restapi.SnapApi
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.network.restapi.CoreApi
import com.midtrans.sdk.corekit.internal.network.restapi.MerchantApi
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

    @Provides
    @Singleton
    fun provideCardTokenRepository(
        coreApi: CoreApi
    ): CoreApiRepository {
        return CoreApiRepository(coreApi)
    }

    @Provides
    @Singleton
    fun provideMerchantApiRepository(
        merchantApi: MerchantApi
    ): MerchantApiRepository {
        return MerchantApiRepository(merchantApi)
    }
}