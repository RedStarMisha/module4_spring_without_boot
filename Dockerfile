FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar later.jar
ENTRYPOINT ["java","-jar","/later.jar"]