package com.wiser.spantext

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SpanController.create()
                .addContent("我同意《中国移动认证服务条款》、 《用户服务协议》、 《用户隐私协议》、 《个人信息保护政策》、 《关注中央人民广播电视》并授权XXXXXXX使用您的本机号码")
                .addForeColorSection(null,Color.RED)
                .addForeColorSection("《中国移动认证服务条款》",Color.BLUE,object : SpanController.OnClickSpanListener{
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity,"点击《中国移动认证服务条款》",Toast.LENGTH_LONG).show()
                    }
                })
                .addForeColorSection("《用户服务协议》",Color.RED,object : SpanController.OnClickSpanListener{
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity,"点击《用户服务协议》",Toast.LENGTH_LONG).show()
                    }
                })
                .addForeColorSection("《用户隐私协议》",Color.YELLOW,object : SpanController.OnClickSpanListener{
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity,"点击《用户隐私协议》",Toast.LENGTH_LONG).show()
                    }
                })
                .addForeColorSection("《个人信息保护政策》",Color.GREEN,object : SpanController.OnClickSpanListener{
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity,"点击《个人信息保护政策》",Toast.LENGTH_LONG).show()
                    }
                })
                .addBackColorSection("《关注中央人民广播电视》",Color.MAGENTA,object : SpanController.OnClickSpanListener{
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity,"点击《关注中央人民广播电视》",Toast.LENGTH_LONG).show()
                    }
                })
                .addSubSection("a","《个人信息保护政策》")
                .showText(tv_content)
    }
}