# KillWechatInfoFlow

关闭微信公众号信息流，恢复以前的列表样式。

实现很简单，hook `startActivity`，拦截打开公众号信息流的 `intent`，将其替换为指向公众号列表即可。

## 更新日志

2020.02.08 v1.0

目前在微信 Google Play 版 7.0.10 版测试通过。
