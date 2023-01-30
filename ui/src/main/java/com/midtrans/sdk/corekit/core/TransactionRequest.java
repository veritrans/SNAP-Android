package com.midtrans.sdk.corekit.core;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import com.midtrans.sdk.corekit.models.*;
import com.midtrans.sdk.corekit.models.snap.*;
import java.util.ArrayList;
import java.util.List;

/**
 * It contains information about transaction like {@literal orderId}, {@literal amount},
 * itemDetails
 * <p/>
 * Created by shivam on 11/5/15.
 */
public class TransactionRequest {

    private String currency = "IDR";
    /**
     * payment method using which user wants to perform transaction. use payment methods from {@link
     * }
     */
    protected int paymentMethod = -1;

    /**
     * unique order id to identify this transaction. <p/>Using this id later u can check status of
     * transaction.
     */
    private String orderId = null;

    /**
     * amount to charge customer.
     */
    private Double amount = 0.0;

    /**
     * It helps to identify whether to execute transaction in secure manner or not.
     */
    private boolean isSecureCard = true;

    private String cardClickType;

    private boolean promoEnabled;
    private List<String> promoCodes;


    /**
     * list of purchased items.
     */
    private ArrayList<ItemDetails> itemDetails = new ArrayList<>();

    /**
     * contains details about customer
     */
    private CustomerDetails mCustomerDetails = null;

    /**
     * contains user app deeplink for merchant app
     */
    private Gopay gopay;
    private Shopeepay shopeepay;
    private UobEzpay uobEzpay;

    private boolean useUi = true;
    private CreditCard creditCard;
    private ExpiryModel expiry;

    private String customField1;
    private String customField2;
    private String customField3;

    private List<String> enabledPayments;

    private PermataBankTransferRequestModel permataVa;
    private BcaBankTransferRequestModel bcaVa;
    private BankTransferRequestModel bniVa;
    private BankTransferRequestModel briVa;

    /**
     * @param orderId  order id of transaction.
     * @param amount   amount to charge.
     * @param currency currency
     */
    public TransactionRequest(@NonNull String orderId, @NonNull Double amount, @NonNull String currency) {

        if (!TextUtils.isEmpty(orderId) && amount > 0) {
            this.orderId = orderId;
            this.amount = amount;
            this.currency = currency;
        } else {
            Logger.e("Invalid transaction data.");
        }
    }


    /**
     * @param orderId order id of transaction.
     * @param amount  amount to charge.
     */
    public TransactionRequest(@NonNull String orderId, double amount) {

        if (!TextUtils.isEmpty(orderId) && amount > 0) {
            this.orderId = orderId;
            this.amount = amount;
            this.currency = "IDR";
            this.paymentMethod = -1; //Constants.PAYMENT_METHOD_NOT_SELECTED;
        } else {
            Logger.e("Invalid transaction data.");
        }
    }


    public CustomerDetails getCustomerDetails() {
        return mCustomerDetails;
    }

    public void setCustomerDetails(@NonNull CustomerDetails customerDetails) {
        mCustomerDetails = customerDetails;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isSecureCard() {
        return isSecureCard;
    }

    public String getCardClickType() {
        return cardClickType;
    }


    public ArrayList<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ArrayList<ItemDetails> itemDetails) {
        this.itemDetails = itemDetails;
    }

    /**
     * It will help to enable/disable default ui provided by sdk. By default it is true, set it to
     * false to use your own ui to show transaction.
     *
     * @param enableUi is UI mode enabled
     */
    protected void enableUi(boolean enableUi) {
        this.useUi = enableUi;
    }

    protected boolean isUiEnabled() {
        return useUi;
    }

    /**
     * It is used in case of payment using credit card.
     * this method being deprecated since v1.9.x, please don't implement this method
     *
     * @param clickType    use click type from Constants.
     * @param isSecureCard is secure
     */
    @Deprecated
    public void setCardPaymentInfo(String clickType, boolean isSecureCard) {
        Logger.i("clicktype:" + clickType + ",isSecured:" + isSecureCard);
        this.cardClickType = clickType;
        this.isSecureCard = isSecureCard;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public ExpiryModel getExpiry() {
        return expiry;
    }

    public void setExpiry(ExpiryModel expiry) {
        this.expiry = expiry;
    }

    public String getCustomField1() {
        return customField1;
    }

    public void setCustomField1(String customField1) {
        this.customField1 = customField1;
    }

    public String getCustomField2() {
        return customField2;
    }

    public void setCustomField2(String customField2) {
        this.customField2 = customField2;
    }

    public String getCustomField3() {
        return customField3;
    }

    public void setCustomField3(String customField3) {
        this.customField3 = customField3;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public List<String> getEnabledPayments() {
        return enabledPayments;
    }

    public void setEnabledPayments(List<String> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }
    public PermataBankTransferRequestModel getPermataVa() {
        return permataVa;
    }

    public void setPermataVa(PermataBankTransferRequestModel permataVa) {
        this.permataVa = permataVa;
    }

    public BcaBankTransferRequestModel getBcaVa() {
        return bcaVa;
    }

    public void setBcaVa(BcaBankTransferRequestModel bcaVa) {
        this.bcaVa = bcaVa;
    }

    public BankTransferRequestModel getBniVa() {
        return bniVa;
    }

    public void setBniVa(BankTransferRequestModel bniVa) {
        this.bniVa = bniVa;
    }

    public BankTransferRequestModel getBriVa() {
        return briVa;
    }

    public void setBriVa(BankTransferRequestModel briVa) {
        this.briVa = briVa;
    }

    public Gopay getGopay() {
        return gopay;
    }

    public void setGopay(Gopay gopay) {
        this.gopay = gopay;
    }

    public Shopeepay getShopeepay() {
        return shopeepay;
    }

    public void setShopeepay(Shopeepay shopeepay) {
        this.shopeepay = shopeepay;
    }

    public UobEzpay getUobEzpay() {
        return uobEzpay;
    }

    public void setUobEzpay(UobEzpay uobEzpay) {
        this.uobEzpay = uobEzpay;
    }
}
