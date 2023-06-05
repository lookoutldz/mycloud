# MyCloud Project

## common-starter 模块

### 定位

通用性高的公共资源模块

### 引入方式：

maven:
```xml
<dependency>
    <groupId>org.looko.mycloud</groupId>
    <artifactId>common-starter</artifactId>
    <version>0.0.19-SNAPSHOT</version>
</dependency>
```

gradle:
```groovy
implementation("org.looko.mycloud:common-starter:0.0.19-SNAPSHOT")
```

### 功能使用

#### 1. 实体/枚举/异常/注解 等公共资源直接使用

#### 2. 开启全局 Controller 统一返回实体 ResponseEntity<T>, 配置方式
application.properties
```properties
mycloud.common.controller.globalResponseWrapping=true
```
application.yml
```yaml
mycloud:
  common:
    controller:
      globalResponseWrapping: true
```