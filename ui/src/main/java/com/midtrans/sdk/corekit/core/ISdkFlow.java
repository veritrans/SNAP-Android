package com.midtrans.sdk.corekit.core;

import static com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_FAILED;
import static com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_INVALID;
import static com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_PENDING;
import static com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_SUCCESS;
import static com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CAPTURE;
import static com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CODE_200;
import static com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CODE_201;
import static com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_SETTLEMENT;

import android.content.Context;

import androidx.annotation.NonNull;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.themes.BaseColorTheme;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.api.callback.Callback;
import com.midtrans.sdk.uikit.api.exception.SnapError;
import com.midtrans.sdk.uikit.api.model.CustomColors;
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail;
import com.midtrans.sdk.uikit.api.model.TransactionResult;
import com.midtrans.sdk.uikit.external.UiKitApi;
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader;
import com.midtrans.sdk.uikit.internal.util.UiKitConstants;

import java.lang.ref.WeakReference;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class ISdkFlow {
    public static WeakReference<TransactionFinishedCallback> transactionFinishedCallback;

    private static Callback<TransactionResult> wrapperCallback = new Callback<TransactionResult>() {
        @Override
        public void onSuccess(TransactionResult result) {
            Logger.d("Callback received Succesffully");
            deliverCallback(result);
        }

        @Override
        public void onError(@NonNull SnapError error) {
            Logger.d("Error Callback");
        }
    };

    private CustomColors customColorThemeToCustomColors(BaseColorTheme baseColorTheme) {
        return new CustomColors(baseColorTheme);
    }

    public void runUIFlow(Context context, String snapToken) {
        if (MidtransSDK.getInstance().getColorTheme() != null) {
            UiKitApi.Companion.getDefaultInstance().setCustomColors(customColorThemeToCustomColors(MidtransSDK.getInstance().getColorTheme()));
        }
        if (MidtransSDK.getInstance().getDefaultText() != null) {
            UiKitApi.Companion.getDefaultInstance().setCustomFontFamily(AssetFontLoader.INSTANCE.fontFamily(MidtransSDK.getInstance().getDefaultText(), context));
        }
        if (snapToken != null) {
            UiKitApi.Companion.getDefaultInstance().runPaymentTokenLegacy(
                    context,
                    snapToken,
                    null,
                    wrapperCallback
            );
        } else {
            TransactionRequest transactionRequest = MidtransSDK.getInstance().getTransactionRequest();
            UiKitApi.Companion.getDefaultInstance().runPaymentLegacy(
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
        if (MidtransSDK.getInstance().getColorTheme() != null) {
            UiKitApi.Companion.getDefaultInstance().setCustomColors(customColorThemeToCustomColors(MidtransSDK.getInstance().getColorTheme()));
        }
        if (MidtransSDK.getInstance().getDefaultText() != null) {
            UiKitApi.Companion.getDefaultInstance().setCustomFontFamily(AssetFontLoader.INSTANCE.fontFamily(MidtransSDK.getInstance().getDefaultText(), context));
        }
        TransactionRequest transactionRequest = MidtransSDK.getInstance().getTransactionRequest();
        UiKitApi.Companion.getDefaultInstance().runPaymentLegacy(
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

        if (transactionResult != null) {
            if (transactionResult.getStatus().contains("canceled")) {
                result = new com.midtrans.sdk.corekit.models.snap.TransactionResult(true);
            } else if (isSuccess(transactionResult.getStatus())) {
                result = new com.midtrans.sdk.corekit.models.snap.TransactionResult(new TransactionResponse(
                        UiKitConstants.STATUS_CODE_200,
                        transactionResult.getTransactionId(),
                        transactionResult.getPaymentType(),
                        STATUS_SUCCESS,
                        transactionResult.getMessage()
                ));
            } else if (isPending(transactionResult.getStatus())) {
                result = new com.midtrans.sdk.corekit.models.snap.TransactionResult(new TransactionResponse(
                        UiKitConstants.STATUS_CODE_201,
                        transactionResult.getTransactionId(),
                        transactionResult.getPaymentType(),
                        STATUS_PENDING,
                        transactionResult.getMessage()
                ));
            } else {
                result = new com.midtrans.sdk.corekit.models.snap.TransactionResult(new TransactionResponse(
                        transactionResult.getStatus(),
                        transactionResult.getTransactionId(),
                        transactionResult.getMessage(),
                        STATUS_FAILED,
                        transactionResult.getMessage()
                ));
            }
        } else {
            result = new com.midtrans.sdk.corekit.models.snap.TransactionResult(new TransactionResponse(
                    null,
                    null,
                    null,
                    STATUS_INVALID,
                    transactionResult.getMessage()
            ));
        }


        if (transactionFinishedCallback != null && transactionFinishedCallback.get() != null) {
            transactionFinishedCallback.get().onTransactionFinished(result);
        }
    }

    private static boolean isSuccess(String status) {
        boolean isSuccess;
        switch (status) {
            case STATUS_CODE_200:
            case STATUS_SUCCESS:
            case STATUS_SETTLEMENT:
            case STATUS_CAPTURE:
                isSuccess = true;
                break;
            default:
                isSuccess = false;
        }
        return isSuccess;
    }

    private static boolean isPending(String status) {
        boolean isPending;
        switch (status) {
            case STATUS_CODE_201:
            case STATUS_PENDING:
                isPending = true;
                break;
            default:
                isPending = false;
        }
        return isPending;
    }
}
