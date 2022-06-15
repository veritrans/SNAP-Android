package com.midtrans.sdk.corekit.internal.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import com.midtrans.sdk.corekit.BuildConfig
import com.midtrans.sdk.corekit.internal.network.restapi.SnapApi
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
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
    fun provideSnapApi(
        @Named("snap_retrofit") retrofit: Retrofit
    ): SnapApi {
        return retrofit.create(SnapApi::class.java)
    }

    @Provides
    @Singleton
    @Named("snap_retrofit")
    fun provideRetrofit(
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
        connectionPool: ConnectionPool
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(chuckInterceptor)
            .build()

    }

    @Provides
    @Singleton
    fun provideChuckInterceptor(context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideConnectionPool(): ConnectionPool {
        return ConnectionPool(
            MAX_IDLE_CONNECTIONS,
            KEEP_ALIVE_DURATION, TimeUnit.MILLISECONDS
        )
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .create()
    }

    companion object {
        private const val READ_TIME_OUT = 120
        private const val WRITE_TIME_OUT = 120
        private const val CONNECTION_TIME_OUT = 30
        private const val KEEP_ALIVE_DURATION = (30 * 1000).toLong()
        private const val MAX_IDLE_CONNECTIONS = 10
    }
}