package com.wiser.spantext

import android.content.Context
import android.graphics.Color
import android.graphics.MaskFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.util.*

class SpanController {

    companion object {

        fun create(): SpanBuilder {
            return SpanBuilder()
        }

        class SpanBuilder {

            private var spanStrBuilder: SpannableStringBuilder? = null

            init {
                spanStrBuilder = SpannableStringBuilder("")
            }

            /**
             * 添加字体片段
             *
             * @param section 要添加的文字
             * @param span    任何span
             * @param flag    可以是
             *                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 不包含 section 前后文字，最常用
             *                Spanned.SPAN_INCLUSIVE_EXCLUSIVE 包含 section 前面文字
             *                Spanned.SPAN_EXCLUSIVE_INCLUSIVE 包含 section 后面文字
             *                Spanned.SPAN_INCLUSIVE_INCLUSIVE 包含 section 前后文字
             *                ……
             * @param onClickSpanListener 字体片段点击监听
             *
             * @return SpanBuilder
             */
            private fun addCertainSection(
                section: String?,
                span: Any?,
                flag: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                onClickSpanListener: OnClickSpanListener? = null
            ): SpanBuilder {
                if (TextUtils.isEmpty(section)) return this
                val start = spanStrBuilder?.length ?: 0
                spanStrBuilder?.append(section)
                val end = spanStrBuilder?.length ?: 0
                spanStrBuilder?.setSpan(span, start, end, flag)
                onClickSpanListener?.let {
                    // 点击
                    spanStrBuilder?.setSpan(
                        object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                it.onClickSpan(widget)
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                super.updateDrawState(ds)
                                ds.isUnderlineText = false
                                ds.color = ds.linkColor
                            }
                        },
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
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
            fun addSubSection(section: String?): SpanBuilder {
                return addCertainSection(section, SubscriptSpan())
            }

            /**
             * 添加上标
             */
            fun addSuperSection(sup: String?): SpanBuilder {
                return addCertainSection(sup, SuperscriptSpan())
            }

            /**
             * 添加下划线片段
             */
            fun addUnderlineSection(section: String?): SpanBuilder {
                return addCertainSection(section, UnderlineSpan())
            }

            /**
             * 添加删除线片段
             */
            fun addDeleteLineSection(section: String?): SpanBuilder {
                return addCertainSection(section, StrikethroughSpan())
            }

            /**
             * 添加绝对大小字体片段
             */
            fun addAbsSizeSection(
                section: String?,
                size: Int
            ): SpanBuilder {
                return addCertainSection(
                    section,
                    AbsoluteSizeSpan(size, true)
                )
            }

            /**
             * 添加相对大小字体片段
             */
            fun addRelSizeSection(
                section: String?,
                proportion: Float
            ): SpanBuilder {
                return addCertainSection(
                    section,
                    RelativeSizeSpan(proportion)
                )
            }

            /**
             * 添加 url 字体片段
             */
            fun addURLSection(
                section: String?,
                url: String?
            ): SpanBuilder {
                return addCertainSection(section, URLSpan(url))
            }

            /**
             * 添加某种风格（Style）的文字片段
             *
             * @param section
             * @param style   TypeFace.BOLD  加粗
             *                TypeFace.BOLD_ITALIC  加粗倾斜
             *                TypeFace.ITALIC  倾斜
             *                TypeFace.NORMAL  正常
             * @return
             */
            fun addStyleSection(
                section: String?,
                style: Int
            ): SpanBuilder {
                return addCertainSection(
                    section,
                    StyleSpan(style)
                )
            }

            /**
             * 添加某种字体的文字片段
             *
             * @param section
             * @param family  The font family for this typeface.  Examples include
             * "monospace", "serif", and "sans-serif".
             */
            fun addTypefaceSection(
                section: String?,
                family: String?
            ): SpanBuilder {
                return addCertainSection(
                    section,
                    TypefaceSpan(family)
                )
            }

            /**
             * 添加某种字体的文字片段
             *
             * @param section
             * @param typeface
             */
            @RequiresApi(Build.VERSION_CODES.P)
            fun addTypefaceSection(
                section: String?,
                typeface: Typeface
            ): SpanBuilder {
                return addCertainSection(
                    section,
                    TypefaceSpan(typeface)
                )
            }

            /**
             * 添加模糊
             * @param section 片段
             * @param maskFilter
             */
            fun addMaskSection(section: String?, maskFilter: MaskFilter?): SpanBuilder {
                return addCertainSection(section, MaskFilterSpan(maskFilter))
            }

            /**
             * 文字后添加图片
             *
             * @param resId
             * @return
             */
            fun addImage(
                context: Context?,
                resId: Int
            ): SpanBuilder {
                insertImage(context, resId, spanStrBuilder?.length ?: 0)
                return this
            }

            /**
             * 文字中某位置（where）插入图片
             *
             * @param resId 图片资源 Id
             * @param where 插入位置：占一个字的位置，整体索引增加一个
             * @return
             */
            fun insertImage(
                targetSection: String?,
                context: Context?,
                color: Int,
            ): SpanBuilder {
                return insertImage(targetSection, context, color, false, Which.LAST)
            }

            /**
             * 文字中某位置（where）插入图片
             *
             * @param resId 图片资源 Id
             * @param where 插入位置：占一个字的位置，整体索引增加一个
             * @return
             */
            fun insertImage(
                targetSection: String?,
                context: Context?,
                color: Int,
                ignoreCase: Boolean,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    ignoreCase,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertImage(context, color, end)
                        }
                    })
            }

            /**
             * 文字中某位置（where）插入图片
             *
             * @param resId 图片资源 Id
             * @param where 插入位置：占一个字的位置，整体索引增加一个
             * @return
             */
            fun insertImage(
                context: Context?,
                resId: Int,
                where: Int,
                isImageCenter: Boolean = false,
                drawable: Drawable? = null
            ): SpanBuilder {
                spanStrBuilder?.insert(where, " ")
                spanStrBuilder?.setSpan(
                    context?.let {
                        if (isImageCenter) {
                            drawable?.let { it1 -> CenterImageSpan(it1) }
                        } else {
                            ImageSpan(it, resId)
                        }
                    },
                    where,
                    where + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                return this
            }

            /**
             * 给最后该片段（section）加上前景色，不区分大小写
             */
            fun setForeColor(
                section: String?,
                color: Int
            ): SpanBuilder {
                return setForeColor(section, color, true, Which.LAST)
            }

            /**
             * 整体加上前景色
             */
            fun setForeColor(color: Int): SpanBuilder {
                return setForeColor(color, 0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 加上前景色
             *
             * @param ignoreCase boolean,true 区分大小写；false, 不区分大小写
             */
            fun setForeColor(
                section: String?,
                color: Int,
                ignoreCase: Boolean,
                which: Which?
            ): SpanBuilder {
                return onDecor(section, ignoreCase, which, object : DecorCallback {
                    override fun decor(start: Int, end: Int) {
                        setForeColor(color, start, end)
                    }
                })
            }

            /**
             * 加上前景色
             */
            fun setForeColor(
                color: Int,
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    ForegroundColorSpan(color),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后该片段（section）加上前景色，不区分大小写
             */
            fun insertForeColor(
                section: String?,
                targetSection: String?,
                color: Int
            ): SpanBuilder {
                return insertForeColor(section, targetSection, color, true, Which.LAST)
            }

            /**
             * 加上前景色
             *
             * @param ignoreCase boolean,true 区分大小写；false, 不区分大小写
             */
            fun insertForeColor(
                section: String?,
                targetSection: String?,
                color: Int,
                ignoreCase: Boolean,
                which: Which?
            ): SpanBuilder {
                return onDecor(targetSection, ignoreCase, which, object : DecorCallback {
                    override fun decor(start: Int, end: Int) {
                        insertForeColor(section, color, end)
                    }
                })
            }

            /**
             * 加上前景色
             */
            fun insertForeColor(
                section: String?,
                color: Int,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    ForegroundColorSpan(color),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）加上背景色，不区分大小写
             */
            fun setBackColor(
                section: String?,
                color: Int
            ): SpanBuilder {
                return setBackColor(section, color, true, Which.LAST)
            }

            /**
             * 整体加上背景色
             */
            fun setBackColor(color: Int): SpanBuilder {
                return setBackColor(color, 0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 加上背景色
             */
            fun setBackColor(
                section: String?,
                color: Int,
                ignoreCase: Boolean,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    ignoreCase,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setBackColor(color, start, end)
                        }
                    })
            }

            /**
             * 加上背景色
             */
            fun setBackColor(
                color: Int,
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    BackgroundColorSpan(color),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）加上背景色，不区分大小写
             */
            fun insertBackColor(
                section: String?,
                targetSection: String?,
                color: Int
            ): SpanBuilder {
                return insertBackColor(section, targetSection, color, true, Which.LAST)
            }

            /**
             * 加上背景色
             */
            fun insertBackColor(
                section: String?,
                targetSection: String?,
                color: Int,
                ignoreCase: Boolean,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    ignoreCase,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertBackColor(section, color, end)
                        }
                    })
            }

            /**
             * 加上背景色
             */
            fun insertBackColor(
                section: String?,
                color: Int,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    BackgroundColorSpan(color),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置为下标
             */
            fun setSubSection(section: String?): SpanBuilder {
                return setSubSection(section, Which.LAST)
            }

            /**
             * 整体设置为下标
             */
            fun setSubSection(): SpanBuilder {
                return setSubSection(0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 设置为下标
             */
            fun setSubSection(
                section: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setSubSection(start, end)
                        }
                    })
            }

            /**
             * 设置为下标
             */
            fun setSubSection(
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    SubscriptSpan(),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 设置为下标
             */
            fun insertSubSection(
                section: String?,
                targetSection: String?
            ): SpanBuilder {
                return insertSubSection(section, targetSection, Which.LAST)
            }

            /**
             * 设置为下标
             */
            fun insertSubSection(
                section: String?,
                targetSection: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertSubSection(section, end)
                        }
                    })
            }

            /**
             * 插入下标
             */
            fun insertSubSection(section: String?, where: Int): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    SubscriptSpan(),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置为上标
             */
            fun setSuperSection(section: String?): SpanBuilder {
                return setSuperSection(section, Which.LAST)
            }

            /**
             * 整体设置为上标
             */
            fun setSuperSection(): SpanBuilder {
                return setSuperSection(0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 设置为上标
             */
            fun setSuperSection(
                section: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setSuperSection(start, end)
                        }
                    })
            }

            /**
             * 设置为上标
             */
            fun setSuperSection(
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    SuperscriptSpan(),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 设置为下标
             */
            fun insertSuperSection(
                section: String?,
                targetSection: String?
            ): SpanBuilder {
                return insertSubSection(section, targetSection, Which.LAST)
            }

            /**
             * 设置为下标
             */
            fun insertSuperSection(
                section: String?,
                targetSection: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertSuperSection(section, end)
                        }
                    })
            }

            /**
             * 插入上标
             */
            fun insertSuperSection(section: String?, where: Int): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    SuperscriptSpan(),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置下划线
             */
            fun setUnderlineSection(section: String?): SpanBuilder {
                return setUnderlineSection(section, Which.LAST)
            }

            /**
             * 给所有文字设置下划线
             */
            fun setUnderlineSection(): SpanBuilder {
                return setUnderlineSection(0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 给片段设置下划线
             */
            fun setUnderlineSection(
                section: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setUnderlineSection(start, end)
                        }
                    })
            }

            /**
             * 给片段设置下划线
             */
            fun setUnderlineSection(
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    UnderlineSpan(),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置下划线
             */
            fun insertUnderlineSection(section: String?, targetSection: String?): SpanBuilder {
                return insertUnderlineSection(section, targetSection, Which.LAST)
            }

            /**
             * 给片段设置下划线
             */
            fun insertUnderlineSection(
                section: String?,
                targetSection: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertUnderlineSection(section, end)
                        }
                    })
            }

            /**
             * 给片段设置下划线
             */
            fun insertUnderlineSection(
                section: String?,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    UnderlineSpan(),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）添加删除线
             */
            fun setDeleteLineSection(section: String?): SpanBuilder {
                return setDeleteLineSection(section, Which.LAST)
            }

            /**
             * 给所有文字添加删除线
             */
            fun setDeleteLineSection(): SpanBuilder {
                return setDeleteLineSection(0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 给片段添加删除线
             */
            fun setDeleteLineSection(
                section: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setDeleteLineSection(start, end)
                        }
                    })
            }

            /**
             * 给片段添加删除线
             */
            fun setDeleteLineSection(
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    StrikethroughSpan(),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）添加删除线
             */
            fun insertDeleteLineSection(section: String?, targetSection: String?): SpanBuilder {
                return insertDeleteLineSection(section, targetSection, Which.LAST)
            }

            /**
             * 给片段添加删除线
             */
            fun insertDeleteLineSection(
                section: String?,
                targetSection: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertDeleteLineSection(section, end)
                        }
                    })
            }

            /**
             * 给片段添加删除线
             */
            fun insertDeleteLineSection(
                section: String?,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    StrikethroughSpan(),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置绝对大小
             */
            fun setAbsSizeSection(
                section: String?,
                size: Int
            ): SpanBuilder {
                return setAbsSizeSection(section, size, Which.LAST)
            }

            /**
             * 给所有文字设置绝对大小
             */
            fun setAbsSizeSection(size: Int): SpanBuilder {
                return setAbsSizeSection(size, 0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 给片段的字体设置绝对大小
             */
            fun setAbsSizeSection(
                section: String?,
                size: Int,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setAbsSizeSection(size, start, end)
                        }
                    })
            }

            /**
             * 给片段的字体设置绝对大小
             */
            fun setAbsSizeSection(
                size: Int,
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    AbsoluteSizeSpan(size, true),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置绝对大小
             */
            fun insertAbsSizeSection(
                section: String?,
                targetSection: String?,
                size: Int
            ): SpanBuilder {
                return insertAbsSizeSection(section, targetSection, size, Which.LAST)
            }

            /**
             * 给片段的字体设置绝对大小
             */
            fun insertAbsSizeSection(
                section: String?,
                targetSection: String?,
                size: Int,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertAbsSizeSection(section, size, end)
                        }
                    })
            }

            /**
             * 给片段的字体设置绝对大小
             */
            fun insertAbsSizeSection(
                section: String?,
                size: Int,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    AbsoluteSizeSpan(size, true),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段的字体设置相对大小
             */
            fun setRelSizeSection(
                section: String?,
                proportion: Float
            ): SpanBuilder {
                return setRelSizeSection(section, proportion, Which.LAST)
            }

            /**
             * 给所有文字设置相对大小
             */
            fun setRelSizeSection(proportion: Float): SpanBuilder {
                return setRelSizeSection(proportion, 0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 给片段的字体设置相对大小
             */
            fun setRelSizeSection(
                section: String?,
                proportion: Float,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setRelSizeSection(proportion, start, end)
                        }
                    })
            }

            /**
             * 给片段的字体设置相对大小
             */
            fun setRelSizeSection(
                proportion: Float,
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    RelativeSizeSpan(proportion),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段的字体设置相对大小
             */
            fun insertRelSizeSection(
                section: String?,
                targetSection: String?,
                proportion: Float
            ): SpanBuilder {
                return insertRelSizeSection(section, targetSection, proportion, Which.LAST)
            }

            /**
             * 给片段的字体设置相对大小
             */
            fun insertRelSizeSection(
                section: String?,
                targetSection: String?,
                proportion: Float,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertRelSizeSection(section, proportion, end)
                        }
                    })
            }

            /**
             * 给片段的字体设置相对大小
             */
            fun insertRelSizeSection(
                section: String?,
                proportion: Float,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    RelativeSizeSpan(proportion),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 设置url 字体片段
             */
            fun setURLSection(section: String?, url: String?): SpanBuilder {
                return setURLSection(section, url, Which.LAST)
            }

            /**
             * 整体转为 url 字体片段
             */
            fun setURLSection(url: String?): SpanBuilder {
                return setURLSection(url, 0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 转为 url 字体片段
             */
            fun setURLSection(
                section: String?,
                url: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setURLSection(url, start, end)
                        }
                    })
            }

            /**
             * 转为 url 字体片段
             */
            fun setURLSection(
                url: String?,
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(URLSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                return this
            }

            /**
             * 插入url 字体片段
             */
            fun insertURLSection(
                section: String?,
                targetSection: String?,
                url: String?
            ): SpanBuilder {
                return insertURLSection(section, targetSection, url, Which.LAST)
            }

            /**
             * 转为 url 字体片段
             */
            fun insertURLSection(
                section: String?,
                targetSection: String?,
                url: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertURLSection(section, url, end)
                        }
                    })
            }

            /**
             * 转为 url 字体片段
             */
            fun insertURLSection(
                section: String?,
                url: String?,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    URLSpan(url),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个片段（section）设置字体风格（粗、斜、正常）
             * @param section 内容
             * @param style   TypeFace.BOLD  加粗
             *                TypeFace.BOLD_ITALIC  加粗倾斜
             *                TypeFace.ITALIC  倾斜
             *                TypeFace.NORMAL  正常
             */
            fun setStyleSection(
                section: String?,
                style: Int
            ): SpanBuilder {
                return setStyleSection(section, style, Which.LAST)
            }

            /**
             * 整体设置字体风格（粗、斜、正常）
             */
            fun setStyleSection(style: Int): SpanBuilder {
                return setStyleSection(style, 0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 设置字体风格（粗、斜、正常）
             */
            fun setStyleSection(
                section: String?,
                style: Int,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setStyleSection(style, start, end)
                        }
                    })
            }

            /**
             * 设置字体风格（粗、斜、正常）
             */
            fun setStyleSection(
                style: Int,
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    StyleSpan(style),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个片段（section）设置字体风格（粗、斜、正常）
             * @param section 内容
             * @param style   TypeFace.BOLD  加粗
             *                TypeFace.BOLD_ITALIC  加粗倾斜
             *                TypeFace.ITALIC  倾斜
             *                TypeFace.NORMAL  正常
             */
            fun insertStyleSection(
                section: String?,
                targetSection: String?,
                style: Int
            ): SpanBuilder {
                return insertStyleSection(section, targetSection, style, Which.LAST)
            }

            /**
             * 设置字体风格（粗、斜、正常）
             */
            fun insertStyleSection(
                section: String?,
                targetSection: String?,
                style: Int,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertStyleSection(section, style, end)
                        }
                    })
            }

            /**
             * 设置字体风格（粗、斜、正常）
             */
            fun insertStyleSection(
                section: String?,
                style: Int,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    StyleSpan(style),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置字体
             */
            @RequiresApi(Build.VERSION_CODES.P)
            fun setTypefaceSection(
                section: String?,
                typeface: Typeface?
            ): SpanBuilder {
                return setTypefaceSection(section, typeface, Which.LAST)
            }

            /**
             * 整体设置字体
             */
            @RequiresApi(Build.VERSION_CODES.P)
            fun setTypefaceSection(typeface: Typeface): SpanBuilder {
                return setTypefaceSection(typeface, 0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 设置字体
             *
             * @param family "monospace", "serif", and "sans-serif"
             * @param which  SpanUtil.Which.FIRST、SpanUtil.Which.LAST、SpanUtil.Which.ALL
             */
            @RequiresApi(Build.VERSION_CODES.P)
            fun setTypefaceSection(
                section: String?,
                typeface: Typeface?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setTypefaceSection(typeface, start, end)
                        }
                    })
            }

            /**
             * 设置字体
             */
            @RequiresApi(Build.VERSION_CODES.P)
            fun setTypefaceSection(
                typeface: Typeface?,
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    typeface?.let { TypefaceSpan(it) },
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置字体
             */
            @RequiresApi(Build.VERSION_CODES.P)
            fun insertTypefaceSection(
                section: String?,
                targetSection: String?,
                typeface: Typeface?
            ): SpanBuilder {
                return insertTypefaceSection(section, targetSection, typeface, Which.LAST)
            }

            /**
             * 设置字体
             *
             * @param typeface
             * @param which  Which.FIRST、Which.LAST、.Which.ALL
             */
            @RequiresApi(Build.VERSION_CODES.P)
            fun insertTypefaceSection(
                section: String?,
                targetSection: String?,
                typeface: Typeface?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertTypefaceSection(section, typeface, end)
                        }
                    })
            }

            /**
             * 设置字体
             */
            @RequiresApi(Build.VERSION_CODES.P)
            fun insertTypefaceSection(
                section: String?,
                typeface: Typeface?,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    typeface?.let { TypefaceSpan(it) },
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置字体
             */
            fun setTypefaceSection(
                section: String?,
                family: String?
            ): SpanBuilder {
                return setTypefaceSection(section, family, Which.LAST)
            }

            /**
             * 整体设置字体
             */
            fun setTypefaceSection(family: String?): SpanBuilder {
                return setTypefaceSection(family, 0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 设置字体
             *
             * @param family "monospace", "serif", and "sans-serif"
             * @param which  SpanUtil.Which.FIRST、SpanUtil.Which.LAST、SpanUtil.Which.ALL
             */
            fun setTypefaceSection(
                section: String?,
                family: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setTypefaceSection(family, start, end)
                        }
                    })
            }

            /**
             * 设置字体
             */
            fun setTypefaceSection(
                family: String?,
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    TypefaceSpan(family),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 给最后一个该片段（section）设置字体
             */
            fun insertTypefaceSection(
                section: String?,
                targetSection: String?,
                family: String?
            ): SpanBuilder {
                return insertTypefaceSection(section, targetSection, family, Which.LAST)
            }

            /**
             * 设置字体
             *
             * @param family "monospace", "serif", and "sans-serif"
             * @param which  SpanUtil.Which.FIRST、SpanUtil.Which.LAST、SpanUtil.Which.ALL
             */
            fun insertTypefaceSection(
                section: String?,
                targetSection: String?,
                family: String?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertTypefaceSection(section, family, end)
                        }
                    })
            }

            /**
             * 设置字体
             */
            fun insertTypefaceSection(
                section: String?,
                family: String?,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    TypefaceSpan(family),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 设置 Mask
             *
             * @param maskFilter BlurMaskFilter、EmbossMaskFilter
             *                   例：//Blur((使…) 变模糊) a character
             *                   new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
             *                   //Emboss a character
             *                   new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
             */
            fun setMaskSection(
                section: String?,
                maskFilter: MaskFilter?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            setMaskSection(maskFilter, start, end)
                        }
                    })
            }

            /**
             * 为最后一个该片段（section）设置 Mask
             *
             * @param maskFilter BlurMaskFilter、EmbossMaskFilter
             *                   例：//Blur a character
             *                   new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
             *                   //Emboss a character
             *                   new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
             */
            fun setMaskSection(
                section: String?,
                maskFilter: MaskFilter?
            ): SpanBuilder {
                return setMaskSection(section, maskFilter, Which.LAST)
            }

            /**
             * 为整体设置 Mask
             *
             * @param maskFilter BlurMaskFilter、EmbossMaskFilter
             *                   例：//Blur a character
             *                   new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
             *                   //Emboss a character
             *                   new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
             */
            fun setMaskSection(maskFilter: MaskFilter?): SpanBuilder {
                return setMaskSection(maskFilter, 0, spanStrBuilder?.length ?: 0)
            }

            /**
             * 设置 Mask
             *
             * @param maskFilter BlurMaskFilter、EmbossMaskFilter
             *                   例：//Blur a character
             *                   new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
             *                   //Emboss a character
             *                   new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
             * @param start      开始位置
             * @param end        截止位置
             */
            fun setMaskSection(
                maskFilter: MaskFilter?,
                start: Int,
                end: Int
            ): SpanBuilder {
                spanStrBuilder?.setSpan(
                    MaskFilterSpan(maskFilter),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 设置 Mask
             *
             * @param maskFilter BlurMaskFilter、EmbossMaskFilter
             *                   例：//Blur((使…) 变模糊) a character
             *                   new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
             *                   //Emboss a character
             *                   new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
             */
            fun insertMaskSection(
                section: String?,
                targetSection: String?,
                maskFilter: MaskFilter?,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    targetSection,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            insertMaskSection(section, maskFilter, end)
                        }
                    })
            }

            /**
             * 为最后一个该片段（section）设置 Mask
             *
             * @param maskFilter BlurMaskFilter、EmbossMaskFilter
             *                   例：//Blur a character
             *                   new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
             *                   //Emboss a character
             *                   new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
             */
            fun insertMaskSection(
                section: String?,
                targetSection: String?,
                maskFilter: MaskFilter?
            ): SpanBuilder {
                return insertMaskSection(section, targetSection, maskFilter, Which.LAST)
            }

            /**
             * 设置 Mask
             *
             * @param maskFilter BlurMaskFilter、EmbossMaskFilter
             *                   例：//Blur a character
             *                   new BlurMaskFilter(density*2, BlurMaskFilter.Blur.NORMAL);
             *                   //Emboss a character
             *                   new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
             * @param start      开始位置
             * @param end        截止位置
             */
            fun insertMaskSection(
                section: String?,
                maskFilter: MaskFilter?,
                where: Int
            ): SpanBuilder {
                if (section == null || where >= spanStrBuilder?.length ?: 0) return this
                spanStrBuilder?.insert(where, section)
                spanStrBuilder?.setSpan(
                    MaskFilterSpan(maskFilter),
                    where,
                    where + section.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 设置对齐方式
             * @param alignment Layout.Alignment.ALIGN_CENTER 居中
             *                  Layout.Alignment.ALIGN_NORMAL 正常（左对齐）
             *                  Layout.Alignment.ALIGN_OPPOSITE 反向（右对齐）
             */
            fun setAlign(alignment: Layout.Alignment): SpanBuilder {
                spanStrBuilder?.setSpan(
                    AlignmentSpan.Standard(alignment),
                    0,
                    spanStrBuilder?.length ?: 0,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                return this
            }

            /**
             * 清除格式
             */
            fun clearSpans(): SpanBuilder {
                spanStrBuilder?.clearSpans()
                return this
            }

            /**
             * 移除最后一个该片段（section）的格式
             */
            fun removeSpans(section: String): SpanBuilder {
                return removeSpans(section, Which.LAST)
            }

            /**
             * 移除格式，从某一个下标开始
             */
            fun removeSpans(
                section: String,
                fromIndex: Int
            ): SpanBuilder {
                var fromIndex = fromIndex
                val baseStr = getString()
                fromIndex = baseStr?.indexOf(section, fromIndex) ?: 0
                removeSpans(fromIndex, fromIndex + section.length)
                return this
            }

            /**
             * 移除格式
             */
            fun removeSpans(
                section: String,
                which: Which?
            ): SpanBuilder {
                return onDecor(
                    section,
                    false,
                    which,
                    object : DecorCallback {
                        override fun decor(start: Int, end: Int) {
                            removeSpans(start, end)
                        }
                    })
            }

            /**
             * 移除格式
             *
             * @param start 开始位置
             * @param end   结束位置
             * @return this
             */
            fun removeSpans(
                start: Int,
                end: Int
            ): SpanBuilder {
                val spans = spanStrBuilder?.getSpans(start, end, Any::class.java)
                if (spans != null) {
                    for (span in spans) {
                        spanStrBuilder!!.removeSpan(span)
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

            private interface DecorCallback {
                fun decor(start: Int, end: Int)
            }

            private fun onDecor(
                section: String?,
                ignoreCase: Boolean,
                which: Which?,
                decorCallback: DecorCallback?
            ): SpanBuilder {
                var section: String? = section
                var baseStr: String? = getString()
                if (ignoreCase) {
                    section = section?.toUpperCase(Locale.getDefault())
                    baseStr = baseStr?.toUpperCase(Locale.getDefault())
                }
                var start = 0
                when (which) {
                    Which.FIRST -> {
                        start = section?.let { baseStr?.indexOf(it) } ?: 0
                        if (start != -1)
                            if (section != null) {
                                decorCallback?.decor(start, start + section.length)
                            }
                    }
                    Which.LAST -> {
                        start = section?.let { baseStr?.lastIndexOf(it) } ?: 0
                        if (start != -1)
                            if (section != null) {
                                decorCallback?.decor(start, start + section.length)
                            }
                    }
                    Which.ALL -> while (true) {
                        start = section?.let { baseStr?.indexOf(it, start) } ?: 0
                        if (start == -1) break
                        if (section != null) {
                            decorCallback?.decor(start, start + section.length)
                        }
                        start += section?.length ?: 0
                    }
                }
                return this
            }

            /**
             * 获得当前 SpanStringBuilder 中的字符串
             */
            fun getString(): String? {
                return spanStrBuilder?.toString()
            }

            /**
             * 获得当前 SpanStringBuilder 实例
             */
            fun getSpanStrBuilder(): SpannableStringBuilder? {
                return spanStrBuilder
            }
        }
    }

    interface OnClickSpanListener {
        fun onClickSpan(view: View)
    }

}

/**
 * 标记第一个、最后一个、所有
 */
enum class Which {
    FIRST, LAST, ALL
}

