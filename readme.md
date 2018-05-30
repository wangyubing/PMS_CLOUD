# 分布式服务

## 微服务学习网址
https://blog.csdn.net/forezp/article/details/70148833/

## 服务注册发现


## 配置服务
http请求地址和资源文件映射如下:
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties

## 路由

## 多模块打包命令
配合pom的配置，使用命令：
mvn clean package 打包全部关联模块
mvn -pl A -am install 单独打包某个模块
参考地址：https://blog.csdn.net/Ser_Bad/article/details/78433340

## 注意事项
1、静态文件，读取配置信息要使用set get的方法，并且头部要有@Compent的注解
2、
create by alean
