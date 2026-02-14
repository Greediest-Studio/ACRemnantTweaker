# ACRemnantTweaker

一个 Minecraft 1.12.2 模组，允许你修改深渊国度（AbyssalCraft）中残存者（Remnant）的交易。

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.12.2-green.svg)](https://minecraft.net)

## 📋 功能特性

- **Java API**：通过编程方式修改残存者交易
- **CraftTweaker 集成**：使用 ZenScript 修改交易
- **基于 Mixin**：与深渊国度无缝集成
- **支持全部 7 种残存者职业**：
  - Farmer（农民）
  - Librarian（图书管理员）
  - Priest（牧师）
  - Blacksmith（铁匠）
  - Butcher（屠夫）
  - Banker（银行家）
  - Master Blacksmith（大师铁匠）

## 📦 前置要求

- Minecraft 1.12.2
- Forge 14.23.5.2847+
- AbyssalCraft（深渊国度）
- CraftTweaker（可选，用于 ZenScript 支持）

## 🚀 安装方法

1. 下载最新版本
2. 将 jar 文件放入 `mods` 文件夹
3. （可选）安装 CraftTweaker 以获得 ZenScript 支持

---

## 📖 使用说明

### 使用 CraftTweaker（推荐整合包作者使用）

在 `scripts/` 文件夹中创建一个 ZenScript 文件（例如 `scripts/remnant.zs`）：

```zenscript
import mods.acremnanttweaker.Remnant;

// 添加交易：16 个钻石 -> 1 个上古雕刻硬币
Remnant.addTrade("farmer", <minecraft:diamond> * 16, <abyssalcraft:coin:3>);

// 添加双输入交易：死灵之书 + 16 个硬币 -> 附魔台
Remnant.addTrade("librarian", 
    <abyssalcraft:necronomicon>, 
    <abyssalcraft:coin:3> * 16, 
    <minecraft:enchanting_table>
);

// 从农民那里移除所有剪刀交易
Remnant.removeTrade("farmer", null, <minecraft:shears>);

// 设置物品售卖数量（玩家卖给残存者）
// 玩家需要卖 1-2 个钻石才能获得 1 个硬币
Remnant.setItemSellingQuantity(<minecraft:diamond>, 1, 2);

// 设置物品购买价格（玩家从残存者购买）
// 玩家需要 5-8 个硬币才能买到 1 把钻石剑
Remnant.setCoinSellingPrice(<minecraft:diamond_sword>, 5, 8);
```

#### CraftTweaker 方法

| 方法 | 说明 |
|------|------|
| `addTrade(profession, input, output)` | 添加单输入交易 |
| `addTrade(profession, input1, input2, output)` | 添加双输入交易 |
| `removeTrade(profession, input, output)` | 移除匹配的交易 |
| `removeAllTrades(profession)` | 移除职业的所有交易 |
| `setItemSellingQuantity(item, min, max)` | 设置售卖数量范围 |
| `setCoinSellingPrice(item, min, max)` | 设置购买价格范围 |

#### 职业名称

支持中文和英文名称：

| ID | English | 中文 |
|----|---------|------|
| 0 | farmer | 农民 |
| 1 | librarian | 图书管理员 |
| 2 | priest | 牧师 |
| 3 | blacksmith | 铁匠 |
| 4 | butcher | 屠夫 |
| 5 | banker | 银行家 |
| 6 | master_blacksmith | 大师铁匠 |

### 使用 Java API（适用于模组开发者）

在 PreInit 事件中修改交易：

```java
import com.edwinyoung.acremnanttweaker.RemnantHelper;
import static com.edwinyoung.acremnanttweaker.RemnantHelper.RemnantProfession.*;

@Mod.EventHandler
public void preInit(FMLPreInitializationEvent event) {
    // 添加简单交易：16 个钻石 -> 1 个硬币
    RemnantHelper.addTrade(FARMER, Items.DIAMOND, 16, ACItems.elder_engraved_coin, 1);
    
    // 添加双输入交易：死灵之书 + 16 个硬币 -> 附魔书
    RemnantHelper.addTrade(LIBRARIAN, 
        ACItems.necronomicon, 1,
        ACItems.elder_engraved_coin, 16,
        Items.ENCHANTED_BOOK, 1
    );
    
    // 通过输出物品移除交易
    RemnantHelper.removeTrade(BLACKSMITH, null, Items.SHEARS);
    
    // 设置数量
    RemnantHelper.setItemSellingQuantity(Items.DIAMOND, 1, 2);
    RemnantHelper.setCoinSellingPrice(Items.DIAMOND_SWORD, 5, 8);
}
```

#### 使用 ItemStack 的高级用法

```java
// 创建带 NBT 的自定义 ItemStack
ItemStack customSword = new ItemStack(Items.DIAMOND_SWORD);
customSword.setStackDisplayName("Special Sword");

ItemStack reward = new ItemStack(ACItems.elder_engraved_coin, 32);

RemnantHelper.addTrade(BLACKSMITH, customSword, reward);
```

#### Java API 方法

**添加交易：**
```java
// 单输入
RemnantHelper.addTrade(RemnantProfession, Item input, int count, Item output, int count);
RemnantHelper.addTrade(RemnantProfession, ItemStack input, ItemStack output);

// 双输入
RemnantHelper.addTrade(RemnantProfession, Item in1, int c1, Item in2, int c2, Item out, int c3);
RemnantHelper.addTrade(RemnantProfession, ItemStack in1, ItemStack in2, ItemStack output);
```

**移除交易：**
```java
// 移除特定交易（null 表示"任意"）
RemnantHelper.removeTrade(RemnantProfession, Item input, Item output);

// 移除所有交易
RemnantHelper.removeAllTrades(RemnantProfession);
```

**修改数量：**
```java
// 设置售卖数量（玩家 -> 残存者）
RemnantHelper.setItemSellingQuantity(Item item, int min, int max);

// 设置购买价格（玩家 <- 残存者）
RemnantHelper.setCoinSellingPrice(Item item, int min, int max);
```

---

## 📝 示例

### 示例 1：高价钻石交易
```zenscript
import mods.acremnanttweaker.Remnant;

// 农民：8 个钻石 -> 1 个硬币（昂贵）
Remnant.addTrade("farmer", <minecraft:diamond> * 8, <abyssalcraft:coin:3>);
Remnant.setItemSellingQuantity(<minecraft:diamond>, 4, 6);
```

### 示例 2：图书管理员书籍交换
```zenscript
import mods.acremnanttweaker.Remnant;

// 图书管理员：书 + 10 个硬币 -> 附魔书
Remnant.addTrade("librarian",
    <minecraft:book>,
    <abyssalcraft:coin:3> * 10,
    <minecraft:enchanted_book>
);

// 移除书架交易
Remnant.removeTrade("librarian", null, <minecraft:bookshelf>);
```

### 示例 3：从屠夫那里买廉价食物
```zenscript
import mods.acremnanttweaker.Remnant;

// 屠夫在你"购买"面包时给你硬币
Remnant.setCoinSellingPrice(<minecraft:bread>, -4, -2);
Remnant.setCoinSellingPrice(<minecraft:cooked_beef>, -5, -3);
```

### 示例 4：移除所有煤炭交易
```zenscript
import mods.acremnanttweaker.Remnant;

// 从所有拥有煤炭交易的职业中移除煤炭交易
Remnant.removeTrade("farmer", <minecraft:coal>, null);
Remnant.removeTrade("blacksmith", <minecraft:coal>, null);
Remnant.removeTrade("butcher", <minecraft:coal>, null);
```

### 示例 5：完整的职业重做
```zenscript
import mods.acremnanttweaker.Remnant;

// 移除农民的所有交易并添加自定义交易
Remnant.removeAllTrades("farmer");
Remnant.addTrade("farmer", <minecraft:wheat> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <abyssalcraft:coin:3> * 5, <minecraft:golden_apple>);
Remnant.addTrade("farmer", <minecraft:emerald> * 16, <abyssalcraft:coin:3> * 2);
```

### 示例 6：Java API 完整配置
```java
import com.edwinyoung.acremnanttweaker.RemnantHelper;
import static com.edwinyoung.acremnanttweaker.RemnantHelper.RemnantProfession.*;

public static void initTrades() {
    // 农民交易
    RemnantHelper.addTrade(FARMER, Items.DIAMOND, 8, ACItems.elder_engraved_coin, 1);
    RemnantHelper.addTrade(FARMER, ACItems.elder_engraved_coin, 5, Items.GOLDEN_APPLE, 1);
    
    // 铁匠交易
    RemnantHelper.addTrade(BLACKSMITH, ACItems.elder_engraved_coin, 15, Items.DIAMOND_CHESTPLATE, 1);
    RemnantHelper.addTrade(BLACKSMITH, Items.IRON_INGOT, 32, ACItems.elder_engraved_coin, 1);
    
    // 图书管理员交易
    RemnantHelper.removeTrade(LIBRARIAN, null, Item.getItemFromBlock(Blocks.BOOKSHELF));
    RemnantHelper.addTrade(LIBRARIAN, 
        Items.BOOK, 1,
        ACItems.elder_engraved_coin, 10,
        Items.ENCHANTED_BOOK, 1
    );
    
    // 价格调整
    RemnantHelper.setItemSellingQuantity(Items.DIAMOND, 4, 6);
    RemnantHelper.setCoinSellingPrice(Items.DIAMOND_SWORD, 18, 22);
}
```

---

## 🔧 配置

### 整合包作者（CraftTweaker）

在 `scripts/` 文件夹中创建脚本。所有更改在游戏重启后生效。

**文件：`scripts/remnant_custom.zs`**
```zenscript
#priority 10

import mods.acremnanttweaker.Remnant;

// 在此处添加你的自定义交易...
```

### 模组开发者（Java）

在开发环境中编辑 `ModTrades.java`：

```java
public static void init(FMLPreInitializationEvent event) {
    // 在此处添加你的自定义交易...
}
```

---

## 🐛 调试

通过检查游戏日志启用详细日志记录。搜索 `ACRemnantTweaker` 可以看到：

- 添加交易：`添加交易到职业 农民 (0): Diamond x 16 + 无 -> Elder Engraved Coin`
- 移除交易：`移除交易: Wheat -> Shears`
- 错误：带堆栈跟踪的错误消息

**日志位置：** `logs/latest.log`

---

## ⚠️ 重要提示

- 🔄 **更改需要重启游戏** - `/ct reload` 不会更新残存者交易
- 💾 **测试前备份你的世界**
- 🧪 **先在创造模式中测试**
- 📊 **检查日志** 以获取错误和确认信息
- 🎯 **交易在残存者生成时应用** - 已存在的残存者不会更新

---

## 📚 文档

- **[示例脚本](example_scripts/)** - 可直接使用的 ZenScript 示例

---

## 🛠️ 从源代码构建

本项目使用 Java 25、Gradle 9.2.1 和 RetroFuturaGradle 2.0.2。

```bash
# 克隆仓库
git clone https://github.com/YourUsername/ACRemnantTweaker.git
cd ACRemnantTweaker

# 构建
./gradlew build

# 输出：build/libs/abyssalcraft_remnant_tweaker-1.0.0.jar
```

### 开发环境配置

1. 克隆此仓库
2. 确保已安装 Java 25
3. 配置 IDEA 使用 Java 25 作为 Gradle
4. 在 IntelliJ IDEA 中打开项目
5. 运行 `./gradlew build`

---

## 🤝 贡献

欢迎贡献！请：

1. Fork 此仓库
2. 创建功能分支
3. 提交你的更改
4. 推送到分支
5. 打开 Pull Request

---

## 📄 许可证

采用 [MIT 许可证](LICENSE)。可自由用于整合包和衍生作品。

---

## 🙏 致谢

- **使用 [RetroFuturaGradle](https://github.com/GTNewHorizons/RetroFuturaGradle) 构建**
- **AbyssalCraft（深渊国度）** 作者 Shinoow
- **CraftTweaker** 作者 Jared
- **MixinBooter** 作者 CleanroomMC

---

## 📞 支持

- 🐛 **Bug 报告**：[GitHub Issues](https://github.com/YourUsername/ACRemnantTweaker/issues)
- 💬 **问题**：先检查日志，然后创建 issue
- 📖 **文档**：查看此仓库中的指南

---

## 🌟 功能路线图

- [x] Java API
- [x] CraftTweaker 集成
- [x] 支持所有 7 种职业
- [x] 交易移除
- [x] 价格修改
- [ ] GUI 配置（未来）
- [ ] 游戏内交易预览（未来）

---

**用 ❤️ 为 Minecraft 模组社区制作**

