MDWechat
====
# 简介
MDWechat 是一个能让微信 Material Design 化的 Xposed 模块.

3.x 版本可是说是 1.x 和 2.x 版本的结合.

由于 3.x 不依赖 WechatSpellbook，部分代码参(chao)考(xi) WechatSpellbook

# 功能
实现的功能有:
1. 主界面 TabLayout Material 化,支持自定义图标
2. ~~主界面搜索 Material 化~~(2.0未加入)
3. 主界面添加悬浮按钮(FloatingActionButton),支持自定义按钮文本/图标/入口
4. 全局头像圆角
5. 全局 ActionBar 颜色修改
6. 全局状态栏颜色修改,支持半透明/全透明(沉浸)
7. 主界面列表去掉分割线,增加 Ripple 效果(按下水波纹),支持修改颜色
8. ~~主界面支持隐藏 发现/设置 页面~~(2.0未加入)
9. 主界面 4 个页面背景修改
10. ~~支持聊天列表置顶底色修改~~(2.0未加入)
11. 聊天气泡修改,支持.9图,支持修改着色,支持修改文本颜色
12. ~~发现页面支持隐藏朋友圈/扫一扫/摇一摇/附近的人/游戏/购物/小程序~~(微信自带,2.0已去掉)
13. 移除会话列表下拉小程序,最低支持微信 6.6.2
14. 识别微X模块入口,移动到悬浮按钮(2.0新增)
15. 主界面字体颜色修改(2.0新增)

# 版本支持
1. ~~支持的微信版本: [酷安渠道版](https://www.coolapk.com/apk/com.tencent.mm)(6.5.19 6.5.22 6.5.23 6.6.0 6.6.1 6.6.2 6.6.3 6.6.5), [play 版](https://play.google.com/store/apps/details?id=com.tencent.mm)(6.5.16 6.5.23 6.6.1 6.6.2)~~(2.0理论上支持任何微信版本,只测试了6.6.7,其他未测)
2. 只支持 Android 5.0 以上

# 效果预览
![main01](https://raw.githubusercontent.com/Blankeer/MDWechat/master/image/main01.png)
![main02](https://raw.githubusercontent.com/Blankeer/MDWechat/master/image/main02.png)
![main03](https://raw.githubusercontent.com/Blankeer/MDWechat/master/image/main03.png)
![main04](https://raw.githubusercontent.com/Blankeer/MDWechat/master/image/main04.png)
![main05](https://raw.githubusercontent.com/Blankeer/MDWechat/master/image/main05.png)
![chat01](https://raw.githubusercontent.com/Blankeer/MDWechat/master/image/chat01.png)

# 使用教程
有待整理文档到 wiki

# 存在的问题
1. 导致微信变卡,这是无法避免的
2. 悬浮按钮在某些机型(魅族/中兴)上显示异常,在聊天页面会显示

# 感谢
1. [WechatSpellbook](https://github.com/Gh0u1L5/WechatSpellbook)
2. [WechatUI](https://www.coolapk.com/apk/ce.hesh.wechatUI)
3. [群消息助手](https://github.com/zhudongya123/WechatChatroomHelper)
4. [WechatMagician](https://github.com/Gh0u1L5/WechatMagician)




