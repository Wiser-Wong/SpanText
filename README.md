# SpanText
TextView部分文本设置不同的样式

## 截图
![images](https://github.com/Wiser-Wong/SpanText/blob/master/images/screenshot.png)

## 操作指南
* 添加完整文本，然后修改其中文本样式

      SpanController.create()
            .addSection("我同意logn(xy)《用户服务协议》、 《用户隐私协议》、 《个人信息保护政策》、 《关注电视》并授权XXXXXXX使用您的本机号码，您的反馈是我们最大的帮助")
            .setSubSection("logn") // 下标
            .setSuperSection("(xy)") // 上标
            .setUnderlineSection("我同意") // 下划线
            .setDeleteLineSection("我同意") // 删除线
            .setAbsSizeSection("服务", 10) // 绝对大小
            .setAbsSizeSection(10, 16, 18)
            .setRelSizeSection("帮助", 2f)// 相对大小
            .setRelSizeSection(2f, 18, 20)
            .setStyleSection("条款", Typeface.BOLD_ITALIC) // 字体风格
            .setStyleSection(Typeface.BOLD_ITALIC, 26, 28)
            .setURLSection("服", "https://www.baidu.com") // 链接
            .setURLSection("https://www.baidu.com", 30, 32)
            .setTypefaceSection("隐私", Typeface.createFromAsset(assets, "hyzhengyuan_85w.ttf")) // 特殊字体
            .setTypefaceSection(Typeface.createFromAsset(assets, "hyzhengyuan_85w.ttf"), 36, 38)
            .setMaskSection("个人", BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID)) // 模糊
            .setMaskSection(BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID), 40, 42)
            .setForeColor("隐私", Color.RED) // 前置颜色
            .setBackColor("信息", Color.GREEN) // 后置背景色
            .setAlign(Layout.Alignment.ALIGN_CENTER) // 对齐方式
            .insertImage(this, R.mipmap.apple, 3) // 插入图片
            .insertImage("保护", this, R.mipmap.apple)
            .showText(tv_content)
      
* 添加完整文本，在基础上继续插入新文本

      SpanController.create()
            .addSection("我同意《用户服务协议》、 《用户隐私协议》、 《个人信息保护政策》、 《关注电视》并授权XXXXXXX使用您的本机号码，您的反馈是我们最大的帮助")
            .insertSubSection("logn", 3)
            .insertSubSection("logn", "同意")
            .insertSuperSection("(xy)", 7)
            .insertSuperSection("(xy)", "服务")
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
            .insertTypefaceSection("特殊字体", Typeface.createFromAsset(assets, "hyzhengyuan_85w.ttf"),101)
            .insertTypefaceSection("特殊字体", "护", Typeface.createFromAsset(assets,"hyzhengyuan_85w.ttf"))
            .insertMaskSection("模糊阴影", BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID), 111)
            .insertMaskSection("模糊阴影", "关注", BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID))
            .insertForeColor("前置背景", Color.YELLOW, 116)
            .insertForeColor("前置背景", "中央", Color.YELLOW)
            .insertBackColor("后置背景", Color.MAGENTA, 137)
            .insertBackColor("后置背景", "广播", Color.MAGENTA)
            .setAlign(Layout.Alignment.ALIGN_CENTER) // 对齐方式
            .insertImage(this, R.mipmap.apple, 3) // 插入图片
            .insertImage("本机", this, R.mipmap.apple,true,drawable)
            .showText(tv_content1)
           
* 按顺序添加文本内容

      SpanController.create()
            .addSection("我同意")
            .addForeColorSection(null, Color.RED)
            .addForeColorSection("《用户服务协议》",Color.RED,object : SpanController.OnClickSpanListener {
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity, "点击《用户服务协议》", Toast.LENGTH_LONG).show()
                    }
                })
            .addSection("、 ")
            .addForeColorSection("《用户隐私协议》", Color.YELLOW,object : SpanController.OnClickSpanListener {
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity, "点击《用户隐私协议》", Toast.LENGTH_LONG).show()
                    }
                })
            .addSection("、 ")
            .addForeColorSection("《个人信息保护政策》",Color.GREEN,object : SpanController.OnClickSpanListener {
                    override fun onClickSpan(view: View) {
                        Toast.makeText(this@MainActivity, "点击《个人信息保护政策》", Toast.LENGTH_LONG).show()
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
            .addImage(this, R.mipmap.apple, true, drawable) // 添加图片
            .showText(tv_content2)
