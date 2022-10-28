package com.midtrans.sdk.uikit;

import android.content.Context;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.IScanner;
import com.midtrans.sdk.corekit.core.ISdkFlow;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.BaseColorTheme;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class SdkUIFlowBuilder {
    /**
     * It  will initialize an data required to sdk.
     *
     * @param context application context
     */

    public static final String CORE_FLOW = "core";
    public static final String UI_FLOW = "ui";
    public static final String WIDGET = "widget";

    protected String clientKey = null;
    protected Context context = null;
    protected boolean enableLog = false;
    protected boolean enableBuiltInTokenStorage = true;
    protected String merchantServerUrl = null;
    protected String merchantName = null;
    protected ISdkFlow sdkFlow;
    protected String defaultText;
    protected String boldText;
    protected String semiBoldText;
    protected ArrayList<PaymentMethodsModel> selectedPaymentMethods;
    protected IScanner externalScanner;
    protected String flow;
    protected BaseColorTheme colorTheme;
    protected String languageCode = "en";
    protected UIKitCustomSetting uiKitCustomSetting;

    private SdkUIFlowBuilder(Context context, String clientKey, String merchantServerUrl, TransactionFinishedCallback callback) {
        this.context = context;
        this.clientKey = clientKey;
        this.merchantServerUrl = merchantServerUrl;
        ISdkFlow.transactionFinishedCallback = new WeakReference<>(callback);
        this.flow = UI_FLOW;
        this.sdkFlow = new ISdkFlow();
    }

    private SdkUIFlowBuilder() {
        this.flow = UI_FLOW;
        this.sdkFlow = new ISdkFlow();
    }

    /**
     * create sdk builder
     *
     * @return SdkUIFlowBuilder
     */
    public static SdkUIFlowBuilder init() {
        return new SdkUIFlowBuilder();
    }

    /**
     * This Sdk builder has been deprecated since version 1.10, please use init() method instead
     *
     * @param context
     * @param clientKey
     * @param merchantServerUrl
     * @param callback
     * @return
     */
    @Deprecated
    public static SdkUIFlowBuilder init(Context context, String clientKey, String merchantServerUrl, TransactionFinishedCallback callback) {
        return new SdkUIFlowBuilder(context, clientKey, merchantServerUrl, callback);

    }

    public SdkUIFlowBuilder setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    public SdkUIFlowBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public SdkUIFlowBuilder setTransactionFinishedCallback(TransactionFinishedCallback callback) {
        ISdkFlow.transactionFinishedCallback = new WeakReference<>(callback);
        return this;
    }

    public SdkUIFlowBuilder setMerchantBaseUrl(String merchantBaseUrl) {
        this.merchantServerUrl = merchantBaseUrl;
        return this;
    }

    public SdkUIFlowBuilder setExternalScanner(IScanner externalScanner) {
        this.externalScanner = externalScanner;
        return this;
    }

    public SdkUIFlowBuilder setBoldText(String boldText) {
        this.boldText = boldText;
        return this;
    }

    public SdkUIFlowBuilder setSemiBoldText(String semiBoldText) {
        this.semiBoldText = semiBoldText;
        return this;
    }

    public SdkUIFlowBuilder setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        return this;
    }

    public SdkUIFlowBuilder enableLog(boolean enableLog) {
        this.enableLog = enableLog;
        return this;
    }

    public SdkUIFlowBuilder setSelectedPaymentMethods(ArrayList<PaymentMethodsModel> selectedPaymentMethods) {
        this.selectedPaymentMethods = selectedPaymentMethods;
        return this;
    }

    public SdkUIFlowBuilder useBuiltInTokenStorage(boolean enableBuiltInTokenStorage) {
        this.enableBuiltInTokenStorage = enableBuiltInTokenStorage;
        return this;
    }

    public SdkUIFlowBuilder setUIkitCustomSetting(UIKitCustomSetting setting) {
        this.uiKitCustomSetting = setting;
        return this;
    }

    public SdkUIFlowBuilder setColorTheme(CustomColorTheme customColorTheme) {
        this.colorTheme = customColorTheme;
        return this;
    }

    public SdkUIFlowBuilder setLanguage(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }
}
