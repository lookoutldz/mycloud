# MyCloud Project

## mail 模块

### 定位

邮件模块, 用于处理发邮件有关业务

### 启动参数

JVM 参数
```text
-Xms256m
-Xmx256m
-XX:+HeapDumpBeforeFullGC
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/Users/$USER/Workspaces/IdeaProjects/mycloud/user/resources/heapdump
-javaagent:/Users/$USER/Workspaces/IdeaProjects/mycloud/skywalking-agent/skywalking-agent.jar
-Dskywalking.agent.service_name=cloud-mail-01
-Dskywalking.logging.file_name=cloud-mail-01-api.log
```