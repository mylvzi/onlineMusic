
````markdown
# 🎵 声动心弦 - 校园音乐分享平台的数字交响

> 用代码记录旋律，用音符连接心弦。一个由 Java 驱动的音乐分享与播放平台，专为校园场景设计，让数字世界中的旋律流动于指尖之间。

---

## 🌟 项目简介

“声动心弦” 是一个基于 **Java + Spring Boot** 构建的轻量级音乐播放与分享平台，支持用户上传、浏览与播放音乐资源，聚焦于校园场景下的音乐交流与个性表达。

本平台致力于打造一个属于大学生的「数字音乐空间」，既满足基础的播放功能，又具备良好的系统架构与扩展性，是一次技术与艺术的结合尝试。

---

## 🛠️ 技术栈

| 层级       | 技术选型                  |
|------------|---------------------------|
| 后端框架   | Spring Boot               |
| 数据访问层 | MyBatis + MySQL           |
| 前端展示   | HTML/CSS + Thymeleaf      |
| 文件管理   | Multipart 上传 + 本地存储 |
| 音频播放   | HTML5 `<audio>` 标签      |
| 其他       | Lombok、Swagger（可选）   |

---

## 🚀 核心功能

- 🎶 音乐上传与管理：支持用户上传本地音乐文件，并自动分类保存
- 📁 播放列表展示：展示所有音乐资源并可点击播放
- 🔍 搜索功能支持：根据歌曲名/歌手关键词搜索
- 👤 用户体验优化：基础用户系统 + 播放界面设计
- 📊 数据库结构清晰：支持后续功能拓展如点赞、评论、收藏等

---

## 📂 项目结构简览

```text
├── src
│   ├── main
│   │   ├── java/com/melodyshare
│   │   │   ├── controller   # 控制层
│   │   │   ├── service      # 业务逻辑
│   │   │   ├── mapper       # MyBatis 映射
│   │   │   └── entity       # 实体类
│   │   └── resources
│   │       ├── templates    # Thymeleaf 页面
│   │       ├── static       # 静态资源（CSS/JS）
│   │       └── application.yml
├── music-files              # 上传的音乐存储位置
└── README.md
````

---

## 🖼️ 界面预览

你可以通过以下地址访问项目登录界面并体验系统功能：

🔗 **[在线访问登录页面](http://60.205.7.136:8080/login.html)**

---

## 📌 启动方式

1. 克隆本项目：

   ```bash
   git clone https://github.com/your-username/melody-share.git
   cd melody-share
   ```

2. 配置数据库（`application.yml`）：

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/music_db
       username: root
       password: yourpassword
   ```

3. 启动 Spring Boot 应用：

   ```bash
   mvn spring-boot:run
   ```

4. 打开浏览器访问登录页：

   ```
   http://60.205.7.136:8080/login.html
   ```

---

## 📖 开发灵感

在音乐充盈生活的时代，我们希望打造一个干净、自由、开放的分享空间。用代码书写旋律，用网页记录青春，**“声动心弦”** 不只是一个项目，更是一段声音的旅程。

---

## 📬 联系我

如需交流或建议，欢迎通过 GitHub Issues 提出，或私信作者 😄。

---

> “音符有声，代码有魂。愿你我在数字中也能听见共鸣。”

```
