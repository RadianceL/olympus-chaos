# smile-boot-starter

<div>
  <img src="https://img.shields.io/badge/%F0%9F%93%85%20Last%20update%20-%20July%2011%202020-green.svg" alt="Last update: July, 2020">
  <img src="https://img.shields.io/badge/%E2%9C%94%20Spring%20Boot%20Version%20-%202.3.1.RELEASE-brightgreen.svg" alt="Spring Boot Version 2.3.1.RELEASE">
</div>

Basic support framework for ultra-lightweight microservices

## TRACE ID
Concept: under the microservice architecture, a request may be hosted by multiple applications, and each application may have multiple hosts for cluster deployment. 
Suppose a request has a problem, we need to track a request. If there is no globally unified ID, it may be difficult to troubleshoot the problem

`smile-boot-starter`Provide a minimalist mode to quickly enable the project to have the tracking ability of `trace-id` 
(one ID is requested globally at a time. After aggregating the log, you can query the requested full buried point path report through this ID)

Automatic integration`smile-boot-logger`，Output tracking log for log embedding point

import`smile-boot-starter`，traceId will be auto support，The `trace-id` can be passed in from the front end to the back end, or automatically generated by the first service that receives the request
Support `spring cloud feign` and `Dubbo`, not invade code, and automatically support when introducing with maven

Full link `trace-id` support is provided, and the front end or the first accessed service is generated. Then this time, the full link is requested through `LocalDataUtils.getTraceId()` get the global `trace-id`

## Environment
Project startup automatically activates the `Environment` object to obtain the environment and application name of the current project，
Environment init with `{spring.profiles.active}` and `{spring.profiles.name}`

EventLogger it also depends on this class to output the environment, so these two configurations are required in spring configuration
```java
public class EnvironmentTemplate {

    public static void main(String[] args){
        Environment.getInstance().isDaily();
        Environment.getInstance().isStaging();
        Environment.getInstance().isProd();
    }
}
```
## QUICK START
Two step fast implementation：
1. import dependence
```xml
<dependency>
	<groupId>com.el</groupId>
	<artifactId>smile-boot-starter</artifactId>
	<version>${smile-boot-starter.last-version}</version>
</dependency>
```
2. application.yml configuration
```yaml
spring:
  profiles:
    # DAILY｜STAGING｜PROD
    # if env is not in (DAILY｜STAGING｜PROD) then use new Environment(applicationName, environment); to map current environment
    active: STAGING
  application:
    name: trace-test
  smile-boot:
    trace-logger:
      # log path: 
      # absolute - absolute: /root/admin/runtime/trace-test/log
      # relative - relative: log -> /root/admin/runtime/trace-test/log
      log-path: relative:log
      # event -> /root/admin/runtime/trace-test/log/event.log
      log-file-name: event
      # 2020-07-12 00:33:32 - msg
      pattern: "%d{yyyy-MM-dd HH:mm:ss} %msg%n"
```
2. Annotate the method of embedding points
```java
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SmsSenderController {

    private final TraceIdServiceProvider traceIdServiceProvider;
   
    
    @GetMapping("/test/api")
    @EventTrace(event = "for test", loggerType = LoggerType.JSON)
    public String test() {
        log.info(LocalDataUtils.getTraceId());
        SmileLocalUtils.setIsSucess(true);
        return traceIdServiceProvider.test();
    }

}
```
Then you can get the log. By default, the log is printed in spring boot jar path->event log
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
    "event":"test",
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

and ，traceId will return to response header，developer in Chrome or other browser console to get it

3. EventLogger
For some important nodes that need to be buried, event logs can be used to record. Look at the following example
   
```java
/**
 * since 2020/1/4
 *
 * @author eddie
 */
@Slf4j
@RestController
@RequirePermissions
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestConfigController {

    @RequestMapping("/get")
    @EventTrace(event = "for test", loggerType = LoggerType.FORMAT)
    public String get() {
        SmileEventLogger.info("test event logger");
        return "SmileLocalUtils.getTraceId();";
    }
}
```

out put：
```log
2020-12-15 00:21:48 INFO  - 20201215002148-2706c6d56ffe4197a1e5c58d666596f7 - [com.el.smile.logger.logger.SmileEventLogger.info] line [30]: test event logger
```
