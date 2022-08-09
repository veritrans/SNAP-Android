package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import com.midtrans.sdk.corekit.api.model.Address
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.external.model.PaymentList
import com.midtrans.sdk.uikit.external.model.PaymentMethod
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
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
            result!!.addressLines, hasItems("street name", "city name", "postal code")
        )
        assertTrue(result.addressLines.size == 3)

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
            result!!.addressLines, hasItems("city name", "postal code")
        )
        assertTrue(result.addressLines.size == 2)

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines, hasItems("postal code")
        )
        assertTrue(result.addressLines.size == 1)
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
            result!!.addressLines, hasItems("street name", "city name", "postal code")
        )
        assertTrue(result.addressLines.size == 3)

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
            result!!.addressLines, hasItems("city name", "postal code")
        )
        assertTrue(result.addressLines.size == 2)

        result = viewModel.getCustomerInfo(
            CustomerDetails(
                shippingAddress = Address(
                    postalCode = "postal code"
                )
            )
        )
        assertNotNull(result)
        assertThat(
            result!!.addressLines, hasItems("postal code")
        )
        assertTrue(result.addressLines.size == 1)
    }

    @Test
    fun getCustomerInfoWhenNoAddressIsProvidedShouldReturnEmptyList() {
        val result = viewModel.getCustomerInfo(CustomerDetails())
        assertNotNull(result)
        assertTrue(result!!.addressLines.isEmpty())
    }

    @Test
    fun getCustomerInfoWhenPhoneNumberIsProvidedShouldReturnPhoneNumber() {
        val result = viewModel.getCustomerInfo(CustomerDetails(
            phone = "0888887777777"
        ))
        assertNotNull(result)
        assertEquals("0888887777777", result!!.phone)
    }
}