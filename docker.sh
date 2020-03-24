#!/bin/sh

if [ ! -x "$(which mvn)" ]; then
    echo "安装mvn"
    exit 1
fi
dockerTag=registry-vpc.cn-hangzhou.aliyuncs.com/wezhyn

docker_build() {
  moduleName=$1
  oPath="$(pwd)"
  indexFile=$oPath/index
  cd "$moduleName"||exit
  adVersion=$(printf 'VERSION=${project.version}\n0\n' | mvn org.apache.maven.plugins:maven-help-plugin:evaluate  | grep '^VERSION' |cut -d '='  -f 2)
  adDocker=$dockerTag/$moduleName:$adVersion
  adDockerLatest=$dockerTag/$moduleName:lastest
  echo "$adDocker" >> "$indexFile"
  docker build . -t "$adDocker"
  docker push "$adDocker"
  docker tag "$adDocker" "$adDockerLatest"
  docker push "$adDockerLatest"
  cd "$oPath"||exit 1
}

mvn package -pl ad-main,screen-server -am -DskipTests
# 生成 ad-main
docker_build "ad-main"
docker_build "screen-server"




