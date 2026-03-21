FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/b-0.0.1-SNAPSHOT.jar b.jar
ENTRYPOINT ["java","-jar","/b.jar","--spring.profiles.active=pro", "&"]