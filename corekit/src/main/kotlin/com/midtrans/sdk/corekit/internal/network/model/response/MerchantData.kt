package com.midtrans.sdk.corekit.internal.network.model.response

/**
 * @author rakawm
 */
class MerchantData     constructor (){
    var  preference: MerchantPreferences? = null
        get() {
            return field
        }
        public set(preference) {
            field = preference
        }
    @com.google.gson.annotations.SerializedName("client_key")
    var  clientKey: kotlin.String? = null
        get() {
            return field
        }
        public set(clientKey) {
            field = clientKey
        }
    @com.google.gson.annotations.SerializedName("enabled_principles")
    var  enabledPrinciples: kotlin.collections.List<kotlin.String>? = null
        get() {
            return field
        }
        public set(enabledPrinciples) {
            field = enabledPrinciples
        }
    @com.google.gson.annotations.SerializedName("point_banks")
    var  pointBanks: java.util.ArrayList<kotlin.String>? = null
        get() {
            return field
        }
        public set(pointBanks) {
            field = pointBanks
        }
    @com.google.gson.annotations.SerializedName("merchant_id")
    var  merchantId: kotlin.String? = null
        get() {
            return field
        }
        public set(merchantId) {
            field = merchantId
        }
    @com.google.gson.annotations.SerializedName("acquiring_banks")
    var  acquiringBanks: kotlin.collections.List<kotlin.String>? = null
        get() {
            return field
        }
        public set(acquiringBanks) {
            field = acquiringBanks
        }
    @com.google.gson.annotations.SerializedName("priority_card_feature")
    var  priorityCardFeature: kotlin.String? = null
        get() {
            return field
        }
        public set(priorityCardFeature) {
            field = priorityCardFeature
        }
    @com.google.gson.annotations.SerializedName("recurring_mid_is_active")
    var  recurringMidIsActive: kotlin.Boolean? = null
        get() {
            return field
        }
        public set(recurringMidIsActive) {
            field = recurringMidIsActive
        }
}
