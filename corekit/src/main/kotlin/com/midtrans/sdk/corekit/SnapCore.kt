package com.midtrans.sdk.corekit

import android.content.Context

class SnapCore private constructor(builder: Builder) {

    init {

    }

    fun hello(): String {
        return "hello snap"
    }

    companion object {
        private var INSTANCE: SnapCore? = null

    }


    class Builder() {
        private lateinit var context: Context

        fun withContext(context: Context) = apply {
            this.context = context
        }

        @Throws(RuntimeException::class)
        fun build(): SnapCore {
            return SnapCore(this)
        }
    }
}