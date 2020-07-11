# smile-boot-starter

<div>
  <img src="https://img.shields.io/badge/%F0%9F%93%85%20Last%20update%20-%20July%2011%202020-green.svg" alt="Last update: July, 2020">
  <img src="https://img.shields.io/badge/%E2%9C%94%20Spring%20Boot%20Version%20-%202.3.1.RELEASE-brightgreen.svg" alt="Spring Boot Version 2.3.1.RELEASE">
</div>

超轻量级微服务基本支持框架

## TRACE ID
引入`smile-boot-starter`后，自动支持traceId，可由前端传入`traceId`到后端，也可由首个接收到请求的服务自动生成

默认拦截器为`com.el.smile.interceptor.BaseTraceInterceptor`

支持`spring cloud feign`和`dubbo`

`spring cloud feign`是由`SpringCloudFeignInterceptor`支持，由config类控制注入

`dubbo`是由`CloudRpcFilter`，注入依赖于`dubbo`的Filter SPI支持

提供全链路traceId支持，前端或者首个被访问的服务生成，之后本次请求全链路获取全局traceId

`LocalDataUtils.getTraceId()`

## Environment
项目启动自动激活Environment对象，获取当前工程的环境和应用名称，取值{spring.profiles.active} 和 {spring.profiles.name}

## EVENT LOGGER
概念： 微服务架构下，一个请求可能会有多个应用来承载，每个应用又有多个主机来集群部署，假设一次请求出现问题，我们需要跟踪一次请求，如果没有全局统一的一个ID，问题排查起来可能比较困难

smile-boot-starter提供极简模式快速让项目拥有traceId的跟踪能力
（一次请求全局拥有一个ID，聚合日志后可以通过该id查询本次请求的全埋点路径报告）

两步快速实现：
1. 引入依赖
![image](https://raw.githubusercontent.com/RadianceL/smile-boot-starter/master/images/WechatIMG1.png)
2. 对要埋点的方法添加注解
![image](https://raw.githubusercontent.com/RadianceL/smile-boot-starter/master/images/WechatIMG2.png)

即可获取到日志，默认情况下，日志打在spring boot jar目录下的event.log

![image](https://raw.githubusercontent.com/RadianceL/smile-boot-starter/master/images/WechatIMG3.png)

TODO
[ ] 日志参数可配置
[ ] 日志文件位置及滚动策略

日志聚合可以使用ELK，或者Loki
ELK比较重，上手有难度，但功能齐全
Loki最近新出的，好多人在推，我没有尝试过，看介绍感觉还可以，可以尝试


## 技术支持

项目个人维护，有使用问题，或者想共同开发都可以issus里留问题或者微信，周末会看

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

