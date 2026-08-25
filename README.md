# 拼图游戏

这是一个使用 Java Swing 开发的 4×4 拼图游戏。

## 功能

- 随机打乱拼图
- 使用方向键移动图片
- 按住 E 键查看完整图片
- 按 P 键作弊直接完成拼图
- 支持更换游戏图片
- 完成拼图后显示胜利图片
- 支持登录、注册和随机验证码
- 支持密码明文查看
- 支持公众号图片展示
- 支持从 `image` 文件夹自动加载图片

## 项目结构

```text
image/                  游戏图片资源
puzzlegame/ui/          游戏主代码和测试代码
APP.java                程序入口
README.md               项目说明
```

## 运行环境

- JDK 8 或更高版本
- Windows、macOS 或 Linux
- Java Swing

## 编译和运行

请在项目根目录执行。

```bash
javac -encoding UTF-8 -d out APP.java puzzlegame/ui/*.java
java -cp out APP
```

Windows PowerShell 也可以使用：

```powershell
javac -encoding UTF-8 -d out APP.java puzzlegame\ui\*.java
java -cp out APP
```

## 资源说明

运行程序时必须保留 `image` 文件夹，其中包含：

- `background.png`
- `image0.jpg`
- `image1.jpg`
- `image2.jpg`
- `image3.jpg`
- `victory.jpg`
- `gongzhonghao.jpg`

## 测试

```bash
java -ea -cp out puzzlegame.ui.LoginJFrameTest
java -ea -cp out puzzlegame.ui.AssetLoaderTest
java -ea -cp out puzzlegame.ui.GameJFrameImageTest
```

## 说明

注册功能目前用于页面流程演示，不会将用户名和密码保存到数据库。
