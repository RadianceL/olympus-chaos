# smile-boot-starter

<div align="center">
  <img src="https://img.shields.io/badge/%F0%9F%93%85%20Last%20update%20-%20March%2012%202020-green.svg" alt="Last update: March, 2020">
  <img src="https://img.shields.io/badge/%E2%9C%94%20Spring%20Boot%20Version%20-%202.3.1.RELEASE-brightgreen.svg" alt="Spring Boot Version 2.3.1.RELEASE">
</div>

超轻量级微服务基本支持框架

## TRACE ID
引入`smile-boot-starter`后，自动支持traceId，可由前端传入`traceId`到后端，也可由首个接收到请求的服务自动生成

默认拦截器为`com.el.smile.interceptor.BaseTraceInterceptor`

支持`spring cloud feign`和`dubbo`

`spring cloud feign`是由`SpringCloudFeignInterceptor`支持，由config类控制注入

`dubbo`是由`CloudRpcFilter`，注入依赖于`dubbo`的Filter SPI支持

## Environment
项目启动自动激活Environment对象，获取当前工程的环境和应用名称，取值{spring.profiles.active} 和 {spring.profiles.name}