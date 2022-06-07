package com.midtrans.sdk.corekit.internal.usecase

import android.content.Context
import android.os.RemoteException
import java.io.IOException
import java.io.InputStream

internal class AssetReader(private val context: Context) {
    @Throws(RemoteException::class)
    fun readAssetsFile(fileName: String): ByteArray {
        var input: InputStream? = null
        try {
            input = context.assets.open(fileName)
            val buffer = ByteArray(input!!.available())
            val size = input.read(buffer)
            if (size == -1) {
                throw RemoteException("Read File Failed")
            }
            return buffer
        } catch (e: IOException) {
            throw RemoteException(e.message)
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {

                }

            }
        }
    }

    fun readAssetsFileAsInputStream(fileName: String): InputStream {
        return context.assets.open(fileName)
    }
}