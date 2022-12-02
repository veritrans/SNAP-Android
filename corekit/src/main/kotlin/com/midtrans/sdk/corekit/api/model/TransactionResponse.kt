package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionResponse(
    /**
     * status_code : 200 status_message : Success, Credit Card 3D Secure transaction is successful
     * transaction_id : 49ab48d4-93e1-4b52-a706-2f7a746b99d0 saved_token_id :
     * 48111119d4a368-602b-4352-a1ac-23bad256741d masked_card : 481111-1114 order_id : 109380dv0
     * gross_amount : 10000.00 payment_type : credit_card transaction_time : 2015-10-30 19:57:33
     * transaction_status : capture fraud_status : accept saved_token_id_expired_at : 2025-10-30
     * 19:57:36 approval_code : 1446209855742 secure_token : true bank : bni eci : 05
     */
    var statusCode: String? = null,
    var statusMessage: String? = null,
    var transactionId: String? = null,
    var savedTokenId: String? = null,
    var maskedCard: String? = null,
    var threeDsVersion: String? = null,
    var channelResponseCode: String? = null,
    var channelResponseMessage: String? = null,
    var cardType: String? = null,
    var orderId: String? = null,
    var grossAmount: Double? = null,
    var paymentType: String? = null,
    var chargeType: String? = null,
    var transactionTime: String? = null,
    var transactionStatus: String? = null,
    var fraudStatus: String? = null,
    var savedTokenIdExpiredAt: String? = null,
    var approvalCode: String? = null,
    var isSecureToken: Boolean = false,
    var permataVaNumber: String? = null,
    var permataExpiration: String? = null,
    var accountNumbers: List<VaNumber>? = null,
    var bcaKlikbcaExpireTime: String? = null,
    var bcaVaNumber: String? = null,
    var bcaExpiration: String? = null,
    var bniVaNumber: String? = null,
    var bniExpiration: String? = null,
    var briVaNumber: String? = null,
    var briExpiration: String? = null,

    @SerializedName("billpayment_expiration")
    var mandiriBillExpiration: String? = null,
    var xlTunaiOrderId: String? = null,
    var xlTunaiMerchantId: String? = null,
    var xlExpiration: String? = null,
    var installmentTerm: String? = null,
    var gopayExpiration: String? = null,
    var alfamartExpireTime: String? = null,
    var alfamartExpirationRaw: String? = null,
    var gopayExpirationRaw: String? = null,
    var shopeepayExpirationRaw: String? = null,
    var qrisExpirationRaw: String? = null,
    var indomaretExpireTime: String? = null,
    var indomaretExpirationRaw: String? = null,
    var redirectUrl: String? = null,
    var uobEzpayWebUrl: String? = null,
    var uobEzpayDeeplinkUrl: String? = null,
    var pdfUrl: String? = null,
    var bank: String? = null,
    var eci: String? = null,
    //for mandiri bill pay
    /**
     * bill number/code
     */
    var billKey: String? = null,

    /**
     * company or biller code.
     */
    var billerCode: String? = null,

    /**
     * payment code for Indomaret
     */
    var paymentCode: String? = null,
    var finishRedirectUrl: String? = null,
    var kiosonExpireTime: String? = null,
    var validationMessages: List<String>? = null,
    var pointBalance: Double = 0.0,
    var pointBalanceAmount: String? = null,
    var pointRedeemAmount: Double = 0.0,

    //for gopay
    var qrCodeUrl: String? = null,
    var deeplinkUrl: String? = null,

    //currently for shopeepay, could be used for other QRIS payment except gopay (gopay still using qr_code_url)
    var qrisUrl: String? = null,
    val qrisAcquirer: String? = null,
    var currency: String? = null

) : Parcelable