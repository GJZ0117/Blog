# Blog-SpringBoot
## 修改配置
- 在 `src/main/java/com/gjz/utils/QiniuUtils.java` 中，将 `url` 更改为自己在七牛云绑定的域名
- 在 `src/resources/applications.yml` 中
    - 修改 `spring.datasource.url` 中的 `ip` 为自己服务器ip或localhost，修改 `spring.datasource.password` 为mysql数据库root用户密码
    - 修改 `spring.redis.host` 为自己服务器ip或localhost，修改 `spring.redis.password` 为redis密码（若没设置密码可删除该配置）
    - 修改 `qiniu.accessKey` 和 `qiniu.accessSecretKey` 为自己在七牛云官网申请的公钥和私钥
## 启动项目
- 运行本项目中的 `BlogApplication.java` 启动后端SpringBoot工程
- 下载 `Blog-Vue` 仓库中的代码，在命令行中执行 `npm run build` 和 `npm run serve` 编译运行前端Vue工程
- 浏览器访问 `ip:80` 
