# smile-boot-starter

<div>
  <img src="https://img.shields.io/badge/%F0%9F%93%85%20Last%20update%20-%20July%2011%202020-green.svg" alt="Last update: July, 2020">
  <img src="https://img.shields.io/badge/%E2%9C%94%20Spring%20Boot%20Version%20-%202.3.1.RELEASE-brightgreen.svg" alt="Spring Boot Version 2.3.1.RELEASE">
</div>

超轻量级微服务基本支持框架

## TRACE ID
概念： 微服务架构下，一个请求可能会有多个应用来承载，每个应用又有多个主机来集群部署，假设一次请求出现问题，我们需要跟踪一次请求，如果没有全局统一的一个ID，问题排查起来可能比较困难

`smile-boot-starter`提供极简模式快速让项目拥有traceId的跟踪能力（一次请求全局拥有一个ID，聚合日志后可以通过该id查询本次请求的全埋点路径报告）

自动集成`smile-boot-logger`，对日志埋点输出跟踪日志

引入`smile-boot-starter`后，自动支持traceId，可由前端传入`traceId`到后端，也可由首个接收到请求的服务自动生成
支持`spring cloud feign`和`dubbo`, 不需要任何配置，引入jar即自动支持

提供全链路traceId支持，前端或者首个被访问的服务生成，之后本次请求全链路通过`LocalDataUtils.getTraceId()`获取全局traceId

## Environment
项目启动自动激活Environment对象，获取当前工程的环境和应用名称，取值{spring.profiles.active} 和 {spring.profiles.name}
EventLogger也会依赖这个类输出环境，所以spring 配置中需要这两个配置
```java
public class EnvironmentTemplate {

    public static void main(String[] args){
        // 是否是日常环境
        Environment.getInstance().isDaily();
        // 是否是预发环境
        Environment.getInstance().isStaging();
        // 是否是线上环境
        Environment.getInstance().isProd();
    }
}
```
## QUICK START
两步快速实现：
1. 引入依赖
```xml
<dependency>
	<groupId>com.el</groupId>
	<artifactId>smile-boot-starter</artifactId>
	<version>${smile-boot-starter.last-version}</version>
</dependency>
```
2. application.yml配置
```yaml
spring:
  profiles:
    # 配置运行环境 可选DAILY｜STAGING｜PROD，与application.yml多环境配置不冲突
    active: STAGING
  application:
    # 应用名称
    name: trace-test
  smile-boot:
    trace-logger:
      # 日志路径配置，两种配置方式： 例如fatjar位置在/root/admin/runtime/trace-test
      # absolute - 取绝对路径，absolute: /root/admin/runtime/trace-test/log
      # relative - 取项目运行的相对路径，relative: log -> /root/admin/runtime/trace-test/log
      log-path: relative:log
      # 日志文件名 event -> /root/admin/runtime/trace-test/log/event.log
      log-file-name: event
      # 2020-07-12 00:33:32 - msg
      pattern: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```
2. 对要埋点的方法添加注解
```java
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SmsSenderController {

    private final TraceIdServiceProvider traceIdServiceProvider;
   
    
    @GetMapping("/test/api")
    @EventTrace(event = "测试", loggerType = LoggerType.JSON)
    public String test() {
        log.info(LocalDataUtils.getTraceId());
        SmileLocalUtils.setIsSucess(true);
        return traceIdServiceProvider.test();
    }

}
```
即可获取到日志，默认情况下，日志打在spring boot jar目录下的event.log
```text
FORMAT:
2020-07-12 00:30:16 - TRACE LOG - 
traceId [20200712003016-b20bc50af8824d988d025a5a17124dd8], 
appName [trace-test], 
env [STAGING], 
ip [115.220.204.114], 
event [test - /provider/test/api], 
method [com.landscape.user.repository.TraceIdServiceProvider.test()], 
success [true], 
costTime [4], 
parameter [without parameter], 
response ["success 20200712003016-b20bc50af8824d988d025a5a17124dd8"], 
features [{LOGGER_IS_SUCCESS=true}]

JSON:
2020-07-12 00:30:16 - {
    "appName":"trace-test",
    "costTime":4,
    "env":"STAGING",
    "event":"测试",
    "features":{
        "LOGGER_IS_SUCCESS":"true"
    },
    "ip":"115.220.204.114",
    "method":"com.landscape.user.repository.TraceIdServiceProvider.test()",
    "parameter":"without parameter",
    "result":"\"success 20200712003332-419cdb0d285e433daac7bebbfb88fca3\"",
    "success":true,
    "traceId":"20200712003332-419cdb0d285e433daac7bebbfb88fca3"
}
```

TODO 需求定义:

2020-06-30
- [x] 日志参数可配置
- [x] 日志文件位置及滚动策略

2020-07-25
- [ ] bug修复： 对当前请求设置一次success将覆盖当前应用所有日志
- [ ] 增加Dapper规范的RPC-id
- [ ] trace日志中增加rpcId
- [ ] 增加业务日志

日志聚合可以使用ELK，或者Loki <br/>
ELK比较重，上手有难度，但功能齐全 <br/>
Loki最近新出的，好多人在推，我没有尝试过，看介绍感觉还可以，可以尝试

## 技术支持
项目个人维护，有使用问题，或者想共同开发都可以issues里留问题或者微信，周末会看

## 更新记录
2020-07-11 --------- 0.0.1-SNAPSHOT
1. 日志输出可配置
2. 获取公网IP
3. 增加日志类型控制 FORMAT ｜ JSON

PS：最近更新了Mac 10.16（BIG SUR）系统，各种问题，又懒得退回，就慢点搞吧

主要依赖
- spring boot自动装配
- alibaba fastjson
- lombok

