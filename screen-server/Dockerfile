FROM openjdk:8-alpine
WORKDIR /screen-server
COPY target/*.jar   screen-server.jar
# tcp
EXPOSE 8888
EXPOSE 5005


CMD ["java" ,"-jar", "screen-server.jar"]