## AD
> 闸种开发小组出品：闸种广告投放

### 代办
* jpa 默认值无效

### 新增
* 在Controller中使用 `@AuthenticationPrincipal AdAuthentication authentication`
代替 `@AuthenticationPrincipal Authentication authentication`,
`authentication.getName()` 返回当前用户名，`authentication.getId` 返回用户自增Id



### 旧

* /api/file/avatar
* 使用[mapStruct](https://mapstruct.org/documentation/stable/reference/html/#defining-mapper) 映射 to <-> dto

### 启动
#### 环境准备
* docker
* docker-compose
* docker-compose.yml中 更换maven本地仓库
* application.yml ：修改spring.profiles.active 本地为dev,远程为pro

#### 启动命令
`docker-compose up -d `后台运行web服务器及mysql
默认 web 端口:8080
默认 mysql 端口 :6666 
> 默认端口是于 localhost本机服务而言
>

