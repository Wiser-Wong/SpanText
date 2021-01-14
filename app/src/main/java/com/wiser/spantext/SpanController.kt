package com.wiser.spantext

import android.graphics.Color
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.SubscriptSpan
import android.view.View
import android.widget.TextView

class SpanController {

    companion object {

        fun create(): SpanBuilder {
            return SpanBuilder()
        }

        class SpanBuilder {

            private var spanStrBuilder: SpannableStringBuilder? = null

            private var content: String? = ""

            init {
                spanStrBuilder = SpannableStringBuilder("")
            }

            /**
             * 添加文字片段
             */
            fun addContent(content: String?): SpanBuilder {
                this.content = content
                spanStrBuilder?.append(content)
                return this
            }

            private fun addCertainSection(
                section: String?,
                span: Any?,
                flag: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                onClickSpanListener: OnClickSpanListener? = null
            ): SpanBuilder {
                if (TextUtils.isEmpty(section)) return this
                val length = section?.length ?: 0
                val index: Int = if (TextUtils.isEmpty(content)) {
                    spanStrBuilder?.append(section)
                    spanStrBuilder?.indexOf(section.toString()) ?: -1

                } else {
                    content?.indexOf(section.toString()) ?: -1
                }
                if (index >= 0) {
                    // 颜色
                    spanStrBuilder?.setSpan(
                        span,
                        index,
                        index + length,
                        flag
                    )
                    // 点击
                    spanStrBuilder?.setSpan(
                        object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                onClickSpanListener?.onClickSpan(widget)
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                super.updateDrawState(ds)
                                ds.isUnderlineText = false
                                ds.color = ds.linkColor
                            }
                        },
                        index,
                        index + length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                return this
            }

            private fun addCertainSection(
                targetSection: String?,
                section: String?,
                span: Any?,
                flag: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            ): SpanBuilder {
                if (TextUtils.isEmpty(section)) return this
                val length = section?.length ?: 0
                val targetLength = targetSection?.length ?: 0
                val index: Int = if (TextUtils.isEmpty(content)) {
                    spanStrBuilder?.append(section)
                    spanStrBuilder?.indexOf(section.toString()) ?: -1

                } else {
                    content?.indexOf(section.toString()) ?: -1
                }
                if (index >= 0) {
                    spanStrBuilder?.insert(index + length, targetSection)
                    // 颜色
                    spanStrBuilder?.setSpan(
                        span,
                        index + length,
                        index + length + targetLength,
                        flag
                    )
                }
                return this
            }

            /**
             * 添加文字片段
             */
            fun addSection(section: String?): SpanBuilder {
                spanStrBuilder?.append(section)
                return this
            }

            /**
             * 添加带前景色的文字片段
             */
            fun addForeColorSection(
                section: String?,
                color: Int,
                onClickSpanListener: OnClickSpanListener? = null
            ): SpanBuilder {
                return addCertainSection(
                    section, ForegroundColorSpan(
                        color
                    ), onClickSpanListener = onClickSpanListener
                )
            }

            /**
             * 添加带背景色的文字片段
             */
            fun addBackColorSection(
                section: String?, color: Int,
                onClickSpanListener: OnClickSpanListener? = null
            ): SpanBuilder {
                return addCertainSection(
                    section, BackgroundColorSpan(
                        color
                    ), onClickSpanListener = onClickSpanListener
                )
            }

            /**
             * 添加下标
             */
            fun addSubSection(sub: String?, targetSection: String? = ""): SpanBuilder {
                if (TextUtils.isEmpty(sub)) return this
                if (TextUtils.isEmpty(targetSection)) {
                    val start = spanStrBuilder?.length ?: 0
                    spanStrBuilder?.append(sub)
                    val end = spanStrBuilder?.length ?: 0
                    // 颜色
                    spanStrBuilder?.setSpan(
                        SubscriptSpan(),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    val subLength = sub?.length ?: 0
                    val targetLength = targetSection?.length ?: 0
                    val index: Int = if (TextUtils.isEmpty(content)) {
                        spanStrBuilder?.append(sub)
                        spanStrBuilder?.indexOf(sub.toString()) ?: -1
                    } else {
                        content?.indexOf(targetSection.toString()) ?: -1
                    }
                    if (index >= 0) {
                        spanStrBuilder?.insert(index + targetLength, sub)
                        // 颜色
                        spanStrBuilder?.setSpan(
                            SubscriptSpan(),
                            index + targetLength,
                            index + targetLength + subLength,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
                return this
            }

            /**
             * 显示到控件
             *
             * @param textView
             */
            fun showText(textView: TextView?) {
                textView?.movementMethod = LinkMovementMethod.getInstance()
                //方法重新设置文字背景为透明色。
                textView?.highlightColor = Color.TRANSPARENT
                textView?.text = spanStrBuilder
                spanStrBuilder?.clearSpans()
                spanStrBuilder?.clear()
                spanStrBuilder = null
            }
        }
    }

    interface OnClickSpanListener {
        fun onClickSpan(view: View)
    }

}

