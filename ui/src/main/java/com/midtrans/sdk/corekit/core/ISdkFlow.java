package com.midtrans.sdk.corekit.core;

import android.content.Context;
import androidx.annotation.NonNull;
import com.midtrans.sdk.corekit.api.model.PaymentType;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.api.callback.Callback;
import com.midtrans.sdk.uikit.api.exception.SnapError;
import com.midtrans.sdk.uikit.api.model.PaymentCallback;
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail;
import com.midtrans.sdk.uikit.api.model.TransactionResult;
import com.midtrans.sdk.uikit.external.UiKitApi;
import com.midtrans.sdk.uikit.internal.model.PaymentTypeItem;
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
                    transactionRequest.getCreditCard(),
                    transactionRequest.getCustomerDetails().getCustomerIdentifier(),
                    new PaymentCallback(""),
                    wrapperCallback,
                    transactionRequest.getExpiry(),
                    null

            );

        }
    }

    public void runCreditCard(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.CREDIT_CARD, null),
                wrapperCallback
        );
    }

    public void runBankTransfer(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, null),
                wrapperCallback
        );
    }

    public void runPermataBankTransfer(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.PERMATA_VA),
                wrapperCallback
        );
    }

    public void runMandiriBankTransfer(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.E_CHANNEL),
                wrapperCallback
        );
    }

    public void runBniBankTransfer(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.BNI_VA),
                wrapperCallback
        );
    }

    public void runBriBankTransfer(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.BRI_VA),
                wrapperCallback
        );
    }

    public void runBCABankTransfer(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.BCA_VA),
                wrapperCallback
        );
    }

    public void runOtherBankTransfer(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.OTHER_VA),
                wrapperCallback
        );
    }

    public void runGoPay(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.GOPAY, null),
                wrapperCallback
        );
    }

    public void runShopeePay(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.SHOPEEPAY, null),
                wrapperCallback
        );
    }

    public void runBCAKlikPay(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.BCA_KLIKPAY, null),
                wrapperCallback
        );
    }

    public void runKlikBCA(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.KLIK_BCA, null),
                wrapperCallback
        );
    }

    @Deprecated
    public void runMandiriClickpay(Context context, String snapToken) {
        //depreciated
    }

    @Deprecated
    public void runMandiriECash(Context context, String snapToken) {
        //depreciated
    }

    public void runCIMBClicks(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.CIMB_CLICKS, null),
                wrapperCallback
        );
    }

    public void runBRIEpay(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.BRI_EPAY, null),
                wrapperCallback
        );
    }

    @Deprecated
    public void runTelkomselCash(Context context, String snapToken) {
       //depreciated
    }

    @Deprecated
    public void runIndosatDompetku(Context context, String snapToken) {

    }

    @Deprecated
    public void runXlTunai(Context context, String snapToken) {

    }

    public void runIndomaret(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.CSTORE, PaymentType.INDOMARET),
                wrapperCallback
        );
    }

    @Deprecated
    public void runKioson(Context context, String snapToken) {

    }

    @Deprecated
    public void runGci(Context context, String snapToken) {

    }

    @Deprecated
    public void runDanamonOnline(Context context, String tokenToken) {

    }

    public void runAkulaku(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.AKULAKU, null),
                wrapperCallback
        );
    }

    public void runAlfamart(Context context, String snapToken) {
        UiKitApi.Companion.getDefaultInstance().startPayment(
                context,
                snapToken,
                new PaymentTypeItem(PaymentType.CSTORE, PaymentType.ALFAMART),
                wrapperCallback
        );
    }

    public void runCardRegistration(Context context, Object callback) {
//        Intent intent = new Intent(context, CardRegistrationActivity.class);
//        context.startActivity(intent);
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
