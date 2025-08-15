# 🏃‍♂️ Fit8 - 8周健身塑形应用

<div align="center">
  <img src="app/src/main/res/drawable/ic_fit8_logo.xml" alt="Fit8 Logo" width="120" height="120">
  
  [![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
  [![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
  [![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
  [![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
</div>

## 📱 应用简介

Fit8是一款专业的8周健身塑形应用，基于科学的训练计划和营养指导，帮助用户实现健康的身体管理目标。应用采用现代化的Material Design 3设计语言，提供流畅的用户体验。

## ✨ 主要功能

### 🏋️‍♀️ 训练系统
- **8周渐进式训练计划** - 科学设计的训练强度递增
- **多样化训练类型** - 上肢、下肢、核心、HIIT、有氧运动
- **智能训练提醒** - 个性化的训练建议和技巧
- **动作详情指导** - 详细的动作说明和注意事项

### 🍎 营养管理
- **智能饮食计划** - 8周循环营养食谱
- **卡路里跟踪** - 精确的营养成分计算
- **饮水量监控** - 每日饮水目标和提醒
- **营养建议** - 个性化的饮食指导

### 📊 数据分析
- **专业数据统计** - 训练进度、体重变化、卡路里消耗
- **可视化图表** - 体重趋势、训练强度、健康评分雷达图
- **智能分析报告** - 基于历史数据的个性化建议
- **数据导出功能** - CSV格式和专业图片报告

### 🏆 激励系统
- **成就徽章** - 多种训练成就解锁
- **进度追踪** - 连续训练天数、总训练时长
- **照片记录** - 身材变化对比功能
- **社交分享** - 专业数据报告图片分享

## 🛠️ 技术架构

### 核心技术栈
- **开发语言**: Kotlin
- **架构模式**: MVVM + Repository Pattern
- **依赖注入**: Hilt (Dagger)
- **数据库**: Room Database
- **异步处理**: Kotlin Coroutines + Flow
- **UI框架**: ViewBinding + Material Design 3

### 第三方库
- **图表库**: MPAndroidChart - 数据可视化
- **图片加载**: Glide - 高效图片处理
- **网络请求**: Retrofit2 + OkHttp3 (预留)
- **JSON解析**: Gson (预留)

### 项目结构
```
app/src/main/java/com/vere/fit8/
├── data/                   # 数据层
│   ├── dao/               # 数据访问对象
│   ├── database/          # 数据库配置
│   ├── model/             # 数据模型
│   ├── repository/        # 数据仓库
│   └── initializer/       # 数据初始化
├── ui/                    # UI层
│   ├── activity/          # Activity
│   ├── fragment/          # Fragment
│   ├── adapter/           # RecyclerView适配器
│   └── viewmodel/         # ViewModel
├── utils/                 # 工具类
└── di/                    # 依赖注入模块
```

## 🚀 快速开始

### 环境要求
- Android Studio Arctic Fox 或更高版本
- Android SDK API 24+ (Android 7.0)
- Kotlin 1.8.0+
- Gradle 8.0+

### 构建步骤
1. **克隆项目**
   ```bash
   git clone https://github.com/dswcpp/FIT8.git
   cd FIT8
   ```

2. **打开项目**
   - 使用Android Studio打开项目
   - 等待Gradle同步完成

3. **运行应用**
   - 连接Android设备或启动模拟器
   - 点击运行按钮或使用快捷键 `Shift + F10`

### 构建Release版本
```bash
./gradlew assembleRelease
```

## 📸 应用截图

| 主页 | 训练计划 | 数据统计 | 个人中心 |
|------|----------|----------|----------|
| ![主页](screenshots/home.png) | ![训练](screenshots/training.png) | ![数据](screenshots/data.png) | ![个人](screenshots/profile.png) |

## 🎯 核心特性

### 数据驱动的健身体验
- **智能算法分析** - 基于用户数据提供个性化建议
- **趋势预测** - 体重变化趋势和目标达成预测
- **健康评分** - 多维度健康状况评估

### 专业级数据导出
- **CSV数据导出** - 完整的训练和健康数据
- **可视化报告** - 专业的图表和分析报告
- **社交分享** - 精美的成果展示图片

### 用户体验优化
- **Material Design 3** - 现代化的界面设计
- **流畅动画** - 精心设计的交互动效
- **响应式布局** - 适配不同屏幕尺寸

## 🤝 贡献指南

欢迎提交Issue和Pull Request来帮助改进Fit8！

### 开发规范
- 遵循Kotlin编码规范
- 使用MVVM架构模式
- 编写单元测试
- 提交前运行代码检查

## 📄 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

## 👨‍💻 作者

**dswcpp** - [GitHub](https://github.com/dswcpp)

## 🙏 致谢

感谢所有为Fit8项目做出贡献的开发者和用户！

---

<div align="center">
  <p>如果这个项目对你有帮助，请给它一个 ⭐️</p>
  <p>Made with ❤️ by dswcpp</p>
</div>
