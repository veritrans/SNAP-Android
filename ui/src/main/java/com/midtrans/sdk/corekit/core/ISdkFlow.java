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
                    transactionRequest.getItemDetails(),
                    transactionRequest.getCreditCard(),
                    transactionRequest.getCustomerDetails().getCustomerIdentifier(),
                    new PaymentCallback(""),
                    wrapperCallback,
                    transactionRequest.getExpiry(),
                    null,
                    transactionRequest.getEnabledPayments(),
                    transactionRequest.getPermataVa(),
                    transactionRequest.getBcaVa(),
                    transactionRequest.getBniVa(),
                    transactionRequest.getBriVa()
            );
        }
    }

    public void runCreditCard(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.CREDIT_CARD, null),
                transactionRequest.getEnabledPayments(),
                null,
                null,
                null,
                null
        );
    }

    public void runBankTransfer(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runBCABankTransfer(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.BCA_VA),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runPermataBankTransfer(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.PERMATA_VA),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runMandiriBankTransfer(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.E_CHANNEL),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runBniBankTransfer(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.BNI_VA),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runBriBankTransfer(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.BRI_VA),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runOtherBankTransfer(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.OTHER_VA),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runGoPay(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.GOPAY, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runShopeePay(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.SHOPEEPAY, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runBCAKlikPay(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.BCA_KLIKPAY, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runKlikBCA(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.KLIK_BCA, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.CIMB_CLICKS, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runBRIEpay(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.BRI_EPAY, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runDanamonOnline(Context context, String tokenToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.DANAMON_ONLINE, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.INDOMARET, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    @Deprecated
    public void runKioson(Context context, String snapToken) {

    }

    @Deprecated
    public void runGci(Context context, String snapToken) {

    }

    public void runAkulaku(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.AKULAKU, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
        );
    }

    public void runAlfamart(Context context, String snapToken) {
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
                new PaymentCallback(""),
                wrapperCallback,
                transactionRequest.getExpiry(),
                new PaymentTypeItem(PaymentType.ALFAMART, null),
                transactionRequest.getEnabledPayments(),
                transactionRequest.getPermataVa(),
                transactionRequest.getBcaVa(),
                transactionRequest.getBniVa(),
                transactionRequest.getBriVa()
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
