FROM maven:3-jdk-8-slim


COPY settings.xml /usr/share/maven/ref/
COPY pom.xml /tmp/
COPY ad-main/pom.xml /tmp/ad-main/

VOLUME /src

EXPOSE 8888

# mvn 参数:
#   -B: 以批处理 (batch) 模式运行 ;  -f: 使用指定的 POM 文件
#   -s: 替换用户级别 settings.xml 文件
RUN  mvn -B -q -f /tmp/pom.xml -s /usr/share/maven/ref/settings.xml dependency:resolve

CMD ["/bin/bash " ,"-c " ,"cd /src && mvn -pl ad-main -am spring-boot:run"]