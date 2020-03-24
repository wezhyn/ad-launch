FROM openjdk:8-jre-alpine
WORKDIR /screen-server
COPY target/*.jar   screen-server.jar
# tcp
EXPOSE 8888


CMD ["java" ,"-jar", "screen-server.jar"]