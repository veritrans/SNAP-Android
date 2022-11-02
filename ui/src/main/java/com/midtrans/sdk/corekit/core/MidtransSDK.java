package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;


/**
 * Created by shivam on 10/19/15.
 */
public class MidtransSDK {

    private static SdkUIFlowBuilder sdkBuilder;
    private static volatile MidtransSDK midtransSDK;
    private static boolean sdkNotAvailable = false;
    private TransactionRequest transactionRequest = null;
    private ISdkFlow uiflow;

    private MidtransSDK() {

    }

    private MidtransSDK(@NonNull SdkUIFlowBuilder sdkBuilder) {
        MidtransSDK.sdkBuilder = sdkBuilder;
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
//            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * This will start actual execution of transaction. if you have enabled an ui then it will start
     * activity according to it.
     *
     * @param context current activity.
     */
    public void startPaymentUiFlow(Context context) {

        runUiSdk(context, null);

    }

    public void startPaymentUiFlow(Context context, String snapToken) {
            runUiSdk(context, snapToken);

    }

    private void runUiSdk(Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runUIFlow(context, snapToken);
        } else {
//            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
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
//            Logger.e("sdk is not initialized.");
        }

        return midtransSDK;
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