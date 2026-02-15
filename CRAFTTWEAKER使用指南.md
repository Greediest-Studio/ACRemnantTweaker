# CraftTweaker 自定义残存者交易完整指南

本指南将详细介绍如何使用 CraftTweaker 自定义深渊国度（AbyssalCraft）中残存者（Remnant）的交易。

---

## 📚 目录

1. [快速入门](#快速入门)
2. [基础概念](#基础概念)
3. [交易类型详解](#交易类型详解)
4. [高级用法](#高级用法)
5. [实用示例](#实用示例)
6. [常见问题](#常见问题)
7. [调试技巧](#调试技巧)

---

## 快速入门

### 第一步：创建脚本文件

在你的 Minecraft 游戏目录中，找到 `scripts/` 文件夹（如果没有则创建一个），然后创建一个新的 `.zs` 文件：

```
minecraft/
├── mods/
├── config/
└── scripts/
    └── remnant_trades.zs  ← 在这里创建你的脚本
```

### 第二步：导入 Remnant 类

在脚本文件的开头，添加导入语句：

```zenscript
import mods.acremnanttweaker.Remnant;
```

### 第三步：添加你的第一个交易

```zenscript
// 让农民用 10 个小麦换取 1 个上古雕刻硬币
Remnant.addTrade("farmer", <minecraft:wheat> * 10, <abyssalcraft:coin:3>);
```

### 第四步：重启游戏

⚠️ **重要**：CraftTweaker 的修改需要**完全重启游戏**才能生效，使用 `/ct reload` 命令**不会**更新残存者的交易！

---

## 基础概念

### 残存者是什么？

残存者（Remnant）是深渊国度模组中的一种 NPC，类似于原版的村民，但使用特殊的货币系统进行交易。

### 残存者的职业

残存者有 7 种职业，每种职业有不同的默认交易：

| 职业 ID | 英文名 | 中文名 | 特点 |
|---------|--------|--------|------|
| 0 | farmer | 农民 | 交易农作物和食物 |
| 1 | librarian | 图书管理员 | 交易书籍和附魔物品 |
| 2 | priest | 牧师 | 交易药水和宗教物品 |
| 3 | blacksmith | 铁匠 | 交易武器和工具 |
| 4 | butcher | 屠夫 | 交易肉类和皮革 |
| 5 | banker | 银行家 | 交易贵重物品 |
| 6 | master_blacksmith | 大师铁匠 | 交易高级装备 |

### 货币系统

残存者使用深渊国度的硬币作为货币：
- `<abyssalcraft:coin:0>` - 雕刻硬币（Engraved Coin）
- `<abyssalcraft:coin:1>` - 融化的雕刻硬币
- `<abyssalcraft:coin:2>` - 雕刻的史前硬币
- `<abyssalcraft:coin:3>` - 上古雕刻硬币（Elder Engraved Coin）- 最常用

---

## 交易类型详解

### 1. 单输入交易

最简单的交易形式：**一种物品** → **一种物品**

```zenscript
Remnant.addTrade(职业, 输入物品, 输出物品);
```

**示例：**

```zenscript
// 农民：32 个小麦 -> 1 个上古雕刻硬币
Remnant.addTrade("farmer", <minecraft:wheat> * 32, <abyssalcraft:coin:3>);

// 铁匠：16 个铁锭 -> 1 个上古雕刻硬币
Remnant.addTrade("blacksmith", <minecraft:iron_ingot> * 16, <abyssalcraft:coin:3>);

// 银行家：1 个上古雕刻硬币 -> 5 个钻石
Remnant.addTrade("banker", <abyssalcraft:coin:3>, <minecraft:diamond> * 5);
```

### 2. 双输入交易

更复杂的交易：**两种物品** → **一种物品**

```zenscript
Remnant.addTrade(职业, 输入物品1, 输入物品2, 输出物品);
```

**示例：**

```zenscript
// 图书管理员：书 + 16 个上古雕刻硬币 -> 附魔书
Remnant.addTrade("librarian", 
    <minecraft:book>, 
    <abyssalcraft:coin:3> * 16, 
    <minecraft:enchanted_book>
);

// 铁匠：钻石剑 + 2 个上古雕刻硬币 -> 附魔钻石剑
Remnant.addTrade("blacksmith",
    <minecraft:diamond_sword>,
    <abyssalcraft:coin:3> * 2,
    <minecraft:diamond_sword>.withTag({ench: [{lvl: 5 as short, id: 16 as short}]})
);

// 牧师：死灵之书 + 32 个上古雕刻硬币 -> 下界之星
Remnant.addTrade("priest",
    <abyssalcraft:necronomicon>,
    <abyssalcraft:coin:3> * 32,
    <minecraft:nether_star>
);
```

### 3. 自定义概率交易

从 1.0.0 版本开始，你可以为交易设置**出现概率**，让自定义交易与原版交易一样参与随机抽取：

#### 单输入 + 概率

```zenscript
Remnant.addTrade(职业, 输入物品, 输出物品, 概率);
```

#### 双输入 + 概率

```zenscript
Remnant.addTrade(职业, 输入物品1, 输入物品2, 输出物品, 概率);
```

**概率值说明：**
- 取值范围：`0.0` 至 `1.0`
- `0.0` = 永不出现（相当于禁用）
- `1.0` = 必定出现（100% 概率，与不指定概率时相同）
- `0.5` = 50% 概率
- `0.1` = 10% 概率（稀有）

**交易机制：**
1. 游戏会根据概率随机决定每个交易是否加入候选池
2. 候选池中的所有交易（包括原版交易）会被打乱顺序
3. 残存者从打乱后的池中选择第 1 个交易显示给玩家
4. 因此每次召唤的残存者可能显示不同的交易

**示例：**

```zenscript
// 高概率（90%）- 常见交易
Remnant.addTrade("farmer", <minecraft:wheat> * 32, <abyssalcraft:coin:3>, 0.9);

// 中等概率（50%）- 随机交易
Remnant.addTrade("blacksmith", <minecraft:iron_ingot> * 16, <abyssalcraft:coin:3>, 0.5);

// 低概率（10%）- 稀有交易
Remnant.addTrade("banker", 
    <abyssalcraft:coin:3>, 
    <minecraft:diamond> * 5, 
    0.1
);

// 极低概率（1%）- 超稀有交易，双输入
Remnant.addTrade("priest",
    <abyssalcraft:necronomicon>,
    <abyssalcraft:coin:3> * 64,
    <minecraft:nether_star>,
    0.01
);

// 必定出现（100%）- 等同于不指定概率
Remnant.addTrade("butcher", 
    <minecraft:wheat> * 10, 
    <minecraft:leather>, 
    1.0
);
```

**实用技巧：**
- 如果希望交易**总是出现**，使用 `1.0` 或直接省略概率参数
- 如果希望交易**很少出现**（稀有奖励），使用 `0.05` ~ `0.2`
- 如果希望交易**偶尔出现**（平衡游戏难度），使用 `0.3` ~ `0.7`
- 如果希望完全禁用某个交易而不删除代码，使用 `0.0`

### 4. 移除交易

从职业中移除特定的交易：

```zenscript
Remnant.removeTrade(职业, 输入物品, 输出物品);
```

**参数说明：**
- 使用 `null` 表示"任意物品"
### 4. 移除交易

从职业中移除特定的交易：

```zenscript
Remnant.removeTrade(职业, 输入物品, 输出物品);
```

**参数说明：**
- 使用 `null` 表示"任意物品"
- 可以只指定输入或输出来移除所有匹配的交易

**示例：**

```zenscript
// 移除农民所有产出剪刀的交易（不管输入是什么）
Remnant.removeTrade("farmer", null, <minecraft:shears>);

// 移除铁匠所有需要煤炭的交易（不管输出是什么）
Remnant.removeTrade("blacksmith", <minecraft:coal>, null);

// 移除屠夫用小麦换取皮革的特定交易
Remnant.removeTrade("butcher", <minecraft:wheat>, <minecraft:leather>);
```

### 5. 移除所有交易

清空某个职业的所有交易：

```zenscript
Remnant.removeAllTrades(职业);
```

**示例：**

```zenscript
// 移除农民的所有交易
Remnant.removeAllTrades("farmer");

// 然后添加你自己的交易
Remnant.addTrade("farmer", <minecraft:wheat> * 64, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:potato> * 64, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:carrot> * 64, <abyssalcraft:coin:3>);
```

### 6. 设置售卖数量

调整玩家**卖给**残存者时需要的物品数量：

```zenscript
Remnant.setItemSellingQuantity(物品, 最小数量, 最大数量);
```

**原理：**
- 残存者会随机在最小值和最大值之间选择一个数量
- 玩家需要提供该数量的物品才能完成交易

**示例：**

```zenscript
// 卖钻石给残存者时，需要 1-3 个钻石
Remnant.setItemSellingQuantity(<minecraft:diamond>, 1, 3);

// 卖金锭时，需要 8-12 个金锭
Remnant.setItemSellingQuantity(<minecraft:gold_ingot>, 8, 12);

// 卖煤炭时，需要 16-24 个煤炭
Remnant.setItemSellingQuantity(<minecraft:coal>, 16, 24);
```

### 7. 设置购买价格

调整玩家**从残存者购买**物品时需要的硬币数量：

```zenscript
Remnant.setCoinSellingPrice(物品, 最小硬币数, 最大硬币数);
```

**特殊技巧：**
- 使用**负数**可以让残存者在"出售"物品时反而给你硬币！

**示例：**

```zenscript
// 从残存者购买钻石剑需要 10-15 个硬币
Remnant.setCoinSellingPrice(<minecraft:diamond_sword>, 10, 15);

// 从残存者购买附魔台需要 20-25 个硬币
Remnant.setCoinSellingPrice(<minecraft:enchanting_table>, 20, 25);

// 特殊：购买面包时，残存者反而给你 2-4 个硬币（负数价格）
Remnant.setCoinSellingPrice(<minecraft:bread>, -4, -2);
```

---

## 高级用法

### 使用 NBT 标签

你可以创建带有特殊属性的物品作为交易：

```zenscript
// 锋利 V 的钻石剑
val sharpnessSword = <minecraft:diamond_sword>.withTag({
    ench: [{lvl: 5 as short, id: 16 as short}]
});

Remnant.addTrade("blacksmith", 
    <abyssalcraft:coin:3> * 20, 
    sharpnessSword
);

// 带自定义名称的物品
val renamedItem = <minecraft:diamond>.withTag({
    display: {Name: "§b特殊钻石"}
});

Remnant.addTrade("banker", 
    <minecraft:emerald> * 16, 
    renamedItem
);
```

### 使用矿物词典

使用矿物词典（Ore Dictionary）可以接受多种同类物品：

```zenscript
// 接受任何木板
Remnant.addTrade("farmer", <ore:plankWood> * 64, <abyssalcraft:coin:3>);

// 接受任何宝石
Remnant.addTrade("banker", <ore:gemDiamond> * 8, <abyssalcraft:coin:3>);
```

### 设置脚本优先级

如果你想确保脚本在其他脚本之前执行，可以设置优先级：

```zenscript
#priority 100

import mods.acremnanttweaker.Remnant;

// 你的交易代码
```

优先级数字越大，执行越早。

### 组合使用多个方法

创建完整的职业自定义：

```zenscript
// 完全重做农民职业
Remnant.removeAllTrades("farmer");

// 添加新的交易
Remnant.addTrade("farmer", <minecraft:wheat> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:carrot> * 24, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:potato> * 24, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <abyssalcraft:coin:3> * 3, <minecraft:golden_carrot> * 8);

// 调整价格
Remnant.setItemSellingQuantity(<minecraft:wheat>, 16, 32);
Remnant.setCoinSellingPrice(<minecraft:golden_carrot>, 2, 4);
```

---

## 实用示例

### 示例 1：资源换硬币（早期游戏）

适合游戏早期，让玩家通过收集资源获得硬币：

```zenscript
import mods.acremnanttweaker.Remnant;

// 农民 - 农作物换硬币
Remnant.addTrade("farmer", <minecraft:wheat> * 48, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:carrot> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:potato> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:beetroot> * 32, <abyssalcraft:coin:3>);

// 矿工/铁匠 - 矿物换硬币
Remnant.addTrade("blacksmith", <minecraft:coal> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("blacksmith", <minecraft:iron_ingot> * 16, <abyssalcraft:coin:3>);
Remnant.addTrade("blacksmith", <minecraft:gold_ingot> * 8, <abyssalcraft:coin:3>);

// 屠夫 - 肉类和皮革换硬币
Remnant.addTrade("butcher", <minecraft:leather> * 16, <abyssalcraft:coin:3>);
Remnant.addTrade("butcher", <minecraft:beef> * 24, <abyssalcraft:coin:3>);
```

### 示例 2：硬币换稀有物品（中后期）

让玩家用硬币购买难以获得的物品：

```zenscript
import mods.acremnanttweaker.Remnant;

// 银行家 - 贵重物品
Remnant.addTrade("banker", <abyssalcraft:coin:3> * 16, <minecraft:diamond> * 4);
Remnant.addTrade("banker", <abyssalcraft:coin:3> * 32, <minecraft:emerald> * 8);
Remnant.addTrade("banker", <abyssalcraft:coin:3> * 64, <minecraft:nether_star>);

// 图书管理员 - 书籍和知识
Remnant.addTrade("librarian", <abyssalcraft:coin:3> * 8, <minecraft:enchanted_book>);
Remnant.addTrade("librarian", <abyssalcraft:coin:3> * 16, <minecraft:name_tag> * 3);

// 牧师 - 魔法物品
Remnant.addTrade("priest", <abyssalcraft:coin:3> * 24, <minecraft:end_crystal>);
Remnant.addTrade("priest", <abyssalcraft:coin:3> * 12, <minecraft:ender_pearl> * 16);
```

### 示例 3：双向交易系统

创建一个平衡的经济系统：

```zenscript
import mods.acremnanttweaker.Remnant;

// 玩家可以卖钻石获得硬币
Remnant.addTrade("banker", <minecraft:diamond> * 4, <abyssalcraft:coin:3>);
Remnant.setItemSellingQuantity(<minecraft:diamond>, 3, 5);

// 玩家也可以用硬币买回钻石，但价格更高
Remnant.addTrade("banker", <abyssalcraft:coin:3> * 2, <minecraft:diamond>);
Remnant.setCoinSellingPrice(<minecraft:diamond>, 1, 2);
```

### 示例 4：整合包专用 - 移除原版交易

移除你不想要的默认交易：

```zenscript
import mods.acremnanttweaker.Remnant;

// 移除所有职业的煤炭交易（让煤炭更稀有）
Remnant.removeTrade("farmer", <minecraft:coal>, null);
Remnant.removeTrade("blacksmith", <minecraft:coal>, null);
Remnant.removeTrade("butcher", <minecraft:coal>, null);

// 移除农民的剪刀交易
Remnant.removeTrade("farmer", null, <minecraft:shears>);

// 移除所有钻石相关的交易（让钻石更难获得）
Remnant.removeTrade("banker", null, <minecraft:diamond>);
Remnant.removeTrade("banker", <minecraft:diamond>, null);
```

### 示例 5：任务式交易

创建需要多个步骤的任务链：

```zenscript
import mods.acremnanttweaker.Remnant;

// 第一步：收集基础资源
Remnant.addTrade("farmer", <minecraft:wheat> * 64, <abyssalcraft:coin:3>);

// 第二步：用硬币买特殊物品
Remnant.addTrade("librarian", 
    <abyssalcraft:coin:3> * 8, 
    <abyssalcraft:necronomicon>
);

// 第三步：用特殊物品+更多硬币换取终极奖励
Remnant.addTrade("priest",
    <abyssalcraft:necronomicon>,
    <abyssalcraft:coin:3> * 32,
    <minecraft:nether_star>
);
```

### 示例 6：主题化职业

给每个职业一个明确的主题：

```zenscript
import mods.acremnanttweaker.Remnant;

// === 农民 - 食物专家 ===
Remnant.removeAllTrades("farmer");
// 买入：农作物
Remnant.addTrade("farmer", <minecraft:wheat> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:carrot> * 24, <abyssalcraft:coin:3>);
// 卖出：熟食
Remnant.addTrade("farmer", <abyssalcraft:coin:3>, <minecraft:bread> * 8);
Remnant.addTrade("farmer", <abyssalcraft:coin:3>, <minecraft:baked_potato> * 6);

// === 铁匠 - 工具武器专家 ===
Remnant.removeAllTrades("blacksmith");
// 买入：金属
Remnant.addTrade("blacksmith", <minecraft:iron_ingot> * 16, <abyssalcraft:coin:3>);
Remnant.addTrade("blacksmith", <minecraft:gold_ingot> * 12, <abyssalcraft:coin:3>);
// 卖出：工具
Remnant.addTrade("blacksmith", <abyssalcraft:coin:3> * 3, <minecraft:iron_pickaxe>);
Remnant.addTrade("blacksmith", <abyssalcraft:coin:3> * 5, <minecraft:diamond_sword>);

// === 图书管理员 - 知识专家 ===
Remnant.removeAllTrades("librarian");
// 买入：纸张和书
Remnant.addTrade("librarian", <minecraft:paper> * 48, <abyssalcraft:coin:3>);
Remnant.addTrade("librarian", <minecraft:book> * 12, <abyssalcraft:coin:3>);
// 卖出：附魔物品
Remnant.addTrade("librarian", <abyssalcraft:coin:3> * 10, <minecraft:enchanted_book>);
Remnant.addTrade("librarian", <abyssalcraft:coin:3> * 15, <minecraft:enchanting_table>);
```

### 示例 7：稀有度系统 - 使用概率创建抽奖式交易

利用概率参数创建不同稀有度的交易，让玩家探索时充满惊喜：

```zenscript
import mods.acremnanttweaker.Remnant;

// === 银行家 - 宝石抽奖系统 ===
// 常见奖励（80% 概率）
Remnant.addTrade("banker", 
    <abyssalcraft:coin:3> * 5, 
    <minecraft:emerald> * 2, 
    0.8
);

// 稀有奖励（30% 概率）
Remnant.addTrade("banker", 
    <abyssalcraft:coin:3> * 10, 
    <minecraft:diamond> * 4, 
    0.3
);

// 超稀有奖励（5% 概率）
Remnant.addTrade("banker", 
    <abyssalcraft:coin:3> * 20, 
    <minecraft:nether_star>, 
    0.05
);

// === 铁匠 - 装备抽奖 ===
// 普通装备（必定出现）
Remnant.addTrade("blacksmith", 
    <abyssalcraft:coin:3> * 3, 
    <minecraft:iron_sword>, 
    1.0
);

// 附魔装备（50% 概率）
val sharpnessSword = <minecraft:diamond_sword>.withTag({
    ench: [{lvl: 3 as short, id: 16 as short}]
});
Remnant.addTrade("blacksmith", 
    <abyssalcraft:coin:3> * 15, 
    sharpnessSword, 
    0.5
);

// 神器级装备（1% 概率 - 超级稀有！）
val godSword = <minecraft:diamond_sword>.withTag({
    ench: [
        {lvl: 5 as short, id: 16 as short},  // 锋利 V
        {lvl: 2 as short, id: 20 as short},  // 火焰附加 II
        {lvl: 3 as short, id: 21 as short}   // 抢夺 III
    ]
});
Remnant.addTrade("blacksmith", 
    <abyssalcraft:coin:3> * 50, 
    godSword, 
    0.01
);

// === 图书管理员 - 知识宝库 ===
// 普通附魔书（70% 概率）
Remnant.addTrade("librarian", 
    <abyssalcraft:coin:3> * 8, 
    <minecraft:enchanted_book>, 
    0.7
);

// 稀有书籍（20% 概率）
Remnant.addTrade("librarian", 
    <abyssalcraft:coin:3> * 15, 
    <minecraft:enchanting_table>, 
    0.2
);

// === 提示 ===
// 这样设置后，每次召唤残存者都可能遇到不同的交易！
// 有些残存者可能只有普通交易，有些可能运气好遇到稀有交易
// 玩家需要探索更多的残存者才能找到心仪的交易
```

**这个系统的优势：**
- 增加探索乐趣：玩家需要找多个残存者才能触发稀有交易
- 平衡游戏难度：强力物品不会轻易获得
- 模拟抽卡体验：每次遇见残存者都像开盲盒
- 与原版融合：自定义交易和原版交易一起随机，感觉更自然

---

## 常见问题

### Q1: 为什么我的修改没有生效？

**A:** 残存者的交易在游戏启动时加载，你必须：
1. 保存脚本文件
2. **完全退出游戏**
3. 重新启动游戏

`/ct reload` 命令**不能**重新加载残存者交易！

### Q2: 可以使用任何职业名称吗？

**A:** 你可以使用以下任意一种格式：
- 英文：`"farmer"`, `"librarian"`, `"priest"`, `"blacksmith"`, `"butcher"`, `"banker"`, `"master_blacksmith"`
- 中文：`"农民"`, `"图书管理员"`, `"牧师"`, `"铁匠"`, `"屠夫"`, `"银行家"`, `"大师铁匠"`

大小写不敏感，但建议使用小写英文以避免问题。

### Q3: 如何知道物品的 ID？

**A:** 在游戏中：
1. 按 `F3 + H` 启用高级工具提示
2. 将鼠标悬停在物品上
3. 看到类似 `minecraft:diamond` 的 ID
4. 在脚本中使用 `<minecraft:diamond>`

### Q4: 为什么会出现"null"错误？

**A:** 常见原因：
1. 物品 ID 拼写错误
2. 模组物品没有正确加载
3. 使用了不存在的元数据（metadata）

**解决方法**：
- 检查拼写
- 确保相关模组已安装
- 查看 `logs/latest.log` 获取详细错误信息

### Q5: 可以一次添加多个交易吗？

**A:** 可以！你可以在一个脚本文件中添加任意多个交易：

```zenscript
import mods.acremnanttweaker.Remnant;

Remnant.addTrade("farmer", <minecraft:wheat> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:carrot> * 24, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:potato> * 24, <abyssalcraft:coin:3>);
// ... 尽可能多的交易
```

### Q6: 已经生成的残存者会更新交易吗？

**A:** 不会！只有**新生成**的残存者才会有新的交易。
- 如果在创造模式测试，杀死旧的残存者并生成新的
- 或者探索新的区块来找到新的残存者

### Q7: 可以修改硬币本身的行为吗？

**A:** 本模组**不能**修改硬币的行为，只能修改残存者的交易。

硬币的价值由交易决定：
- 如果很多交易都给硬币，硬币就便宜
- 如果很少交易给硬币但很多需要硬币，硬币就贵

### Q8: 如何平衡交易？

**A:** 平衡建议：
1. **早期资源** → 硬币：给少量硬币（1个）
2. **稀有资源** → 硬币：给较多硬币（2-5个）
3. **硬币** → **重要物品**：需要较多硬币（10-30个）
4. **考虑游戏进度**：早期玩家应该能通过简单资源获得少量硬币

### Q9: 概率是如何工作的？

**A:** 交易概率系统的工作流程：
1. **概率检查**：每个交易按照其概率（0.0-1.0）随机决定是否加入候选池
   - 例如概率 0.5 的交易有 50% 几率加入池中
2. **混合原版**：自定义交易和原版交易会放在同一个候选池中
3. **随机打乱**：所有候选交易会被随机打乱顺序
4. **选择交易**：残存者从打乱后的池中选择**第 1 个**交易显示

**这意味着什么？**
- 每个残存者通常只显示 **1 个**交易
- 高概率交易更容易出现，但不保证
- 低概率交易很少出现，需要多找几个残存者
- 自定义交易和原版交易混在一起，体验更自然

**示例场景：**
假设铁匠有以下交易：
- 原版交易 A（概率 0.5）
- 原版交易 B（概率 0.3）
- 自定义交易 C（概率 1.0）
- 自定义交易 D（概率 0.2）

当生成铁匠残存者时：
- 交易 A 有 50% 几率进入候选池
- 交易 B 有 30% 几率进入候选池
- 交易 C 必定进入候选池（100%）
- 交易 D 有 20% 几率进入候选池
- 进入池中的交易被打乱，残存者显示第 1 个

**结果：** 每次生成的铁匠可能显示不同交易！

### Q10: 为什么我设置了概率 1.0 但交易有时还是不出现？

**A:** 这是正常现象！原因有两个：

1. **原版交易竞争**：
   - 即使你的交易概率是 1.0（100%），它也会和原版交易一起打乱
   - 残存者只会选择第 1 个交易
   - 所以你的交易可能被"排在后面"而没有显示

2. **解决方法**：
   ```zenscript
   // 方法 1：移除原版交易，只保留自定义的
   Remnant.removeAllTrades("farmer");
   Remnant.addTrade("farmer", <minecraft:wheat> * 32, <abyssalcraft:coin:3>, 1.0);
   
   // 方法 2：添加多个自定义交易，增加出现几率
   Remnant.addTrade("farmer", <minecraft:wheat> * 32, <abyssalcraft:coin:3>, 1.0);
   Remnant.addTrade("farmer", <minecraft:carrot> * 24, <abyssalcraft:coin:3>, 1.0);
   Remnant.addTrade("farmer", <minecraft:potato> * 24, <abyssalcraft:coin:3>, 1.0);
   // 现在至少会显示一个自定义交易的可能性更高
   ```

3. **多找几个残存者**：
   - 这是设计特性，不是 bug
   - 鼓励玩家探索，找到合适的残存者
   - 让每个残存者的交易更独特

---

## 调试技巧

### 1. 检查日志文件

游戏日志会显示所有交易的添加和移除信息。

**日志位置：** `logs/latest.log`

**搜索关键词：**
```
ACRemnantTweaker
```

**成功添加交易的日志：**
```
[ACRemnantTweaker] 添加交易到职业 农民 (0): Wheat x 32 + 无 -> Elder Engraved Coin
```

**移除交易的日志：**
```
[ACRemnantTweaker] 移除交易: Wheat -> Shears
```

### 2. 使用创造模式测试

在创造模式中：
1. 使用刷怪蛋生成残存者
2. 右键点击查看交易
3. 如果不满意，杀死它并生成新的
4. 重复测试直到满意

### 3. 逐步添加交易

不要一次性添加所有交易，而是：
1. 先添加 1-2 个交易测试
2. 重启游戏验证
3. 确认工作后再添加更多
4. 这样容易定位错误

### 4. 使用注释组织代码

```zenscript
import mods.acremnanttweaker.Remnant;

// ===== 农民交易 =====
// 买入：基础农作物
Remnant.addTrade("farmer", <minecraft:wheat> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:carrot> * 24, <abyssalcraft:coin:3>);

// 卖出：高级食物
Remnant.addTrade("farmer", <abyssalcraft:coin:3>, <minecraft:golden_apple>);

// ===== 铁匠交易 =====
// ... 更多交易
```

### 5. 备份你的脚本

在大量修改之前，复制一份脚本文件作为备份：
```
scripts/
├── remnant_trades.zs        ← 当前版本
└── remnant_trades.zs.backup ← 备份
```

---

## 完整示例脚本

这是一个完整的、平衡的示例脚本，你可以直接使用或作为参考：

```zenscript
#priority 10

import mods.acremnanttweaker.Remnant;

/*
 * ACRemnantTweaker - 残存者交易自定义
 * 
 * 设计理念：
 * - 早期：通过收集资源获得硬币
 * - 中期：用硬币购买实用物品
 * - 后期：用稀有资源换取贵重物品
 */

// ========================================
// 农民 - 基础资源收购
// ========================================
Remnant.removeAllTrades("farmer");

// 农作物换硬币（早期收入来源）
Remnant.addTrade("farmer", <minecraft:wheat> * 48, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:carrot> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:potato> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("farmer", <minecraft:beetroot> * 32, <abyssalcraft:coin:3>);

// 硬币换食物（便宜的补给）
Remnant.addTrade("farmer", <abyssalcraft:coin:3>, <minecraft:bread> * 16);
Remnant.addTrade("farmer", <abyssalcraft:coin:3>, <minecraft:baked_potato> * 12);

// 高级交易：硬币换金苹果
Remnant.addTrade("farmer", <abyssalcraft:coin:3> * 8, <minecraft:golden_apple>);

// ========================================
// 铁匠 - 工具和武器
// ========================================
Remnant.removeAllTrades("blacksmith");

// 金属换硬币
Remnant.addTrade("blacksmith", <minecraft:iron_ingot> * 20, <abyssalcraft:coin:3>);
Remnant.addTrade("blacksmith", <minecraft:gold_ingot> * 16, <abyssalcraft:coin:3>);

// 硬币换工具（价格适中）
Remnant.addTrade("blacksmith", <abyssalcraft:coin:3> * 3, <minecraft:iron_pickaxe>);
Remnant.addTrade("blacksmith", <abyssalcraft:coin:3> * 3, <minecraft:iron_axe>);
Remnant.addTrade("blacksmith", <abyssalcraft:coin:3> * 4, <minecraft:iron_sword>);

// 硬币换盔甲（贵一些）
Remnant.addTrade("blacksmith", <abyssalcraft:coin:3> * 6, <minecraft:iron_chestplate>);
Remnant.addTrade("blacksmith", <abyssalcraft:coin:3> * 5, <minecraft:iron_leggings>);

// ========================================
// 图书管理员 - 知识和附魔
// ========================================
Remnant.removeAllTrades("librarian");

// 纸和书换硬币
Remnant.addTrade("librarian", <minecraft:paper> * 64, <abyssalcraft:coin:3>);
Remnant.addTrade("librarian", <minecraft:book> * 16, <abyssalcraft:coin:3>);

// 硬币换附魔相关
Remnant.addTrade("librarian", <abyssalcraft:coin:3> * 12, <minecraft:enchanted_book>);
Remnant.addTrade("librarian", <abyssalcraft:coin:3> * 20, <minecraft:enchanting_table>);
Remnant.addTrade("librarian", <abyssalcraft:coin:3> * 8, <minecraft:bookshelf> * 4);

// 高级交易：死灵之书
Remnant.addTrade("librarian", 
    <minecraft:book>,
    <abyssalcraft:coin:3> * 24,
    <abyssalcraft:necronomicon>
);

// ========================================
// 屠夫 - 肉类和皮革
// ========================================
Remnant.removeAllTrades("butcher");

// 生肉换硬币
Remnant.addTrade("butcher", <minecraft:beef> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("butcher", <minecraft:porkchop> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("butcher", <minecraft:chicken> * 32, <abyssalcraft:coin:3>);
Remnant.addTrade("butcher", <minecraft:leather> * 24, <abyssalcraft:coin:3>);

// 硬币换熟食（比自己烤贵）
Remnant.addTrade("butcher", <abyssalcraft:coin:3>, <minecraft:cooked_beef> * 8);
Remnant.addTrade("butcher", <abyssalcraft:coin:3>, <minecraft:cooked_porkchop> * 8);

// ========================================
// 牧师 - 魔法物品
// ========================================
Remnant.removeAllTrades("priest");

// 末影珍珠换硬币
Remnant.addTrade("priest", <minecraft:ender_pearl> * 8, <abyssalcraft:coin:3>);

// 硬币换药水材料
Remnant.addTrade("priest", <abyssalcraft:coin:3> * 4, <minecraft:blaze_rod> * 2);
Remnant.addTrade("priest", <abyssalcraft:coin:3> * 3, <minecraft:glowstone_dust> * 16);
Remnant.addTrade("priest", <abyssalcraft:coin:3> * 3, <minecraft:redstone> * 32);

// 高级魔法物品
Remnant.addTrade("priest", <abyssalcraft:coin:3> * 16, <minecraft:ender_pearl> * 16);

// ========================================
// 银行家 - 贵重物品交易
// ========================================
Remnant.removeAllTrades("banker");

// 宝石换硬币（高价收购）
Remnant.addTrade("banker", <minecraft:diamond> * 4, <abyssalcraft:coin:3> * 3);
Remnant.addTrade("banker", <minecraft:emerald> * 6, <abyssalcraft:coin:3> * 2);

// 硬币换宝石（更贵）
Remnant.addTrade("banker", <abyssalcraft:coin:3> * 5, <minecraft:diamond> * 2);
Remnant.addTrade("banker", <abyssalcraft:coin:3> * 4, <minecraft:emerald> * 3);

// 终极交易：下界之星
Remnant.addTrade("banker", <abyssalcraft:coin:3> * 64, <minecraft:nether_star>);

// ========================================
// 大师铁匠 - 高级装备
// ========================================
Remnant.removeAllTrades("master_blacksmith");

// 钻石工具和武器（价格较高）
Remnant.addTrade("master_blacksmith", <abyssalcraft:coin:3> * 8, <minecraft:diamond_pickaxe>);
Remnant.addTrade("master_blacksmith", <abyssalcraft:coin:3> * 8, <minecraft:diamond_axe>);
Remnant.addTrade("master_blacksmith", <abyssalcraft:coin:3> * 10, <minecraft:diamond_sword>);

// 钻石盔甲
Remnant.addTrade("master_blacksmith", <abyssalcraft:coin:3> * 12, <minecraft:diamond_chestplate>);
Remnant.addTrade("master_blacksmith", <abyssalcraft:coin:3> * 10, <minecraft:diamond_leggings>);
Remnant.addTrade("master_blacksmith", <abyssalcraft:coin:3> * 8, <minecraft:diamond_helmet>);
Remnant.addTrade("master_blacksmith", <abyssalcraft:coin:3> * 8, <minecraft:diamond_boots>);

// ========================================
// 价格调整
// ========================================

// 设置玩家售卖物品的数量范围
Remnant.setItemSellingQuantity(<minecraft:diamond>, 3, 5);
Remnant.setItemSellingQuantity(<minecraft:emerald>, 4, 6);
Remnant.setItemSellingQuantity(<minecraft:gold_ingot>, 12, 18);
Remnant.setItemSellingQuantity(<minecraft:iron_ingot>, 16, 24);

// 设置玩家购买物品的价格范围
Remnant.setCoinSellingPrice(<minecraft:diamond>, 4, 6);
Remnant.setCoinSellingPrice(<minecraft:diamond_sword>, 8, 12);
Remnant.setCoinSellingPrice(<minecraft:enchanted_book>, 10, 15);

print("======================================");
print("ACRemnantTweaker: 残存者交易已自定义");
print("======================================");
```

---

## 总结

使用 CraftTweaker 自定义残存者交易非常简单：

1. ✅ 创建 `.zs` 脚本文件在 `scripts/` 文件夹
2. ✅ 导入 `mods.acremnanttweaker.Remnant`
3. ✅ 使用 `addTrade()` 添加交易
4. ✅ 使用 `removeTrade()` 移除交易
5. ✅ 使用 `setItemSellingQuantity()` 和 `setCoinSellingPrice()` 调整价格
6. ✅ **重启游戏**使修改生效

记住：**创意和平衡**是关键！设计有趣的交易链，让玩家有动力与残存者互动。

---

**祝你自定义交易愉快！** 🎮✨

如有问题，请查看日志文件 `logs/latest.log` 或在 GitHub 提交 Issue。
