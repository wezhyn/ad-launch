FROM openjdk:8-alpine

WORKDIR /ad-main
COPY target/*.jar   ad-main.jar
# web
EXPOSE 28080
EXPOSE 5005

# dubbo
EXPOSE 12345

CMD ["java" ,"-jar", "ad-main.jar"]