## AD
> 闸种开发小组出品：闸种广告投放

### 新增
* 使用mapStruct 映射 to <-> dto

### 修改
* to: 对 mysql 端数据对象的映射
* dto: 对前台传送数据的映射
* 使用


### 旧

/api/file/avatar

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

