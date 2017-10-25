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