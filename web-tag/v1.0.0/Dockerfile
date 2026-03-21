FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/web-0.0.1-SNAPSHOT.jar webApp.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java","-jar","/webApp.jar","--spring.profiles.active=pro", "&"]