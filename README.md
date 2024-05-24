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
2. 全局异常处理：包装为ResultWrapper
3. 全局格式化：LocalDateTime、LocalDate、LocalTime使用常规格式；BigDecimal、Long使用字符串；
4. 全局Feign处理：自动将ResultWrapper里的数据取出

### 2.4工具类
1. PageUtil：分页工具。功能：将PageBO转为MyBatis-Plus的分页对象；将MyBatis-Plus的分页结果转为PageVO等；

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
| 配置  | 描述  | 默认值  |
| :------------ | :------------ | :------------ |
| suchtool.nicecommon.enableGlobalExceptionAdvice  | 启用全局异常处理  | true  |
| suchtool.nicecommon.enableGlobalResponseBodyAdvice  | 启用全局响应处理  | true  |
| suchtool.nicecommon.enableJacksonConfig  |  启用Jackson配置 | true  |
