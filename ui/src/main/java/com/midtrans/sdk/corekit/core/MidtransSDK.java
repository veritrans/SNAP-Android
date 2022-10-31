package com.midtrans.sdk.corekit.core;

import androidx.annotation.NonNull;

import com.midtrans.sdk.uikit.SdkUIFlowBuilder;


/**
 * Created by shivam on 10/19/15.
 */
public class MidtransSDK {

    private static SdkUIFlowBuilder sdkBuilder;
    private static volatile MidtransSDK midtransSDK;
    private static boolean sdkNotAvailable = false;

    private MidtransSDK() {

    }

    private MidtransSDK(@NonNull SdkUIFlowBuilder sdkBuilder) {
        MidtransSDK.sdkBuilder = sdkBuilder;
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
