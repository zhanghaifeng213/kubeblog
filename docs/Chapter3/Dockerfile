FROM openjdk:8
MAINTAINER QingFeng
VOLUME /tmp
ADD target/kubeblog.jar /kubeblog.jar
EXPOSE 9100
ENTRYPOINT ["java","-jar","/kubeblog.jar"]