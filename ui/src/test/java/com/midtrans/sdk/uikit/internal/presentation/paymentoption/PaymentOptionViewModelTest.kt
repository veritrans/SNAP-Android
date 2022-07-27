package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import com.midtrans.sdk.uikit.external.model.PaymentList
import com.midtrans.sdk.uikit.external.model.PaymentMethod
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Assert.assertTrue
import org.junit.Test

internal class PaymentOptionViewModelTest {

    @Test
    fun whenBankTransferMethodIsInPaymentListInitiateListShouldCreateBankTransferList() {
        val list = PaymentList(
            options = listOf(
                PaymentMethod(
                    type = "permata_va",
                    category = "bank_transfer",
                    status = "up"
                ),
                PaymentMethod(
                    type = "bca_va",
                    category = "bank_transfer",
                    status = "up"
                ),
                PaymentMethod(
                    type = "bni_va",
                    category = "bank_transfer",
                    status = "up"
                )
            )
        )
        val vm = PaymentOptionViewModel()
        val result = vm.initiateList(list, true)
        assertTrue(result.paymentMethods.size == 1)
        assertThat(
            result.paymentMethods[0],
            allOf(
                hasProperty("type", equalTo("bank_transfer")),
                hasProperty("methods", equalTo(listOf("permata_va", "bca_va", "bni_va"))),
                hasProperty("icons", hasSize<List<Int>>(3))
            )
        )
    }

    @Test
    fun whenBankTransferMethodIncludeOtherVaInitiateListShouldCreateBankTransferListWithAllBankIcon() {
        val list = PaymentList(
            options = listOf(
                PaymentMethod(
                    type = "permata_va",
                    category = "bank_transfer",
                    status = "up"
                ),
                PaymentMethod(
                    type = "other_va",
                    category = "bank_transfer",
                    status = "up"
                )
            )
        )
        val vm = PaymentOptionViewModel()
        val result = vm.initiateList(list, true)
        assertTrue(result.paymentMethods.size == 1)
        assertThat(
            result.paymentMethods[0],
            allOf(
                hasProperty("type", equalTo("bank_transfer")),
                hasProperty("methods", equalTo(listOf("permata_va", "other_va"))),
                hasProperty("icons", hasSize<List<Int>>(5))
            )
        )
    }

    @Test
    fun whenSoloPaymentMethodsAreInPaymentListInitiateListShouldCreateThoseMethodsList() {
        val list = PaymentList(
            options = listOf(
                PaymentMethod(
                    type = "bca_klikbca",
                    status = "up"
                ),
                PaymentMethod(
                    type = "bca_klikpay",
                    status = "up"
                ),
                PaymentMethod(
                    type = "cimb_clicks",
                    status = "up"
                ),
                PaymentMethod(
                    type = "bri_epay",
                    status = "up"
                ),
                PaymentMethod(
                    type = "danamon_online",
                    status = "up"
                ),
                PaymentMethod(
                    type = "gopay",
                    status = "up"
                ),
                PaymentMethod(
                    type = "akulaku",
                    status = "up"
                ),
                PaymentMethod(
                    type = "uob_ezpay",
                    mode = listOf("uobweb", "uobapp"),
                    status = "up"
                )
            )
        )
        val vm = PaymentOptionViewModel()
        val result = vm.initiateList(list, true)
        assertTrue(result.paymentMethods.size == 8)
        assertThat(
            result.paymentMethods[0],
            allOf(
                hasProperty("type", equalTo("bca_klikbca")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[1],
            allOf(
                hasProperty("type", equalTo("bca_klikpay")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[2],
            allOf(
                hasProperty("type", equalTo("cimb_clicks")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[3],
            allOf(
                hasProperty("type", equalTo("bri_epay")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[4],
            allOf(
                hasProperty("type", equalTo("danamon_online")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[5],
            allOf(
                hasProperty("type", equalTo("gopay")),
                hasProperty("icons", hasSize<List<Int>>(2))
            )
        )
        assertThat(
            result.paymentMethods[6],
            allOf(
                hasProperty("type", equalTo("akulaku")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[7],
            allOf(
                hasProperty("type", equalTo("uob_ezpay")),
                hasProperty("methods", equalTo(listOf("uobweb", "uobapp"))),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
    }

    @Test
    fun whenConvenienceStorePaymentMethodIsInTheListInitiateListShouldCreateConvenienceStoreList() {
        val list = PaymentList(
            options = listOf(
                PaymentMethod(
                    type = "indomaret",
                    category = "cstore",
                    status = "up"
                ),
                PaymentMethod(
                    type = "alfamart",
                    category = "cstore",
                    status = "up"
                )
            )
        )
        val vm = PaymentOptionViewModel()
        val result = vm.initiateList(list, true)
        assertTrue(result.paymentMethods.size == 2)
        assertThat(
            result.paymentMethods[0],
            allOf(
                hasProperty("type", equalTo("indomaret")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[1],
            allOf(
                hasProperty("type", equalTo("alfamart")),
                hasProperty("icons", hasSize<List<Int>>(3))
            )
        )
    }

    @Test
    fun whenShopeepayForPhoneAndDeviceIsPhoneInitiateListShouldCreateListShopeepay() {
        val list = PaymentList(
            options = listOf(
                PaymentMethod(
                    type = "shopeepay",
                    status = "up"
                )
            )
        )
        val vm = PaymentOptionViewModel()
        val result = vm.initiateList(list, false)
        assertTrue(result.paymentMethods.size == 1)
        assertThat(
            result.paymentMethods[0],
            allOf(
                hasProperty("type", equalTo("shopeepay")),
                hasProperty("icons", hasSize<List<Int>>(2))
            )
        )
    }

    @Test
    fun whenShopeepayForPhoneAndDeviceIsTabletInitiateListShouldNotCreateList() {
        val list = PaymentList(
            options = listOf(
                PaymentMethod(
                    type = "shopeepay",
                    status = "up"
                )
            )
        )
        val vm = PaymentOptionViewModel()
        val result = vm.initiateList(list, true)
        assertTrue(result.paymentMethods.isEmpty())
    }

    @Test
    fun whenShopeepayForTabletAndDeviceIsTabletInitiateListShouldCreateList() {
        val list = PaymentList(
            options = listOf(
                PaymentMethod(
                    type = "qris",
                    acquirer = "shopeepay",
                    status = "up"
                )
            )
        )
        val vm = PaymentOptionViewModel()
        val result = vm.initiateList(list, true)
        assertTrue(result.paymentMethods.size == 1)
        assertThat(
            result.paymentMethods[0],
            allOf(
                hasProperty("type", equalTo("shopeepay")),
                hasProperty("icons", hasSize<List<Int>>(2))
            )
        )
    }

    @Test
    fun whenShopeepayForTabletAndDeviceIsPhoneInitiateListShouldNotCreateList() {
        val list = PaymentList(
            options = listOf(
                PaymentMethod(
                    type = "qris",
                    acquirer = "shopeepay",
                    status = "up"
                )
            )
        )
        val vm = PaymentOptionViewModel()
        val result = vm.initiateList(list, false)
        assertTrue(result.paymentMethods.isEmpty())
    }

    @Test
    fun initiateListShouldOnlyCreateListForPaymentWithStatusUp() {
        val list = PaymentList(
            options = listOf(
                PaymentMethod(
                    type = "bca_klikbca",
                    status = "up"
                ),
                PaymentMethod(
                    type = "bca_klikpay",
                    status = "up"
                ),
                PaymentMethod(
                    type = "cimb_clicks",
                    status = "down"
                ),
                PaymentMethod(
                    type = "bri_epay",
                    status = "up"
                ),
                PaymentMethod(
                    type = "danamon_online",
                    status = "down"
                ),
                PaymentMethod(
                    type = "gopay",
                    status = "up"
                ),
                PaymentMethod(
                    type = "akulaku",
                    status = "down"
                ),
                PaymentMethod(
                    type = "uob_ezpay",
                    mode = listOf("uobweb", "uobapp"),
                    status = "up"
                )
            )
        )
        val vm = PaymentOptionViewModel()
        val result = vm.initiateList(list, true)
        assertTrue(result.paymentMethods.size == 5)
        assertThat(
            result.paymentMethods[0],
            allOf(
                hasProperty("type", equalTo("bca_klikbca")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[1],
            allOf(
                hasProperty("type", equalTo("bca_klikpay")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[2],
            allOf(
                hasProperty("type", equalTo("bri_epay")),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
        assertThat(
            result.paymentMethods[3],
            allOf(
                hasProperty("type", equalTo("gopay")),
                hasProperty("icons", hasSize<List<Int>>(2))
            )
        )
        assertThat(
            result.paymentMethods[4],
            allOf(
                hasProperty("type", equalTo("uob_ezpay")),
                hasProperty("methods", equalTo(listOf("uobweb", "uobapp"))),
                hasProperty("icons", hasSize<List<Int>>(1))
            )
        )
    }
}