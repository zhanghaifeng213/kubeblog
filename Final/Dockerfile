FROM openjdk:8-jdk-alpine3.7
MAINTAINER QingFeng
VOLUME /tmp
ADD target/kubeblog.jar /kubeblog.jar
EXPOSE 5000
ENTRYPOINT ["java","-jar","/kubeblog.jar"]