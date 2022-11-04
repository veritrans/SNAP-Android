package com.midtrans.sdk.corekit.core;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import com.midtrans.sdk.corekit.models.*;
import com.midtrans.sdk.corekit.models.snap.*;
import java.util.ArrayList;
import java.util.Currency;
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


    private boolean useUi = true;
    private CreditCard creditCard;
    private ExpiryModel expiry;

    private List<String> enabledPayments;

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
//            Logger.e("Invalid transaction data.");
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
//            Logger.e("Invalid transaction data.");
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
//        Logger.i("clicktype:" + clickType + ",isSecured:" + isSecureCard);
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

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

}
