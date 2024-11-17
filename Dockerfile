FROM openjdk:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} springboot_inotebook.jar

EXPOSE 8080

ENTRYPOINT ["java" ,"-jar", "/springboot_inotebook.jar"]