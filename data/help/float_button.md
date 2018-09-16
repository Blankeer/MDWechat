# 悬浮按钮定制教程

## 路径
`/sdcard/mdwechat/config/view/floatbutton.txt`

## 文件说明
1. 文件采用 json 语法，修改必须遵循该格式，否则解析失败，不会显示。
入门语法教程：[菜鸟教程](http://www.runoob.com/json/json-tutorial.html)
2. 字段说明
- order: 顺序，越小越靠上
- type：跳转后的类名
- text：显示名称
- icon：图标名称

## icon 说明
所有图标路径在 `/sdcard/mdwechat/icon/`下，如果不存在该图标，不会显示。

## type 说明
type 为跳转后的 Activity 完整类名，可以借助 开发者助手、auto.js等工具查看。
以下是常见类名：

名称 | 类名
---- | ---
小程序 | com.tencent.mm.plugin.appbrand.ui.AppBrandLauncherUI
朋友圈 | com.tencent.mm.plugin.sns.ui.SnsTimeLineUI
扫一扫 | com.tencent.mm.plugin.scanner.ui.BaseScanUI
收付款 | com.tencent.mm.plugin.offline.ui.WalletOfflineCoinPurseUI
钱包 | com.tencent.mm.plugin.mall.ui.MallIndexUI
收藏 | com.tencent.mm.plugin.fav.ui.FavoriteIndexUI
群聊 | com.tencent.mm.ui.contact.SelectContactUI
添加好友 | com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI

特殊 type：

type | 说明
---- | ---
weiX | 微x模块

## 建议
如果以上教程看完，还是一脸懵逼，建议你还是放弃吧。