
# 第五章 Kubernetes入门及集群搭建
## 学习目标：
1. 了解 Kubernetes 的起源和发展
2. 掌握如何使用 kubeadm搭建 Kubernetes 集群
3. 查看 Kubernetes 集群部署状态

# 5-1 Kubernetes的起源和发展 
## Kubernetes 的起源

- Kubernetes最初源于谷歌内部的Borg，Kubernetes 的最初目标是为应用的容器化编排部署提供一个最小化的平台，包含几个基本功能：
1. 将应用水平扩容到多个集群
2. 为扩容的实例提供负载均衡的策略
3. 提供基本的健康检查和自愈能力
4. 实现任务的统一调度



## Kubernetes 的发展
- 2014年6月 谷歌云计算专家Eric Brewer在旧金山的发布会为这款新的开源工具揭牌。
- 2015年7月22日K8S迭代到 v 1.0并在OSCON大会上正式对外公布。
- 为了建立容器编排领域的标准和规范，Google、RedHat 等开源基础设施领域玩家们，在 2015 年共同牵头发起了名为 CNCF（Cloud Native Computing Foundation）的基金会。Kubernetes 成为 CNCF 最核心的项目。发起成员：AT&T, Box, Cisco, Cloud Foundry Foundation, CoreOS, Cycle Computing, Docker, eBay, Goldman Sachs, Google, Huawei, IBM, Intel, Joyent, Kismatic, Mesosphere, Red Hat, Switch SUPERNAP, Twitter, Univa, VMware and Weaveworks。
- 2018年，超过 1700 开发者成为 Kubernetes 项目社区贡献者，全球有 500 多场沙龙。国内出现大量基于 Kubernetes 的创业公司。
- 2020 年，Kubernetes 项目已经成为贡献者仅次于 Linux 项目的第二大开源项目。成为了业界容器编排的事实标准，各大厂商纷纷宣布支持 Kubernetes 作为容器编排的方案。

# 5-2 为什么需要 Kubernetes？
## 传统的容器编排痛点
容器技术虽然解决了应用和基础设施异构的问题，让应用可以做到一次构建，多次部署，但在复杂的微服务场景，单靠 Docker 技术还不够，它仍然有以下问题没有解决：
- 集成和编排微服务模块
- 提供按需自动扩容，缩容能力
- 故障自愈
- 集群内的通信
## Kubernetes 能解决的问题
- 按需的垂直扩容，新的服务器(node)能够轻易的增加或删除
- 按需的水平扩容，容器实例能够轻松扩容，缩容
- 副本控制器，你不用担心副本的状态
- 服务发现和路由
- 自动部署和回滚，如果应用状态错误，可以实现自动回滚

## 什么时候使用 Kubernetes？
- 当你的应用是微服务架构
- 开发者需要快速部署自己的新功能到测试环境进行验证
- 降低硬件资源成本，提高使用率

## 什么时候不适合使用 Kubernetes
- 应用是轻量级的单体应用，没有高并发的需求
- 团队文化不适应变革

# 5-3 Kubernetes 的架构和核心概念
## 主控制节点组件
主控制节点组件对集群做出全局决策(比如调度)，以及检测和响应集群事件（例如，当不满足部署的 replicas 字段时，启动新的 pod）。

主控制节点组件可以在集群中的任何节点上运行。 然而，为了简单起见，设置脚本通常会在同一个计算机上启动所有主控制节点组件，并且不会在此计算机上运行用户容器。
- apiserver
主节点上负责提供 Kubernetes API 服务的组件；它是 Kubernetes 控制面的前端组件。
- etcd
etcd 是兼具一致性和高可用性的键值数据库，可以作为保存 Kubernetes 所有集群数据的后台数据库。

- kube-scheduler
主节点上的组件，该组件监视那些新创建的未指定运行节点的 Pod，并选择节点让 Pod 在上面运行。
调度决策考虑的因素包括单个 Pod 和 Pod 集合的资源需求、硬件/软件/策略约束、亲和性和反亲和性规范、数据位置、工作负载间的干扰和最后时限。
- kube-controller-manager
在主节点上运行控制器的组件。
从逻辑上讲，每个控制器都是一个单独的进程，但是为了降低复杂性，它们都被编译到同一个可执行文件，并在一个进程中运行。这些控制器包括:
    1. 节点控制器（Node Controller）: 负责在节点出现故障时进行通知和响应。
    3. 副本控制器（Replication Controller）: 负责为系统中的每个副本控制器对象维护正确数量的 Pod。
    3. 终端控制器（Endpoints Controller）: 填充终端(Endpoints)对象(即加入 Service 与 Pod)。
    4. 服务帐户和令牌控制器（Service Account & Token Controllers），为新的命名空间创建默认帐户和 API 访问令牌.


## 从节点组件
节点组件在每个节点上运行，维护运行的 Pod 并提供 Kubernetes 运行环境。

- kubelet 
一个在集群中每个节点上运行的代理。它保证容器都运行在 Pod 中。

kubelet 接收一组通过各类机制提供给它的 PodSpecs，确保这些 PodSpecs 中描述的容器处于运行状态且健康。kubelet 不会管理不是由 Kubernetes 创建的容器。

- kube-proxy
kube-proxy 是集群中每个节点上运行的网络代理,实现 Kubernetes Service 概念的一部分。
kube-proxy 维护节点上的网络规则。这些网络规则允许从集群内部或外部的网络会话与 Pod 进行网络通信。

- 容器运行时（Container Runtime）
容器运行环境是负责运行容器的软件。
Kubernetes 支持多个容器运行环境: Docker、 containerd、cri-o、 rktlet 以及任何实现 Kubernetes CRI (容器运行环境接口)。

## 插件（Addons）
- DNS
尽管其他插件都并非严格意义上的必需组件，但几乎所有 Kubernetes 集群都应该有集群 DNS， 因为很多示例都需要 DNS 服务。
- Web 界面（仪表盘）
Dashboard 是K ubernetes 集群的通用的、基于 Web 的用户界面。 它使用户可以管理集群中运行的应用程序以及集群本身并进行故障排除。

- 容器资源监控
容器资源监控 将关于容器的一些常见的时间序列度量值保存到一个集中的数据库中，并提供用于浏览这些数据的界面。

- 集群层面日志
集群层面日志 机制负责将容器的日志数据 保存到一个集中的日志存储中，该存储能够提供搜索和浏览接口。

# 5-4 Kubernetes 的部署方案介绍

## 部署目标
- 在所有节点上安装Docker和kubeadm
- 部署Kubernetes Master
- 部署容器网络插件
## 部署架构
|   ip  |  域名  | 备注| 安装软件|
|  ----  | ----  |----  |----  |
|  192.168.99.101 | master | 主节点 |Docker Kubeadm kubelet kubectl flannel |
|  192.168.99.102 | node1 |从节点 1 |Docker Kubeadm kubelet kubectl |
|  192.168.99.103 | node2 |从节点 2 |Docker Kubeadm kubelet kubectl|
## 环境准备
- 3台虚拟机CentOS7.x-86_x64
- 硬件配置：2GB或更多RAM，2个CPU或更多CPU，硬盘30GB或更多
- 集群中所有机器之间网络互通
- 可以访问外网，需要拉取镜像
- 禁止swap分区

# 5-5 Virtualbox 虚拟机配置双网卡实现固定IP
- Virtualbox安装 CentOS
- 配置虚机双网卡,实现固定 IP，且能访问外网
网卡 1： 仅主机host-only
网卡 2： 网络转换地址NAT
查看虚拟机网络，点击管理—>主机网络管理器，记住ip地址（192.168.99.1），并选择“手动配置网卡”。
- 重启虚拟机，此时在虚拟机 ping www.baidu.com 是返回成功的。
- 设置外部网络访问虚拟机
设置静态ip地址，编辑网络配置文件，编辑网络设置文件
```
vi /etc/sysconfig/network-scripts/ifcfg-enp0s3
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
#BOOTPROTO=dhcp
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=enp0s3
UUID=08012b4a-d6b1-41d9-a34d-e0f52a123e7a
DEVICE=enp0s3
ONBOOT=yes
BOOTPROTO=static
IPADDR=192.168.99.101
```
- 重启网络
```
systemctl restart network
```

- 查看 enp0s3 网卡的 ip
```
[root@localhost Final]#ip addr |grep 192
inet 192.168.99.101/24 brd 192.168.99.255 scope global noprefixroute enp0s3
```
- 此时虚拟机既可以访问外网，也能够和宿主机( 192.168.31.178)进行通信
```
ping 192.168.31.178
PING 192.168.31.178 (192.168.31.178): 56 data bytes
64 bytes from 192.168.31.178: icmp_seq=0 ttl=64 time=0.060 ms
```
- 使用iTerm2 连接虚拟机


# 5-6 配置虚拟机 Yum 源，iptables
- 删除原有的yum源：
```
rm -f /etc/yum.repos.d/*
```
- 重新下载阿里云的yum源：
```
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo

```

- 列出yum各软件包：
```
yum list
```
- 清除缓存：
```
yum clean all
```

# 5-7 Master节点安装 kubeadm, kubelet and kubectl

- 安装基本软件包
```
[root@localhost ~]# yum install wget net‐tools vim bash‐comp* ‐y

```

- 设置主机名，管理节点设置主机名为master

```
[root@master ~]# hostnamectl set‐hostname master
[root@master ~]# su ‐
```
- 配置 Master 和 work 节点的域名
```
vim /etc/hosts
 192.168.99.101 master
 192.168.99.102 node1
 192.168.99.103 node2
```
- 关闭 防火墙
```
systemctl stop firewalld
systemctl disable firewalld
```

- 关闭 SeLinux
```
setenforce 0
sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config
```

- 关闭 swap
```
swapoff -a
yes | cp /etc/fstab /etc/fstab_bak
cat /etc/fstab_bak |grep -v swap > /etc/fstab
```

- 配置Docker, K8S的阿里云yum源
```
[root@master ~]# cat >>/etc/yum.repos.d/kubernetes.repo <<EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
[root@master ~]# wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
[root@master ~]# yum clean all
[root@master ~]# yum repolist
```
- 安装并启动 docker
```
yum install -y docker-ce.x86_64 docker-ce-cli.x86_64 containerd.io.x86_64

mkdir /etc/docker

cat > /etc/docker/daemon.json <<EOF
{
  "registry-mirrors": ["https://registry.cn-hangzhou.aliyuncs.com"],
  "exec-opts": ["native.cgroupdriver=systemd"]
}
EOF
```
- 编辑/usr/lib/systemd/system/docker.service
```
ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock --exec-opt native.cgroupdriver=systemd
```
```
# Restart Docker
systemctl daemon-reload
systemctl enable docker
systemctl restart docker
```
此时查看 docker info，可以看到默认 Cgroup Driver为 systemd

- 卸载旧版本
```
yum remove -y kubelet kubeadm kubectl
```
- 安装kubelet、kubeadm、kubectl
```
yum install -y kubelet.x86_64 kubeadm.x86_64 kubectl.x86_64
```

- 重启 docker，并启动 kubelet
```
systemctl enable kubelet && systemctl start kubelet
```

# 5-8 初始化Master 节点
- 将桥接的IPv4流量传递到iptables的链
```
echo "1" >/proc/sys/net/bridge/bridge-nf-call-iptables
vi /etc/sysctl.d/k8s.conf 
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1 
```
- 初始化主节点
```
kubeadm init --kubernetes-version=1.19.2 \
--apiserver-advertise-address=10.168.99.101 \
--image-repository registry.aliyuncs.com/google_containers \
--service-cidr=10.1.0.0/16 \
--pod-network-cidr=10.244.0.0/16

mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
- 安装网络插件 Flannel
```
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
```
- 查看是否成功创建flannel网络
```
ifconfig |grep flannel
```

- 重置 kubeadm
```
kubeadm reset
```

# 5-9 安装配置 worker Node节点
- 初始虚拟机，Centos，配置双网卡
![图片描述](//img.mukewang.com/wiki/5f81ad9709adac0715340828.jpg)
![图片描述](//img.mukewang.com/wiki/5f81ada70971ea2315140770.jpg)
注意 clone snapshot 虚拟机时，选择'Generate new MAC address'。
- ssh 免密登录
- 设置 ip 地址为 192.168.99.102
- 配置域名
```
hostnamectl set-hostname node1
vi /etc/hosts
192.168.99.101 master
192.168.99.102 node1
192.168.99.103 node2
```
- 配置阿里云 yum 源
```
[root@master ~]# cat >>/etc/yum.repos.d/kubernetes.repo <<EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
[root@master ~]# wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
[root@master ~]# yum clean all
[root@master ~]# yum repolist
```
- 安装基础软件
```
yum install bash‐comp* vim net‐tools wget ‐y
```
- 安装 Docker，Kubeadm，Kubectl，kubelet
```
yum install docker-ce -y
systemctl start docker
systemctl enable docker
yum install kubelet kubeadm kubectl -y
systemctl enable kubelet
```
- kubadm join 加入集群
```
kubeadm join 192.168.99.101:6443 --token vrqf1w.dyg1wru7nz0ut9jz    --discovery-token-ca-cert-hash sha256:1832d6d6c8386de5ecb1a7f512cfdef27a6d14ef901ffbe7d3c01d999d794f90
```
默认token的有效期为24小时，当过期之后，该token就不可用了。解决方法如下：

重新生成新的token，在master端执行
```
kubeadm token create --print-join-command
```
- 将 master 节点的 admin.conf 拷贝到 node1
```
scp /etc/kubernetes/admin.conf root@node1:/etc/kubernetes/
```
- 配置 Kubeconfig 环境变量
```
echo "export KUBECONFIG=/etc/kubernetes/admin.conf" >> ~/.bash_profile
source ~/.bash_profile
```
- 安装 flannel 网络插件
```
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
```
- 将master节点下面 /etc/cni/net.d/下面的所有文件拷贝到node节点上

在node1节点上面创建目录：
`mkdir -p /etc/cni/net.d/`

在master： 
`scp /etc/cni/net.d/* root@nodeip:/etc/cni/net.d/`

执行命令：
`kubectl get nodes 查看 node 节点处于ready状态`

- 检查集群状态
稍等几分钟，在master节点输入命令检查集群状态.
```
kubectl get nodes
```

