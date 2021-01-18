package com.wiser.spantext

import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SpanController.create()
            .addSection("我同意logn(xy)《中国移动认证服务条款》、 《用户服务协议》、 《用户隐私协议》、 《个人信息保护政策》、 《关注中央人民广播电视》并授权XXXXXXX使用您的本机号码，您的反馈是我们最大的帮助")
            .setSubSection("logn") // 下标
            .setSuperSection("(xy)") // 上标
            .setUnderlineSection("我同意") // 下划线
            .setDeleteLineSection("我同意") // 删除线
            .setAbsSizeSection("中国", 10) // 绝对大小
            .setAbsSizeSection(10, 16, 18)
            .setRelSizeSection("移动", 2f)// 相对大小
            .setRelSizeSection(2f, 18, 20)
            .setStyleSection("条款", Typeface.BOLD_ITALIC) // 字体风格
            .setStyleSection(Typeface.BOLD_ITALIC, 26, 28)
            .setURLSection("服", "https://www.baidu.com") // 链接
            .setURLSection("https://www.baidu.com", 30, 32)
            .setTypefaceSection(
                "隐私",
                Typeface.createFromAsset(assets, "hyzhengyuan_85w.ttf")
            ) // 特殊字体
            .setTypefaceSection(Typeface.createFromAsset(assets, "hyzhengyuan_85w.ttf"), 36, 38)
            .setMaskSection("个人", BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID)) // 模糊
            .setMaskSection(BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID), 40, 42)
            .setForeColor("隐私", Color.RED) // 前置颜色
            .setBackColor("信息", Color.GREEN) // 后置背景色
            .setAlign(Layout.Alignment.ALIGN_CENTER) // 对齐方式
            .insertImage(this, R.mipmap.apple, 3) // 插入图片
            .insertImage("保护", this, R.mipmap.apple)
            .showText(tv_content)

        SpanController.create()
            .addSection("我同意《中国移动认证服务条款》、 《用户服务协议》、 《用户隐私协议》、 《个人信息保护政策》、 《关注中央人民广播电视》并授权XXXXXXX使用您的本机号码，您的反馈是我们最大的帮助")
            .insertSubSection("logn", 3)
            .insertSubSection("logn", "中国")
            .insertSuperSection("(xy)", 7)
            .insertSuperSection("(xy)", "移动")
            .insertUnderlineSection("下划线", 25)
            .insertUnderlineSection("下划线", "证")
            .insertDeleteLineSection("删除线", 33)
            .insertDeleteLineSection("删除线", "条款")
            .insertAbsSizeSection("绝对大小", 5, 47)
            .insertAbsSizeSection("绝对大小", "服务", 5)
            .insertRelSizeSection("相对大小", 2f, 59)
            .insertRelSizeSection("相对大小", "隐私", 2f)
            .insertStyleSection("字体风格", Typeface.BOLD_ITALIC, 70)
            .insertStyleSection("字体风格", "个人", Typeface.BOLD_ITALIC)
            .insertURLSection("URL", "https://www.baidu.com", 86)
            .insertURLSection("URL", "信息", "https://www.baidu.com")
            .insertTypefaceSection(
                "特殊字体",
                Typeface.createFromAsset(assets, "hyzhengyuan_85w.ttf"),
                101
            )
            .insertTypefaceSection(
                "特殊字体",
                "护",
                Typeface.createFromAsset(assets, "hyzhengyuan_85w.ttf")
            )
            .insertMaskSection("模糊阴影", BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID), 111)
            .insertMaskSection("模糊阴影", "关注", BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID))
            .insertForeColor("前置背景", Color.YELLOW, 116)
            .insertForeColor("前置背景", "中央", Color.YELLOW)
            .insertBackColor("后置背景", Color.MAGENTA, 137)
            .insertBackColor("后置背景", "广播", Color.MAGENTA)
            .setAlign(Layout.Alignment.ALIGN_CENTER) // 对齐方式
            .insertImage(this, R.mipmap.apple, 3) // 插入图片
            .insertImage("本机", this, R.mipmap.apple)
            .showText(tv_content1)

        // TextView SpanStringBuilder在小米8 小米9 SE 部分手机针对连续中文符号这种情况下，会将一行的右边界处的多余文本截取，估计是小米修改了API导致的

        SpanController.create()
            .addSection("我同意")
            .addForeColorSection(null, Color.RED)
            .addForeColorSection(
                "《中国移动认证服务条款》",
                Color.BLUE,
                object : SpanController.OnClickSpanListener {
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity, "点击《中国移动认证服务条款》", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            .addSection("、 ")
            .addForeColorSection(
                "《用户服务协议》",
                Color.RED,
                object : SpanController.OnClickSpanListener {
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity, "点击《用户服务协议》", Toast.LENGTH_LONG).show()
                    }
                })
            .addSection("、 ")
            .addForeColorSection(
                "《用户隐私协议》",
                Color.YELLOW,
                object : SpanController.OnClickSpanListener {
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity, "点击《用户隐私协议》", Toast.LENGTH_LONG).show()
                    }
                })
            .addSection("、 ")
            .addForeColorSection(
                "《个人信息保护政策》",
                Color.GREEN,
                object : SpanController.OnClickSpanListener {
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity, "点击《个人信息保护政策》", Toast.LENGTH_LONG).show()
                    }
                })
            .addSection("、 ")
            .addBackColorSection(
                "《关注中央人民广播电视》",
                Color.MAGENTA,
                object : SpanController.OnClickSpanListener {
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity, "点击《关注中央人民广播电视》", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            .addSubSection("logn")
            .addSuperSection("(xy)")
            .addUnderlineSection("并授权")
            .addImage(this, R.mipmap.apple) // 添加图片
            .addDeleteLineSection("XXXXXXX")
            .addAbsSizeSection("使用", 10)
            .addRelSizeSection("您的", 2f)
            .addURLSection("本机", "https:www.baidu.com")
            .addStyleSection("号码", Typeface.BOLD_ITALIC)
            .addTypefaceSection("，您的", Typeface.createFromAsset(assets, "hyzhengyuan_85w.ttf"))
            .addMaskSection("反馈是我们最大的帮助", BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID))
            .addImage(this, R.mipmap.apple) // 添加图片
            .showText(tv_content2)
    }
}