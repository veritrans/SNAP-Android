package com.midtrans.sdk.corekit.core;

import static com.midtrans.sdk.corekit.core.Logger.TAG;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.midtrans.sdk.uikit.SdkUIFlowBuilder;


/**
 * Created by shivam on 10/19/15.
 */
public class MidtransSDK {
    private static final String ADD_TRANSACTION_DETAILS = "Add transaction request details.";

    private static SdkUIFlowBuilder sdkBuilder;
    private static volatile MidtransSDK midtransSDK;
    private static boolean sdkNotAvailable = false;
    private TransactionRequest transactionRequest = null;
    private ISdkFlow uiflow;
    private UIKitCustomSetting uiKitCustomSetting;

    private MidtransSDK() {

    }

    private MidtransSDK(@NonNull SdkUIFlowBuilder sdkBuilder) {
        MidtransSDK.sdkBuilder = sdkBuilder;
        uiKitCustomSetting = sdkBuilder.uiKitCustomSetting;
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
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
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

    public void startPaymentUiFlow(Context context, PaymentMethod paymentMethod) {
            runDirectPaymentUiSdk(context, paymentMethod, null);
    }

    private void runDirectPaymentUiSdk(Context context, PaymentMethod paymentMethod, String snapToken) {
        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            startCreditCardUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER)) {
            startBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_BCA)) {
            startBCABankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_PERMATA)) {
            startPermataBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_MANDIRI)) {
            startMandiriBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_BNI)) {
            startBniBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_BRI)) {
            startBriBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_OTHER)) {
            startOtherBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.GO_PAY)) {
            startGoPayUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.SHOPEEPAY)) {
            startShopeePayUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BCA_KLIKPAY)) {
            startBCAKlikPayUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.KLIKBCA)) {
            startKlikBCAUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.CIMB_CLICKS)) {
            startCIMBClicksUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.EPAY_BRI)) {
            startBRIEpayUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.DANAMON_ONLINE)) {
            startDanamonOnlineUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.AKULAKU)) {
            startAkulakuUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.ALFAMART)) {
            startAlfamartUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.INDOMARET)) {
            startIndomaretUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.UOB_EZPAY)) {
            startUobEzPay(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.UOB_EZPAY_APP)) {
            startUobEzPayApp(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.UOB_EZPAY_WEB)) {
            startUobEzPayWeb(context, snapToken);
        } else {
            if (TextUtils.isEmpty(snapToken)) {
                startPaymentUiFlow(context);
            } else {
                startPaymentUiFlow(context, snapToken);
            }
        }
    }

    private void startCreditCardUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable()) {
            if (uiflow != null) {
                uiflow.runCreditCard(context, snapToken);
            }
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable()) {
            if (uiflow != null) {
                uiflow.runBankTransfer(context, snapToken);
            }
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startBCABankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runBCABankTransfer(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startPermataBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable()) {
            if (uiflow != null) {
                uiflow.runPermataBankTransfer(context, snapToken);
            }

        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startMandiriBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable()) {
            if (uiflow != null) {
                uiflow.runMandiriBankTransfer(context, snapToken);
            }

        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startBniBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable()) {
            if (uiflow != null) {
                uiflow.runBniBankTransfer(context, snapToken);
            }

        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startBriBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable()) {
            if (uiflow != null) {
                uiflow.runBriBankTransfer(context, snapToken);
            }

        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startOtherBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runOtherBankTransfer(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startGoPayUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runGoPay(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startShopeePayUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runShopeePay(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startBCAKlikPayUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runBCAKlikPay(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startKlikBCAUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runKlikBCA(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startCIMBClicksUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runCIMBClicks(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startBRIEpayUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runBRIEpay(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startDanamonOnlineUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runDanamonOnline(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startAkulakuUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runAkulaku(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startAlfamartUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runAlfamart(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startIndomaretUIFlow(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runIndomaret(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startUobEzPay(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runUobEzPay(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startUobEzPayApp(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runUobEzPayApp(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void startUobEzPayWeb(@NonNull Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runUobEzPayWeb(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    private void runUiSdk(Context context, String snapToken) {
        if (isTransactionRequestAvailable() && uiflow != null) {
            uiflow.runUIFlow(context, snapToken);
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
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
