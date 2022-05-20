# KubeBlog
一个博客系统，由Java Spring Boot framework + MySQL database + Semantic UI 组成。目的是将该博客运行在 Kubernetes 环境里，从而学习和实践 Kubernetes 的概念。


# 博客项目环境准备
## 硬软件配置
|  软件   | 访问路径  | 备注  |
|  ----  | ----  |----  |
| 硬软件  | 4 核 16G 内存+VirtualBox 6.1 |注：也可以用 2 台 2 核 8G Centos 虚拟机 |
| mysql  | localhost:3306 |root/password |
| mvn  | 3.6.2 | |
| JDK  | 1.8.0_25 | |



## 域名映射
|  域名   |  ip  | 访问路径  |
|  ----  | ----  |----  |
|  127.0.0.1  | art.local | http://art.local:8081 |
|  192.168.99.101 | master | |
|  192.168.99.102 | node1 | |
|  192.168.99.103 | node2 | |