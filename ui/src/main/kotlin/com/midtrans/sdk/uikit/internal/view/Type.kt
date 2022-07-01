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
    var normalStart = 0
    var normalEnd = 0
    val spanList = getSpans(0, spanned.length, Any::class.java)
    spanList.forEach { span ->
        val start = getSpanStart(span)
        normalEnd = start
//        append(spanned.toString().substring(normalStart, normalEnd))
        val end = getSpanEnd(span)
        normalStart = end
        val text = spanned.toString().substring(start, end)
        when (span) {
            is StyleSpan -> {
                append(buildAnnotatedString {
                    append(text)
                    when (span.style) {
                        Typeface.BOLD -> addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold),
                            0,
                            text.length
                        )
                        Typeface.ITALIC -> addStyle(
                            SpanStyle(fontStyle = FontStyle.Italic),
                            0,
                            text.length
                        )
                        Typeface.BOLD_ITALIC -> addStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            ), 0, text.length
                        )
                    }
                })

            }
            is UnderlineSpan -> {
                append(buildAnnotatedString {
                    append(text)
                    addStyle(SpanStyle(textDecoration = TextDecoration.Underline), 0, text.length)
                })
            }
            is ForegroundColorSpan -> {
                append(buildAnnotatedString {
                    append(text)
                    addStyle(SpanStyle(color = Color(span.foregroundColor)), 0, text.length)
                })
            }
            is BulletSpan -> {
                val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))
                append(buildAnnotatedString {
                    withStyle(style = paragraphStyle){
                        append("1. ")
                        append("")
                        append(text)
                    }

                    withStyle(style = paragraphStyle){
                        append("1. ")
                        append("")
                        append(text)
                    }

                })

            }
        }
    }
}


class ssss() {
    @Composable
    fun qqq() {
        val bullet = "\u2022"
        val messages = listOf(
            "Hey This is first paragraph",
            "Hey this is my second paragraph. Any this is 2nd line.",
            "Hey this is 3rd paragraph."
        )

        val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))
        Text(
            buildAnnotatedString {
                messages.forEach {
                    withStyle(style = paragraphStyle) {
                        append(bullet)
                        append("\t\t")
                        append(it)
                    }
                }
            }
        )
    }
}
