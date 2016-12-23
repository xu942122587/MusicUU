# 音乐助手
一个免费下载音乐的App
[问题交流反馈社区](http://qtfreet.cn)  

####使用到的开源项目
#####网络请求框架
[retrofit2](https://github.com/square/retrofit)  
#####json解析
[Gson](https://github.com/google/gson)  
#####视频播放
[ijkplayer](https://github.com/Bilibili/ijkplayer)  
#####图片加载
[picasso](https://github.com/square/picasso)  
#####歌词显示
[LrcView](https://github.com/WuLiFei/LyricViewDemo)

######2016.8.18
* 移除掉了主页面的搜索按钮，太丑了，把搜索放在了搜索框右侧的搜索图标和软键盘上
* 更改了选择音乐源的样式
* 播放mv改为全屏了
* 歌曲下载放到后台了
* 支持显示歌词了
* 还是不支持后台播放，- -

######2016.9.1
* 调整了下搜索歌曲页面的布局大小，现在可以显示更多的选项
* 增加了音悦台接口，可以直接看mv
* 越写越觉得代码丑，丑炸了

######2016.9.9
* 封装部分代码到BaseActivity
* 去除一个崩溃bug

######2016.9.19
* 移除搜索界面右上角小三点，避免屏幕适配问题
* 现在在搜索条目上向左滑动可显示下载，mv按钮
* 修改部分提示

######2016.12.6
* 修复自动更新导致的崩溃bug
* 优化程序性能
* 优化部分UI
* 修复bug
* 因旧版api已经停用，导致搜索会崩溃，更及时更新到最新版
* 恢复下载音乐时选择音质选项

######2016.12.21
* 修复播放时无限下一首的问题（已知问题，加载时圆圈不动）
* 统一网络框架，优化代码
* 更新下载功能

![image](https://raw.githubusercontent.com/Qrilee/MusicUU/master/screenshots/pic.png)

捐赠：愿意支持开发的可以支持下哈
![image](http://qtfreet.com/zfb.png) ![image](http://qtfreet.com/wx.png)

