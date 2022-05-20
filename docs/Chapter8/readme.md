# 4.1 深入理解容器镜像中心
- Dockerhub
https://registry.hub.docker.com/
- 配置国内 Docker 镜像源
```
vi  /etc/docker/daemon.json
#修改后如下：
{
    "registry-mirrors": ["https://registry.docker-cn.com"],
    "live-restore": true
}
```
- 重启 docker 服务
```
systemctl restart docker
```

- 推送镜像到 dockerhub
```
docker login
Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
Username: yourname
Password:

Login Succeeded

docker tag kubeblog:1.0 yourname/kubeblog:1.0
[root@localhost Final]# docker push yourname/kubeblog:1.0
```
# 4.2 实践搭建私有镜像中心
## 安装 JFrog Container Registry(JCR)

```
mkdir -p $JFROG_HOME/artifactory/var/etc/
cd $JFROG_HOME/artifactory/var/etc/
touch ./system.yaml
chown -R 1030:1030 $JFROG_HOME/artifactory/var
chmod -R 777 $JFROG_HOME/artifactory/var

docker run --name artifactory-jcr -v $JFROG_HOME/artifactory/var/:/var/opt/jfrog/artifactory -d -p 8081:8081 -p 8082:8082 docker.bintray.io/jfrog/artifactory-jcr:latest
```
- 登录镜像中心JCR
```
localhost:8081
admin/passw0rd
```

# 4.3 将博客应用镜像上传到私有容器镜像中心进行管理
- 配置 JCR 本地域名
```
vi /etc/hosts
127.0.0.1 art.local
```
- 登录镜像中心
```
docker login art.local:8081 admin/passw0rd

docker build -t art.local:8081/docker-local/kubeblog:1.0 .

docker push art.local:8081/docker-local/kubeblog:1.0

```
- 登录 JCR 查看推送的镜像