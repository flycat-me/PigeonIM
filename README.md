# 基于SpringBoot、WebSocket的网页聊天应用
前端使用vue + element-ui，后端使用SpringBoot相关技术
## 使用技术
- SpringBoot
- Mybatis-plus
- WebSocket
- MYSQL
- Redis
- 华为OBS对象存储服务
- Spring Security

## 启动项目
1. 修改配置文件
首先需要配置数据信息，在application.yml中选择使用的配置文件，在对应的文件中修改配置。
2. 配置OBS（对象存储） 在上传文件的时候使用的是华为的OBS对象存储服务
```yaml
pigeon:
  im:
    obs:
      #obs连接地址
      endPoint: obs地址
      ak: ak
      sk: sk
      bucketName: 桶名称
```
3. 然后就可以启动项目<br>
项目采用了token作为登录的令牌，除了登陆注册接口其他均需要携带token验证。