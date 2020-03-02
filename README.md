# KillWechatInfoFlow 去他妈的微信信息流

关闭微信公众号信息流，恢复以前的列表样式。

## Bug 收集

各位用户烦请关注 Issue 列表，如发现相同的 Bug 请跟帖，发现新 Bug 请提交新 Issue。

提交 Issue 前请确保已经过测试：除待测试的模块外，关闭其余所有模块，确认问题仍存在。

提交 Issue 时请注明以下信息
+ 机型
+ 系统及 Android 版本（例如 H2OS Android 10）
+ 框架类型（EdXposed / 太极阴 / 太极阳 / rovo89 原版 Xposed 等）及版本
+ 微信详细版本（我-设置-关于微信-连续点击微信图标即可看到）以及是 Play 还是国内版
+ 视情况可能需要提供日志和提取的微信安装包

#### 目前 Open 的 Issue 列表
+ [#1](https://github.com/chouqibao/KillWechatInfoFlow/issues/1)：似乎多见于一加 7/7T 系列，这些机型的用户烦请关注一下，即使模块正常运行也请报告一下。

## 更新日志

##### 2020.03.02 v4.0  更改 hook 点
前述 `com.tencent.mm.storage.s.blj()` 在微信更新后方法名可能会改变，从而导致失效。查看该方法内部代码后，将 hook 点换为 `com.tencent.mm.sdk.platformtools.az.decodeInt("BizTimeLineOpenStatus", int)`，将该方法的返回值设为 `0` 即可。


##### 2020.03.02 v3.0  兼容 Play 版微信

##### 2020.02.21 v2.0  改变了实现方法
进入一个公众号时，若未读消息较多，则右上角会弹出一个“XX条新消息”的提示，点击可直接跳转到上次阅读位置。在用了上述替换 `intent` 的目标的方法后这一功能没有了。

在看了反编译出的源码后，发现在进入公众号列表时，会调用方法 `com.tencent.mm.storage.s.blj()`，若返回 `true` 则进入信息流界面，否则进入传统的列表界面。尝试 hook 该方法后，发现新消息数量提示条又回来了。

目前不确定 hook 该方法会不会带来什么后果。

##### 2020.02.08 v1.0
实现很简单，hook `startActivity`，拦截打开公众号信息流的 `intent`，将其替换为指向公众号列表即可。
