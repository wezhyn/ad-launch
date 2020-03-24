FROM openjdk:8-jre-alpine

WORKDIR /ad-main
COPY target/*.jar   ad-main.jar
# web
EXPOSE 28080

# dubbo
EXPOSE 12345

CMD ["java" ,"-jar", "ad-main.jar"]