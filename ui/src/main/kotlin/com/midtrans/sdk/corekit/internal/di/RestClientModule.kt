package com.midtrans.sdk.corekit.internal.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import com.midtrans.sdk.uikit.BuildConfig
import com.midtrans.sdk.corekit.internal.network.MerchantInterceptor
import com.midtrans.sdk.corekit.internal.network.SnapRequestInterceptor
import com.midtrans.sdk.corekit.internal.network.restapi.CoreApi
import com.midtrans.sdk.corekit.internal.network.restapi.MerchantApi
import com.midtrans.sdk.corekit.internal.network.restapi.SnapApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class RestClientModule {

    @Provides
    @Singleton
    fun provideMerchantApi(
        @Named("merchant_retrofit") retrofit: Retrofit
    ): MerchantApi {
        return retrofit.create(MerchantApi::class.java)
    }

    @Provides
    @Singleton
    @Named("merchant_retrofit")
    fun provideMerchantRetrofit(
        gson: Gson,
        @Named("merchant_client") httpClient: OkHttpClient,
        @Named("merchant_url") merchantUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(merchantUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("merchant_client")
    fun provideMerchantOkHttpClient(
        chuckInterceptor: ChuckerInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        merchantInterceptor: MerchantInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(chuckInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(merchantInterceptor)
            .build()
    }

    @Provides
    fun provideMerchantInterceptor(
        @Named("merchant_client_key") clientKey: String
    ): MerchantInterceptor {
        return MerchantInterceptor(clientKey)
    }

    @Provides
    @Singleton
    fun provideSnapApi(
        @Named("snap_retrofit") retrofit: Retrofit
    ): SnapApi {
        return retrofit.create(SnapApi::class.java)
    }

    @Provides
    @Singleton
    @Named("snap_retrofit")
    fun provideSnapRetrofit(
        gson: Gson,
        @Named("snap_client") httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SNAP_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("snap_client")
    fun provideSnapOkHttpClient(
        chuckInterceptor: ChuckerInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        snapRequestInterceptor: SnapRequestInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(chuckInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(snapRequestInterceptor)
            .build()
    }

    @Provides
    fun provideSnapRequestInterceptor(): SnapRequestInterceptor {
        return SnapRequestInterceptor()
    }

    @Provides
    @Singleton
    fun provideCoreApi(
        @Named("core_retrofit") retrofit: Retrofit
    ): CoreApi {
        return retrofit.create(CoreApi::class.java)
    }

    @Provides
    @Singleton
    @Named("core_retrofit")
    fun provideCoreApiRetrofit(
        gson: Gson,
        @Named("core_client") httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.CORE_API_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    @Named("core_client")
    fun provideCoreApiOkHttpClient(
        chuckInterceptor: ChuckerInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(chuckInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    }

    @Provides
    fun provideChuckInterceptor(context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context).build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(@Named("enable_log") enableLog: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(if (enableLog) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .create()
    }

    companion object {
        private const val READ_TIME_OUT = 120
        private const val WRITE_TIME_OUT = 120
        private const val CONNECTION_TIME_OUT = 30
    }
}