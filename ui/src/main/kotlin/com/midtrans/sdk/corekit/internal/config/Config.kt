package com.midtrans.sdk.corekit.internal.config

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.midtrans.sdk.corekit.internal.data.sharedpref.SharedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

internal class Config(private val sharedData: SharedData) {

    companion object {
        @JvmStatic
        @Volatile
        private var INSTANCE: Config? = null

        private const val CONFIG_DATA_STORE_NAME: String = "CoreKitDataStore"

        fun getInstance(context: Context): Config {
            return if (INSTANCE == null) {
                val datastore = PreferenceDataStoreFactory.create(
                    corruptionHandler = null,
                    migrations = listOf(),
                    scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
                ) {
                    context.applicationContext.preferencesDataStoreFile(CONFIG_DATA_STORE_NAME)
                }
                val sharedData = SharedData(dataStrore = datastore)
                Config(sharedData)
            } else INSTANCE!!
        }
    }

    init {
        INSTANCE = this
    }
}