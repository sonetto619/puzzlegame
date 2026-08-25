# 登录注册界面 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 提供带背景、密码显示按钮和随机验证码的登录注册界面；通过合法输入和验证码后进入拼图游戏。

**Architecture:** `LoginJFrame` 负责登录表单、验证码和进入游戏。`RegisterJFrame` 提供同样的表单外观和校验后返回登录页，不持久化账号数据。两者均直接使用 Swing 组件和 `image\\background.png`。

**Tech Stack:** Java SE、Swing、ImageIcon。

---

### Task 1: 登录规则的回归测试

**Files:**
- Create: `puzzlegame/ui/LoginJFrameTest.java`
- Modify: `puzzlegame/ui/LoginJFrame.java`

- [ ] **Step 1: 写入失败测试**

```java
check(LoginJFrame.isLegalInput("player_01"), "letters, digits, and underscores are legal");
check(!LoginJFrame.isLegalInput("player!"), "punctuation is illegal");
check(LoginJFrame.isCaptchaCorrect("A1B2", "a1b2"), "captcha comparison ignores case");
```

- [ ] **Step 2: 运行测试，确认失败**

Run: `javac -encoding UTF-8 -d out\\production\\puzzlegame APP.java puzzlegame\\ui\\*.java`

Expected: 编译失败，因为 `LoginJFrame` 尚未定义登录校验方法。

- [ ] **Step 3: 实现最小登录规则**

```java
static boolean isLegalInput(String value) {
    return value != null && value.matches("[A-Za-z0-9_]+");
}
```

- [ ] **Step 4: 运行测试，确认通过**

Run: `java -ea -cp out\\production\\puzzlegame puzzlegame.ui.LoginJFrameTest`

Expected: 退出码为 0。

### Task 2: 登录页与注册页

**Files:**
- Modify: `puzzlegame/ui/LoginJFrame.java`
- Modify: `puzzlegame/ui/RegisterJFrame.java`
- Modify: `APP.java`

- [ ] **Step 1: 为登录页添加四行表单**

使用 `JTextField`、`JPasswordField`、验证码标签和登录/注册按钮。背景标签尺寸为 `508 x 560`，密码框右侧按钮在按住时清除回显字符、松开后恢复。

- [ ] **Step 2: 添加按钮事件**

登录事件验证用户名、密码、验证码；验证成功时创建 `GameJFrame` 并关闭登录页，失败时显示错误并刷新验证码。注册按钮打开 `RegisterJFrame`。

- [ ] **Step 3: 完成注册页与入口**

注册页采用相同输入布局；“注册”按钮使用相同校验，成功后返回登录页。`APP.main` 只启动 `LoginJFrame`。

- [ ] **Step 4: 编译并运行全部自检**

Run: `javac -encoding UTF-8 -d out\\production\\puzzlegame APP.java puzzlegame\\ui\\*.java`

Run: `java -ea -cp out\\production\\puzzlegame puzzlegame.ui.LoginJFrameTest`

Run: `java -ea -cp out\\production\\puzzlegame puzzlegame.ui.GameJFrameImageTest`

Expected: 三条命令均成功退出。
