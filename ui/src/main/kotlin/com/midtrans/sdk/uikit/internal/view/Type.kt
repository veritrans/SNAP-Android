package com.midtrans.sdk.uikit.internal.view

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.midtrans.sdk.uikit.R

@OptIn(ExperimentalTextApi::class)
private fun getGoogleFontFamily(
    name: String,
    provider: GoogleFont.Provider = googleFontProvider,
    weights: List<FontWeight>
): FontFamily {
    return FontFamily(
        weights.map {
            Font(GoogleFont(name), provider, it)
        }
    )
}

@OptIn(ExperimentalTextApi::class)
private val googleFontProvider: GoogleFont.Provider by lazy {
    GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
}

@OptIn(ExperimentalTextApi::class)
fun getPoppinsFontFamily(): FontFamily {
    return getGoogleFontFamily(
        name = "Poppins",
        weights = listOf(
            FontWeight.Normal,
            FontWeight.Bold,
            FontWeight.ExtraLight,
            FontWeight.SemiBold
        )
    )
}

@OptIn(ExperimentalTextApi::class)
fun getRobotoFontFamily(): FontFamily {
    return getGoogleFontFamily(
        name = "Roboto",
        weights = listOf(
            FontWeight.Normal,
            FontWeight.Bold,
            FontWeight.ExtraLight,
            FontWeight.SemiBold
        )
    )
}

val AppFontTypography = Typography(
    defaultFontFamily = getPoppinsFontFamily()
)

/**
 * Converts a [Spanned] into an [AnnotatedString] trying to keep as much formatting as possible.
 *
 * Currently supports `bold`, `italic`, `underline` and `color`.
 */
fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    val spanList = getSpans(0, spanned.length, Any::class.java)
    spanList.forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> {
                when (span.style) {
                    Typeface.BOLD -> addStyle(
                        SpanStyle(fontWeight = FontWeight.Bold),
                        start,
                        end
                    )
                    Typeface.ITALIC -> addStyle(
                        SpanStyle(fontStyle = FontStyle.Italic),
                        start,
                        end
                    )
                    Typeface.BOLD_ITALIC -> addStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        ), start, end
                    )
                }
            }

            is UnderlineSpan -> {

                addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)

            }
            is ForegroundColorSpan -> {

                addStyle(SpanStyle(color = Color(span.foregroundColor)), start, end)

            }

        }
    }
}

