# nicecommon

## 1.介绍
nicecommon：超好用的Java公共项目。

## 2.功能

### 2.1公共类

1. PageBO：分页入参
2. PageVO：分页返回
3. CommonEntity：数据库公共实体类
4. ResultWrapper：结果封装

### 2.2异常类

1. BusinessException：业务异常
2. SystemException：系统异常
3. AuthenticationException：认证异常（比如：token不正确或缺少token）
4. AuthorizationException：授权异常（比如：有token，但无权访问此URL）
5. CustomCodeException：自定义错误码的异常

### 2.3全局处理
1. 全局响应处理：包装为ResultWrapper
2. 全局异常处理：处理异常、打印异常日志、包装为报错的ResultWrapper
3. 全局格式化：LocalDateTime、LocalDate、LocalTime的格式化；BigDecimal、Long使用字符串；
4. 全局Feign处理：自动将ResultWrapper里的数据取出

### 2.4工具类
1. PageUtil：分页工具。功能：将PageBO转为MyBatis-Plus的分页对象；将MyBatis-Plus的分页结果转为PageVO等；

### 2.5注解
1.ResultWrapperIgnore：不自动包装ResultWrapper（用于接口方法）

### 2.6 自动组件
1.NiceCommonMybatisFillUpdateSQLProvider

提供一个Bean实现此接口的provideParam方法，即可将字段自动填充到UPDATE语句中。
provideParam返回值是一个Map，key：字段名，value：字段值。
备注：此法可以解决某些情况下update_time等自动填充失效的问题。

## 快速使用

### 1.引入依赖
```xml
<dependency>
    <groupId>com.suchtool</groupId>
    <artifactId>nicecommon-spring-boot-starter</artifactId>
    <version>{newest-version}</version>
</dependency>

```
## 配置
yml配置如下：
```yaml
suchtool:
  nicecommon:
    global-exception:
      enable: true  # 启用全局异常处理。默认值: true
      enable-log: true  # 启用全局异常处理的日志打印。默认值: true
      advice-order: 20000  # 全局异常处理的切面顺序。默认值: 20000
    global-response:
      enable: true  # 启用全局响应处理。默认值: true
      advice-order: true  # 全局响应处理的切面顺序。默认值: true
      # 全局响应忽略处理的URL，用AntPathMatcher实现，支持通配符，例如：/webjars/**。
      # 默认值: 空。（内部已自动跳过swagger相关url）
      ignore-url:
        - /test/test1
        - /test/test2
    global-format:
      enable-number-to-string: true  # 全局格式处理启用数字转字符串。默认值: true
      date-time-format-type: pretty  # 全局格式处理的日期时间格式。取值：pretty（年月日 时分秒）、timestamp（时间戳）、none。默认值: pretty
    jackson:
      enable-config: true # 启用jackson配置。默认值：true。若关闭，则global-format的json格式化也会失效
      enable-avoid-fail: true #启用避免报错功能。默认值：true
```