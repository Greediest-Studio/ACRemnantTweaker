// ACRemnantTweaker - 示例ZenScript脚本
// 将此文件放在 scripts/ 目录下使用
// 文件名: remnant_trades_example.zs

#priority 10

import mods.acremnanttweaker.Remnant;

// ========================================
// 农民 (Farmer) 交易
// ========================================

// 添加：16个钻石 -> 1个Elder Engraved Coin
Remnant.addTrade("farmer", <minecraft:diamond> * 16, <abyssalcraft:coin:3>);

// 添加：5个Elder Engraved Coin -> 1个金苹果
Remnant.addTrade("farmer", <abyssalcraft:coin:3> * 5, <minecraft:golden_apple>);

// 移除：所有输出为剪刀的交易
// Remnant.removeTrade("farmer", null, <minecraft:shears>);


// ========================================
// 图书管理员 (Librarian) 交易
// ========================================

// 添加：1个Necronomicon + 16个Elder Engraved Coin -> 1个附魔台
Remnant.addTrade("librarian", 
    <abyssalcraft:necronomicon>, 
    <abyssalcraft:coin:3> * 16, 
    <minecraft:enchanting_table>
);

// 添加：10个Elder Engraved Coin -> 1个钻石
Remnant.addTrade("librarian", <abyssalcraft:coin:3> * 10, <minecraft:diamond>);


// ========================================
// 牧师 (Priest) 交易
// ========================================

// 添加：16个腐肉 + 8个Elder Engraved Coin -> 3个经验瓶
Remnant.addTrade("priest",
    <minecraft:rotten_flesh> * 16,
    <abyssalcraft:coin:3> * 8,
    <minecraft:experience_bottle> * 3
);


// ========================================
// 铁匠 (Blacksmith) 交易
// ========================================

// 添加：32个铁锭 -> 1个Elder Engraved Coin
Remnant.addTrade("blacksmith", <minecraft:iron_ingot> * 32, <abyssalcraft:coin:3>);

// 添加：20个Elder Engraved Coin -> 1个钻石剑
Remnant.addTrade("blacksmith", <abyssalcraft:coin:3> * 20, <minecraft:diamond_sword>);

// 移除：所有使用煤炭作为输入的交易
// Remnant.removeTrade("blacksmith", <minecraft:coal>, null);


// ========================================
// 屠夫 (Butcher) 交易
// ========================================

// 添加自定义食物交易
Remnant.addTrade("butcher", <abyssalcraft:coin:3> * 3, <minecraft:cooked_beef> * 16);


// ========================================
// 银行家 (Banker) 交易
// ========================================

// 银行家主要交易硬币，一般不需要修改


// ========================================
// 大师铁匠 (Master Blacksmith) 交易
// ========================================

// 添加：30个Elder Engraved Coin -> 1个钻石胸甲
Remnant.addTrade("master_blacksmith", <abyssalcraft:coin:3> * 30, <minecraft:diamond_chestplate>);

// 添加：25个Elder Engraved Coin -> 1个钻石镐
Remnant.addTrade("master_blacksmith", <abyssalcraft:coin:3> * 25, <minecraft:diamond_pickaxe>);


// ========================================
// 物品售卖数量设置
// ========================================

// 设置钻石的售卖数量：玩家需要卖1-2个钻石才能获得1个硬币
Remnant.setItemSellingQuantity(<minecraft:diamond>, 1, 2);

// 设置铁锭的售卖数量：玩家需要卖32-48个铁锭才能获得1个硬币
Remnant.setItemSellingQuantity(<minecraft:iron_ingot>, 32, 48);

// 设置小麦的售卖数量：玩家需要卖18-22个小麦才能获得1个硬币
Remnant.setItemSellingQuantity(<minecraft:wheat>, 18, 22);


// ========================================
// 物品购买价格设置
// ========================================

// 设置钻石剑的购买价格：需要5-8个硬币
Remnant.setCoinSellingPrice(<minecraft:diamond_sword>, 5, 8);

// 设置面包的价格：负数表示玩家购买时获得钱（Remnant给钱）
// 玩家买面包时，Remnant会给玩家2-4个硬币
Remnant.setCoinSellingPrice(<minecraft:bread>, -4, -2);

// 设置Ethaxium剑的价格：需要12-14个硬币
// Remnant.setCoinSellingPrice(<abyssalcraft:ethaxiumsword>, 12, 14);


// ========================================
// 批量移除示例
// ========================================

// 移除农民的所有交易（慎用！）
// Remnant.removeAllTrades("farmer");

// 移除所有职业中输出为钻石的交易
// Remnant.removeTrade("farmer", null, <minecraft:diamond>);
// Remnant.removeTrade("librarian", null, <minecraft:diamond>);
// Remnant.removeTrade("priest", null, <minecraft:diamond>);
// Remnant.removeTrade("blacksmith", null, <minecraft:diamond>);
// Remnant.removeTrade("butcher", null, <minecraft:diamond>);
// Remnant.removeTrade("banker", null, <minecraft:diamond>);
// Remnant.removeTrade("master_blacksmith", null, <minecraft:diamond>);


// ========================================
// 注意事项
// ========================================
// 1. 所有交易修改需要重启游戏才能生效
// 2. 使用 /ct reload 只能重载脚本，但不会重新生成Remnant的交易
// 3. 交易会在Remnant首次生成交易列表时应用
// 4. 建议先在创造模式中测试配置
// 5. 查看游戏日志可以看到详细的交易添加/移除信息
