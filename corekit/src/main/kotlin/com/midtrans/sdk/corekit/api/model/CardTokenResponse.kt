package com.midtrans.sdk.corekit.api.model

data class CardTokenResponse (
    /**
     * statusMessage : OK, success request new token bank : bni statusCode : 200 tokenId :
     * 481111-1114-0452c0cb-3199-4081-82ba-2e05b378c0ca redirectUrl : https://api.sandbox.veritrans.co
     * .id/v2/token/redirect/481111-1114-0452c0cb-3199-4081-82ba-2e05b378c0ca
     */
    var statusCode: String?,
    var statusMessage: String?,
    var bank: String?,
    var tokenId: String?,
    var redirectUrl: String?,
    var hash: String?
)