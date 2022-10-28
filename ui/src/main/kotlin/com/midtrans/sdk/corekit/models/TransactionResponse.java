package com.midtrans.sdk.corekit.models;

import android.text.TextUtils;

import com.midtrans.sdk.uikit.api.model.TransactionResult;

/**
 * contains information about payment charge api call like, </p> status message, status code,
 * transaction id, transaction status etc.
 * <p>
 * Created by chetan on 30/10/15.
 */
public class TransactionResponse {
    /**
     * status_code : 200 status_message : Success, Credit Card 3D Secure transaction is successful
     * transaction_id : 49ab48d4-93e1-4b52-a706-2f7a746b99d0 saved_token_id :
     * 48111119d4a368-602b-4352-a1ac-23bad256741d masked_card : 481111-1114 order_id : 109380dv0
     * gross_amount : 10000.00 payment_type : credit_card transaction_time : 2015-10-30 19:57:33
     * transaction_status : capture fraud_status : accept saved_token_id_expired_at : 2025-10-30
     * 19:57:36 approval_code : 1446209855742 secure_token : true bank : bni eci : 05
     */

    private String statusCode;
    private String transactionId;

    private String paymentType;
    private String transactionStatus;


    public TransactionResponse() { }

    public TransactionResponse(
            String statusCode,
            String transactionId,
            String paymentType,
            String transactionStatus
    ) {
        this.statusCode = statusCode;
        this.transactionId = transactionId;
        this.paymentType = paymentType;
        this.transactionStatus = transactionStatus;
    }

    public TransactionResponse(TransactionResult transactionResult){
        this.statusCode = "200";
        this.transactionId = transactionResult.getTransactionId();
        this.paymentType = transactionResult.getPaymentType();
        this.transactionStatus = transactionResult.getStatus();
    }


    public String getStatusCode() {
        return TextUtils.isEmpty(statusCode) ? "" : statusCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPaymentType() {
        return TextUtils.isEmpty(paymentType) ? "" : paymentType;
    }



    public String getTransactionStatus() {
        return TextUtils.isEmpty(transactionStatus) ? "" : transactionStatus;
    }

}