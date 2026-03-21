FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/cjMange.jar cjMange.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java","-jar","/cjMange.jar","--spring.profiles.active=pro", "&"]