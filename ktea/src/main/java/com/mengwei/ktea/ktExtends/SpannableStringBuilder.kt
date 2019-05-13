

import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.support.annotation.ColorInt
import android.text.Spannable.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.*


/**
 * Create by MengWei at 2018/7/13
 */

inline fun buildSpannedString(builderAction: SpannableStringBuilder.() -> Unit): SpannedString {
    val builder = SpannableStringBuilder()
    builder.builderAction()
    return SpannedString(builder)
}


inline fun SpannableStringBuilder.inSpans(
    vararg spans: Any,
    builderAction: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    val start = length
    builderAction()
    for (span in spans) setSpan(span, start, length, SPAN_INCLUSIVE_EXCLUSIVE)
    return this
}


inline fun SpannableStringBuilder.inSpans(
    span: Any,
    builderAction: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    val start = length
    builderAction()
    setSpan(span, start, length, SPAN_INCLUSIVE_EXCLUSIVE)
    return this
}


inline fun SpannableStringBuilder.bold(builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(StyleSpan(BOLD), builderAction = builderAction)


inline fun SpannableStringBuilder.italic(builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(StyleSpan(ITALIC), builderAction = builderAction)


inline fun SpannableStringBuilder.underline(builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(UnderlineSpan(), builderAction = builderAction)

inline fun SpannableStringBuilder.color(
        @ColorInt color: Int,
        builderAction: SpannableStringBuilder.() -> Unit
) = inSpans(ForegroundColorSpan(color), builderAction = builderAction)


inline fun SpannableStringBuilder.backgroundColor(
    @ColorInt color: Int,
    builderAction: SpannableStringBuilder.() -> Unit
) = inSpans(BackgroundColorSpan(color), builderAction = builderAction)


inline fun SpannableStringBuilder.strikeThrough(builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(StrikethroughSpan(), builderAction = builderAction)

inline fun SpannableStringBuilder.scale(
    proportion: Float,
    builderAction: SpannableStringBuilder.() -> Unit
) = inSpans(RelativeSizeSpan(proportion), builderAction = builderAction)

inline fun SpannableStringBuilder.superscript(builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(SuperscriptSpan(), builderAction = builderAction)


inline fun SpannableStringBuilder.subscript(builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(SubscriptSpan(), builderAction = builderAction)
