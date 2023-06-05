# MyCloud Project

## email-starter 模块 (deprecated)

### 定位

邮件发送模块

由于业务没有复杂到需要自定义复杂的发送规则，

将不久后**弃用**此模块，转向 ``spring-boot-starter-mail``

### 引入方式：

maven:
```xml
<dependency>
    <groupId>org.looko.mycloud</groupId>
    <artifactId>email-starter</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```

gradle:
```groovy
implementation("org.looko.mycloud:email-starter:0.0.2-SNAPSHOT")
```

### 功能使用

#### 配置方式
application.properties
```properties
# 可配置多个来自不同类型邮箱的发件人
mycloud.email.configs[0].host=smtp.qq.com
mycloud.email.configs[0].senderEmail=abc@qq.com
mycloud.email.configs[0].senderPassword=xxxyyy
mycloud.email.configs[1].host=smtp.163.com
mycloud.email.configs[1].senderEmail=abc@163.com
mycloud.email.configs[1].senderPassword=xxxyyy
```
application.yml
```yaml
mycloud:
  email:
    config:
      - host: smtp.qq.com
        senderEmail: abc@qq.com
        senderPassword: xxxyyy
      - host: smtp.163.com
        senderEmail: abc@163.com
        senderPassword: xxxyyy
```

#### 使用方式

使用 ``EmailManager.sendEmail()`` 发送邮件