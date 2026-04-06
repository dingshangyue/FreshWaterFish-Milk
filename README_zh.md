# FreshwaterFish

基于 Forge + Bukkit 的服务端核心，目标是在 1.20.1 上提供更好的兼容性与性能优化。

> 使用本服务端过程中出现的任何问题请在本项目 Issue 反馈。

## 特性

- **兼容性强** - 支持 Bukkit/Spigot 插件与 Forge 模组同时运行
- **高性能** - 异步保存世界，区块优化...
- **易于使用** - 简单的安装与使用
- **Velocity支持** - 支持Velocity Modern转发，实现跨服功能

## 主要维护版本

> **当前主要维护版本：Minecraft 1.20.1**
>
> - **Forge 版本**：47.4.16
> - **稳定性**：较好
> - **插件兼容性**：一般，仅Spigot
> - **模组兼容性**：优秀

## 下载

### 稳定版本

- [GitHub Releases](https://github.com/QianMo0721/FreshwaterFish/releases) - 推荐用于生产环境

### 开发版本

- [每日构建版本](https://github.com/QianMo0721/FreshwaterFish/actions/workflows/gradle.yml?query=branch%3ATrials) *(需要
  GitHub 登录)*

### 自行构建

- 克隆本项目到本地 `git clone -b <分支> https://github.com/QianMo0721/FreshwaterFish.git`
- 运行 `./gradlew cleanBuild remapSpigotJar idea --no-daemon -i --stacktrace --refresh-dependencies` 进行配置
- 运行 `./gradlew build collect` 构建项目
- 构建完成后，jar 文件位于 `./build/libs` 目录下

## 安装使用

1. **下载** jar 文件
2. **启动服务器**：

   ```bash
   java -jar freshwaterfish-1.20.1-1.0.14.jar nogui
   ```

   > `nogui` 参数将禁用服务器控制面板
   >
3. 在每次更新前，请将新的 JAR 替换旧的 JAR，并清理旧版本运行缓存目录，避免历史缓存影响修复生效。

## 不兼容

- 可能不兼容一些优化模组
- 与所有的优化Bukkit插件不兼容

## 支持与帮助

### 文档

- [文档（基础参考）](https://wiki.izzel.io/s/freshwaterfish-docs) - 可用于部分配置项参考
- [待办事项](TODO.md)

### 问题反馈

- [提交 Bug](https://github.com/QianMo0721/FreshwaterFish/issues/new/choose) - 遇到问题请在这里报告
- [讨论区](https://github.com/QianMo0721/FreshwaterFish/discussions) - 提问和讨论
- 请将本服务端问题反馈到本项目。

## 开源协议

本项目基于 [GPL v3](LICENSE) 协议开源。
