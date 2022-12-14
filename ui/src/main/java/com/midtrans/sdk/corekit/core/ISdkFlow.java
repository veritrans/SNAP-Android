package com.midtrans.sdk.corekit.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.api.callback.Callback;
import com.midtrans.sdk.uikit.api.exception.SnapError;
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail;
import com.midtrans.sdk.uikit.api.model.TransactionResult;
import com.midtrans.sdk.uikit.external.UiKitApi;

import java.lang.ref.WeakReference;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class ISdkFlow {
    public static WeakReference<TransactionFinishedCallback> transactionFinishedCallback;

    private static Callback<TransactionResult> wrapperCallback = new Callback<TransactionResult>() {
        @Override
        public void onSuccess(TransactionResult result) {
            deliverCallback(result);
        }

        @Override
        public void onError(@NonNull SnapError error) {

        }
    };

    public void runUIFlow(Context context, String snapToken) {
        if (snapToken != null) {
            UiKitApi.Companion.getDefaultInstance().startPayment(
                    context,
                    snapToken,
                    null,
                    wrapperCallback
            );
        } else {
            TransactionRequest transactionRequest = MidtransSDK.getInstance().getTransactionRequest();
            UiKitApi.Companion.getDefaultInstance().startPayment(
                    context,
                    new SnapTransactionDetail(
                            transactionRequest.getOrderId(),
                            transactionRequest.getAmount(),
                            transactionRequest.getCurrency()
                    ),
                    transactionRequest.getCustomerDetails(),
                    transactionRequest.getItemDetails(),
                    transactionRequest.getCreditCard(),
                    transactionRequest.getCustomerDetails().getCustomerIdentifier(),
                    transactionRequest.getUobEzpay(),
                    wrapperCallback,
                    transactionRequest.getExpiry(),
                    null,
                    transactionRequest.getEnabledPayments(),
                    transactionRequest.getPermataVa(),
                    transactionRequest.getBcaVa(),
                    transactionRequest.getBniVa(),
                    transactionRequest.getBriVa(),
                    transactionRequest.getGopay(),
                    transactionRequest.getShopeepay(),
                    transactionRequest.getCustomField1(),
                    transactionRequest.getCustomField2(),
                    transactionRequest.getCustomField3()
            );
        }
    }

    public void runDirectPayment(Context context, String snapToken, PaymentMethod paymentMethod) {
        TransactionRequest transactionRequest = MidtransSDK.getInstance().getTransactionRequest();
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                new SnapTransactionDetail(
                        transactionRequest.getOrderId(),
                        transactionRequest.getAmount(),
                        transactionRequest.getCurrency()
                ),
                transactionRequest.getCustomerDetails(),
                transactionRequest.getItemDetails(),
                transactionRequest.getCreditCard(),
                transactionRequest.getCustomerDetails().getCustomerIdentifier(),
                transactionRequest.getUobEzpay(),
                wrapperCallback,
                transactionRequest.getExpiry(),
                paymentMethod,
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa(),
                transactionRequest.getGopay(),
                transactionRequest.getShopeepay(),
                transactionRequest.getCustomField1(),
                transactionRequest.getCustomField2(),
                transactionRequest.getCustomField3()
        );
    }

    private static void deliverCallback(TransactionResult transactionResult) {
        com.midtrans.sdk.corekit.models.snap.TransactionResult result;

        if (transactionResult.getStatus().contains("canceled")) {
            result = new com.midtrans.sdk.corekit.models.snap.TransactionResult(true);
        } else {
            result = new com.midtrans.sdk.corekit.models.snap.TransactionResult(new TransactionResponse(transactionResult));
        }

        if (transactionFinishedCallback != null && transactionFinishedCallback.get() != null) {
            transactionFinishedCallback.get().onTransactionFinished(result);
        }
    }
}
