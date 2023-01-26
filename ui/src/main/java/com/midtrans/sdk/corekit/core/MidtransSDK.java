package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.themes.BaseColorTheme;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.R;

import java.lang.ref.WeakReference;


/**
 * Created by shivam on 10/19/15.
 */
public class MidtransSDK {
    private static final String ADD_TRANSACTION_DETAILS = "Add transaction request details.";

    private static SdkUIFlowBuilder sdkBuilder;
    private static volatile MidtransSDK midtransSDK;
    private static boolean sdkNotAvailable = false;
    private TransactionRequest transactionRequest = null;
    private String merchantServerUrl = null;
    private BaseColorTheme colorTheme;
    private ISdkFlow uiflow;
    private UIKitCustomSetting uiKitCustomSetting;
    public static WeakReference<TransactionFinishedCallback> transactionFinishedCallback = ISdkFlow.transactionFinishedCallback;

    private MidtransSDK() {

    }

    private MidtransSDK(@NonNull SdkUIFlowBuilder sdkBuilder) {
        MidtransSDK.sdkBuilder = sdkBuilder;
        uiKitCustomSetting = sdkBuilder.uiKitCustomSetting;
        merchantServerUrl = sdkBuilder.merchantServerUrl;
    }

    /**
     * Set transaction information that you want to execute.
     *
     * @param transactionRequest transaction request  object
     */
    public void setTransactionRequest(TransactionRequest transactionRequest) {

        if (transactionRequest != null) {
            this.transactionRequest = transactionRequest;
        } else {
            Logger.e(ADD_TRANSACTION_DETAILS);
        }
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    public void setColorTheme(CustomColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public BaseColorTheme getColorTheme() {
        return colorTheme;
    }

    /**
     * This will start actual execution of transaction. if you have enabled an ui then it will start
     * activity according to it.
     *
     * @param context current activity.
     */
    public void startPaymentUiFlow(Context context) {
        if (merchantBaseUrlAvailable(context)) {
            runUiSdk(context, null);
        }
    }

    public void startPaymentUiFlow(Context context, String snapToken) {
        if (snapTokenAvailable(context, snapToken)) {
            runUiSdk(context, snapToken);
        }
    }

    public void startPaymentUiFlow(Context context, PaymentMethod paymentMethod) {
        if (merchantBaseUrlAvailable(context)) {
            runDirectPaymentUiSdk(context, paymentMethod, null);
        }
    }

    public void startPaymentUiFlow(Context context, PaymentMethod paymentMethod, String snapToken) {
        if (snapTokenAvailable(context, snapToken)) {
            runDirectPaymentUiSdk(context, paymentMethod, snapToken);
        }
    }

    private void runDirectPaymentUiSdk(Context context, PaymentMethod paymentMethod, String snapToken) {
        startDirectPayment(context, snapToken, paymentMethod);
    }

    private void startDirectPayment(Context context, String snapToken, PaymentMethod paymentMethod) {
        if (isTransactionRequestAvailable()) {
            if (uiflow != null) {
                uiflow.runDirectPayment(context, snapToken, paymentMethod);
            }
        } else {
            Logger.e(ADD_TRANSACTION_DETAILS);
        }
    }

    private boolean snapTokenAvailable(Context context, String snapToken) {
        if (TextUtils.isEmpty(snapToken)) {
            String message = context.getString(R.string.invalid_snap_token);
            Logger.e(message);

            if (transactionFinishedCallback != null) {
                transactionFinishedCallback.get().onTransactionFinished(new TransactionResult(TransactionResult.STATUS_INVALID, message));
            }

            return false;
        }
        return true;
    }

    private boolean merchantBaseUrlAvailable(Context context) {
        if (TextUtils.isEmpty(merchantServerUrl)) {
            String message = context.getString(R.string.invalid_merchant_base_url);
            Logger.e(message);

            if (transactionFinishedCallback != null) {
                transactionFinishedCallback.get().onTransactionFinished(new TransactionResult(TransactionResult.STATUS_INVALID, message));
            }

            return false;
        }
        return true;
    }

    private void runUiSdk(Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runUIFlow(context, snapToken);
        } else {
            Logger.e(ADD_TRANSACTION_DETAILS);
        }
    }

    private boolean isTransactionRequestAvailable() {
        return transactionRequest != null;
    }


    /**
     * get Veritrans SDK instance
     *
     * @param newSdkBuilder SDK Coreflow Builder
     */
    public static MidtransSDK delegateInstance(@NonNull SdkUIFlowBuilder newSdkBuilder) {
        if (newSdkBuilder != null) {
            midtransSDK = new MidtransSDK(newSdkBuilder);
            sdkBuilder = newSdkBuilder;
            midtransSDK.uiflow = new ISdkFlow();
        } else {
            Logger.e("sdk is not initialized.");
        }

        return midtransSDK;
    }

    public UIKitCustomSetting getUIKitCustomSetting() {
        if (this.uiKitCustomSetting == null) {
            this.uiKitCustomSetting = new UIKitCustomSetting();
        }
        return uiKitCustomSetting;
    }

    public void setUiKitCustomSetting(UIKitCustomSetting uiKitCustomSetting) {
        this.uiKitCustomSetting = uiKitCustomSetting;
    }


    /**
     * Returns instance of midtrans sdk.
     *
     * @return MidtransSDK instance
     */
    public synchronized static MidtransSDK getInstance() {
        if (midtransSDK == null) {
            synchronized (MidtransSDK.class) {
                if (midtransSDK == null) {
                    if (sdkBuilder != null) {
                        midtransSDK = new MidtransSDK(sdkBuilder);
                        sdkNotAvailable = false;
                    } else {
                        midtransSDK = new MidtransSDK();
                        sdkNotAvailable = true;
                    }
                }
            }
        } else {
            sdkNotAvailable = false;
        }

        return midtransSDK;
    }

    public boolean isSdkNotAvailable() {
        return sdkNotAvailable;
    }

}
