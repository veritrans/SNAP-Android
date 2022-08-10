package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import com.midtrans.sdk.corekit.api.model.Address
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.PaymentMethod
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BANK_TRANSFER
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.CREDIT_CARD
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.KLIK_BCA
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.SHOPEEPAY
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.SHOPEEPAY_QRIS
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.beans.HasPropertyWithValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class PaymentOptionViewModelTest {
    private lateinit var viewModel: PaymentOptionViewModel

    @Before
    fun setUp() {
        viewModel = PaymentOptionViewModel()
    }

    @Test
    fun getCustomerInfoWhenInputNullShouldReturnNull() {
        val result = viewModel.getCustomerInfo(null)
        assertNull(result)
    }

    @Test
    fun getCustomerInfoWhenFirstNameOnlyShouldPutFirstNameAsName() {
        val result = viewModel.getCustomerInfo(
            CustomerDetails(
                firstName = "first name"
            )
        )
        assertNotNull(result)
        assertEquals("first name", result?.name)
    }

    @Test
    fun getCustomerInfoWhenLastNameOnlyShouldPutLastNameAsName() {
        val result = viewModel.getCustomerInfo(
            CustomerDetails(
                lastName = "last name"
            )
        )
        assertNotNull(result)
        assertEquals("last name", result?.name)
    }

    @Test
    fun getCustomerInfoWhenFirstNameAndLastNameShouldPutBothAsName() {
        val result = viewModel.getCustomerInfo(
            CustomerDetails(
                firstName = "first name",
                lastName = "last name"
            )
        )
        assertNotNull(result)
        assertEquals("first name last name", result?.name)
    }

    @Test
    fun getCustomerInfoWhenShippingAddressIsProvidedShouldCreateAddressLines() {
        var result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    address = "street name",
                    city = "city name",
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("street name", "city name", "postal code"),
                hasSize(3)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    city = "city name",
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("city name", "postal code"),
                hasSize(2)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("postal code"),
                hasSize(1)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    postalCode = ""
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines, hasSize(0)
        )
    }

    @Test
    fun getCustomerInfoWhenBillingAddressIsProvidedShouldCreateAddressLines() {
        var result = viewModel.getCustomerInfo(
            CustomerDetails(
                billingAddress = Address(
                    address = "street name",
                    city = "city name",
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("street name", "city name", "postal code"),
                hasSize(3)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                billingAddress = Address(
                    city = "city name",
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("city name", "postal code"),
                hasSize(2)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                billingAddress = Address(
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines,
            allOf(
                hasItems("postal code"),
                hasSize(1)
            )
        )

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                billingAddress = Address(
                    postalCode = ""
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines, hasSize(0)
        )
    }

    @Test
    fun getCustomerInfoWhenNoAddressIsProvidedShouldReturnEmptyList() {
        val result = viewModel.getCustomerInfo(CustomerDetails())
        assertNotNull(result)
        assertTrue(result!!.addressLines.isEmpty())
    }

    @Test
    fun getCustomerInfoWhenPhoneNumberIsProvidedShouldReturnPhoneNumber() {
        var result = viewModel.getCustomerInfo(
            CustomerDetails(
                phone = "0888887777777"
            )
        )
        assertNotNull(result)
        assertEquals("0888887777777", result!!.phone)

        result = viewModel.getCustomerInfo(CustomerDetails())
        assertNotNull(result)
        assertEquals("", result!!.phone)
    }

    @Test
    fun initiateListWhenPaymentListIsEmptyShouldReturnEmptyList() {
        val result = viewModel.initiateList(emptyList(), true)
        assertTrue(result.paymentMethods.isEmpty())
    }

    @Test
    fun initiateListWhenDeviceIsTabletShouldReturnListForTablet() {
        val result = viewModel.initiateList(providePaymentMethodList(), true).paymentMethods
        assertTrue(result.size == 4)
        assertThat(
            result,
            hasItems(
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(BANK_TRANSFER)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(5))
                ),
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(KLIK_BCA)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(1))
                ),
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(CREDIT_CARD)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(4))
                ),
                allOf(
                    HasPropertyWithValue("type", equalTo(SHOPEEPAY_QRIS)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(2))
                )
            )
        )
    }

    @Test
    fun initiateListWhenDeviceIsPhoneShouldReturnListForPhone() {
        val result = viewModel.initiateList(providePaymentMethodList(), false).paymentMethods
        assertTrue(result.size == 4)
        assertThat(
            result,
            hasItems(
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(BANK_TRANSFER)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(5))
                ),
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(KLIK_BCA)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(1))
                ),
                allOf(
                    HasPropertyWithValue.hasProperty("type", equalTo(CREDIT_CARD)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(4))
                ),
                allOf(
                    HasPropertyWithValue("type", equalTo(SHOPEEPAY)),
                    HasPropertyWithValue.hasProperty("icons", hasSize<String>(2))
                )
            )
        )
    }

    private fun providePaymentMethodList(): List<PaymentMethod> {
        return listOf(
            PaymentMethod(
                type = BANK_TRANSFER,
                channels = listOf(PaymentType.PERMATA_VA, PaymentType.E_CHANNEL, PaymentType.ALL_VA)
            ),
            PaymentMethod(
                type = KLIK_BCA,
                channels = emptyList()
            ),
            PaymentMethod(
                type = CREDIT_CARD,
                channels = emptyList()
            ),
            PaymentMethod(
                type = SHOPEEPAY_QRIS,
                channels = emptyList()
            ),
            PaymentMethod(
                type = SHOPEEPAY,
                channels = emptyList()
            )
        )
    }
}