package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.core.themes.BaseColorTheme
import com.midtrans.sdk.uikit.internal.util.HslConverter

data class CustomColors(
    val backgroundBorderSolidSecondary: Int? = null,
    val backgroundBorderSolidPrimary: Int? = null,
    val backgroundFillPrimary: Int? = null,
    val backgroundFillSecondary: Int? = null,
    val backgroundFillHighlight: Int? = null,
    val backgroundFillBrand: Int? = null,
    val backgroundFillInverse: Int? = null,
    val backgroundFillLight: Int? = null,
    val textPrimary: Int? = null,
    val textSecondary: Int? = null,
    val textMuted: Int? = null,
    val textInverse: Int? = null,
    val textBrand: Int? = null,
    val textDisabled: Int? = null,
    val iconPrimary: Int? = null,
    val iconInverse: Int? = null,
    val iconMuted: Int? = null,
    val iconBrand: Int? = null,
    val iconDisabled: Int? = null,
    val link: Int? = null,
    val linkHover: Int? = null,
    val linkActive: Int? = null,
    val linkVisited: Int? = null,
    val interactiveFillBrandDefault: Int? = null,
    val interactiveFillBrandHover: Int? = null,
    val interactiveFillBrandActive: Int? = null,
    val interactiveFillDefault: Int? = null,
    val interactiveFillHover: Int? = null,
    val interactiveFillActive: Int? = null,
    val interactiveBorderInput: Int? = null,
    val interactiveBorderDotted: Int? = null,
    val interactiveDisabled: Int? = null,
    val interactiveFocus: Int? = null,
    val interactiveBorderSupport: Int? = null,
    val interactiveBorderAction: Int? = null,
    val interactiveFillInverse: Int? = null,
    val supportDangerDefault: Int? = null,
    val supportDangerHover: Int? = null,
    val supportDangerActive: Int? = null,
    val supportDangerFill: Int? = null,
    val supportWarningDefault: Int? = null,
    val supportWarningHover: Int? = null,
    val supportWarningActive: Int? = null,
    val supportWarningFill: Int? = null,
    val supportSuccessDefault: Int? = null,
    val supportSuccessHover: Int? = null,
    val supportSuccessActive: Int? = null,
    val supportSuccessFill: Int? = null,
    val supportInfoDefault: Int? = null,
    val supportInfoHover: Int? = null,
    val supportInfoActive: Int? = null,
    val supportInfoFill: Int? = null,
    val supportNeutralFill: Int? = null,
    val transparent: Int? = null,
    val overlayBlack: Int? = null,
    val overlayWhite: Int? = null,
    val lineLightMuted: Int? = null
){
    constructor(baseColorTheme: BaseColorTheme) : this(
        interactiveFillInverse = baseColorTheme.primaryColor,
        supportNeutralFill = HslConverter.addSaturation(
            HslConverter.addBrightness(
                baseColorTheme.primaryColor,
                0.38f
            ), -0.3f
        ),
        backgroundBorderSolidPrimary = baseColorTheme.primaryColor,
        textPrimary = baseColorTheme.primaryDarkColor,
        interactiveBorderInput = baseColorTheme.primaryColor,
        textMuted = baseColorTheme.secondaryColor,
        lineLightMuted = HslConverter.addSaturation(
            HslConverter.addBrightness(
                baseColorTheme.primaryColor,
                0.2f
            ), -0.2f
        )
    )

    constructor(customColorTheme: CustomColorTheme) : this(
        interactiveFillInverse = customColorTheme.getPrimaryColor(),
        supportNeutralFill = HslConverter.addSaturation(
            HslConverter.addBrightness(
                customColorTheme.getPrimaryColor(),
                0.38f
            ), -0.3f
        ),
        backgroundBorderSolidPrimary = customColorTheme.getPrimaryColor(),
        textPrimary = customColorTheme.getPrimaryDarkColor(),
        interactiveBorderInput = customColorTheme.getPrimaryColor(),
        textMuted = customColorTheme.getSecondaryColor(),
        lineLightMuted = HslConverter.addSaturation(
            HslConverter.addBrightness(
                customColorTheme.getPrimaryColor(),
                0.2f
            ), -0.2f
        )
    )
}