package com.midtrans.sdk.corekit

import android.annotation.SuppressLint
import android.content.Context
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionRequest
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.di.DaggerSnapComponent
import com.midtrans.sdk.corekit.internal.di.SnapComponent
import com.midtrans.sdk.corekit.internal.scheduler.SdkScheduler
import com.midtrans.sdk.corekit.internal.usecase.BankTransferUsecase
import com.midtrans.sdk.corekit.internal.util.NetworkUtil
import javax.inject.Inject

class SnapCore private constructor(builder: Builder) {

    @Inject
    internal lateinit var bankTransferUsecase: BankTransferUsecase

    init {
        buildDaggerComponent(builder.context).inject(this)
    }

    fun hello(): String {
        return "hello snap"
    }

    @SuppressLint("CheckResult")
    fun paymentUsingBankTransfer(
        snapToken: String,
        @PaymentType.Def paymentType: String,
        email: String? = null,
        callback: Callback<TransactionResponse>
    ) {
        bankTransferUsecase.charge(snapToken, paymentType, email)
            .subscribeOn(SdkScheduler().io())
            .observeOn(SdkScheduler().ui())
            .subscribe(
                {
                    callback.onSuccess(it)
                },
                {
                    callback.onError(SnapError(cause = it))
                }
            )

    }

    companion object {
        private var INSTANCE: SnapCore? = null

        internal fun buildDaggerComponent(applicationContext: Context): SnapComponent {
            return DaggerSnapComponent.builder()
                .applicationContext(applicationContext)
                .build()
        }

        fun getInstance(): SnapCore? = INSTANCE
    }


    class Builder() {
        internal lateinit var context: Context

        fun withContext(context: Context) = apply {
            this.context = context
        }

        @Throws(RuntimeException::class)
        fun build(): SnapCore {
            INSTANCE = SnapCore(this)
            return INSTANCE!!
        }
    }

    //Todo: this pattern seems anti pattern.
//    private fun isTransactionRequestAvailable(): Boolean {
//        return transactionRequest != null || !TextUtils.isEmpty(authenticationToken)
//    }
}