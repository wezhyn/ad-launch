## AD
> 闸种开发小组出品：闸种广告投放

### 代办

### 新增
 * /day 查询当天账单
 * /day/2020-02-22T09:33:45.699Z 查询具体时间的账单
> /week /month 类同
 * 错误代码，整理：
[错误类型](common/src/main/java/com/wezhyn/project/controller/ResponseType.java)
例子： 访问 /api/test/auth 
```json
{
  "code": 50009,
  "data": null,
  "message": "不允许访问",
  "path": "/api/test/auth"
}
```
> 其他未捕获异常代办
* 手机登录 ：/api/user/login
```json
{
"发送1" : {
  "mobilePhone": "13456789999"
},
"接收1": {
  "code": 20000,
  "data": null,
  "message": "发送登录短信成功",
  "path": null
},
"发送2": {
  "mobilePhone": "13456789999",
  "code": "508143"
  },
"接收2": {
  "code": 20000,
  "data": {
    "token": "token"
  },
  "message": "登录成功",
  "path": null
}
}
```

### 旧

* 在Controller中使用 `@AuthenticationPrincipal AdAuthentication authentication`
代替 `@AuthenticationPrincipal Authentication authentication`,
`authentication.getName()` 返回当前用户名，`authentication.getId` 返回用户自增Id
* jpa 默认值
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

