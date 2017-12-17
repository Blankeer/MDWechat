配置文件是 floatbutton.txt, JSON 格式,不懂的人不要乱改, 格式改乱了解析会失败
所有的图标都是在/sdcard/mdwechat/icon 目录下,图标尺寸最好一致
可以把你想要使用的图标复制到这个目录,后面使用,名称必须一致!
比如,复制  aaaa.png 到这个目录 , floatbutton.txt 可以使用 `"icon": "aaaa.png"`
下面是说明:
{
  "info": "自定义悬浮按钮",//忽略
  "menu": {
    "icon": "ic_add.png"//收起时加号的图标
  },
  "items": [
    {
      "order": 1,//顺序,越小越靠上
      "type": "walletcoin",//类型,可选值在后面,只能是可选值中的一个
      "text": "收付款",//显示文本,可以随便改
      "icon": "ic_money.png"//图标,必须存在
    },
    ...//省略后面的,也可以自定义个数,在后面加就行了
  ]
}
type 可选值
search:搜索
timeline:朋友圈
walletcoin:收付款
scan:扫一扫
addfriend:添加好友
appbrand:小程序
mywallet:钱包
favorite:收藏
chatgroup:群聊

新增自定义 type: 可以是 plugin 页面的 class 全名
形式必须是: com.tencent.mm.plugin.*.ui.* ,其他包下的 Activity 不支持
比如: 发朋友圈页面是 com.tencent.mm.plugin.sns.ui.En_c4f742e5
只能是无参数的页面,好友个人资料页面就不行
支持的页面,举个例子: 设置 我的个人信息页 卡包 我的表情 公众号 等
以上具体未测试
Activity class 名称的获取需要借助其他工具,比如: AutoJS,开发者助手


