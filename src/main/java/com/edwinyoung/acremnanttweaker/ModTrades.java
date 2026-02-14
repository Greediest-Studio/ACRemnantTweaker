package com.edwinyoung.acremnanttweaker;

import com.shinoow.abyssalcraft.api.item.ACItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * 配置Remnant交易的类
 * 所有的交易修改都在这里定义
 */
public class ModTrades {

    /**
     * 初始化所有的交易修改
     * 在PreInit阶段调用
     */
    public static void init(FMLPreInitializationEvent event) {
        ACRemnantTweaker.LOGGER.info("开始配置Remnant交易...");

        // 示例：给农民添加自定义交易
        exampleFarmerTrades();
        
        // 示例：给图书管理员添加自定义交易
        exampleLibrarianTrades();
        
        // 示例：给铁匠添加自定义交易
        exampleBlacksmithTrades();

        // 示例：修改物品价格
        examplePriceModifications();

        ACRemnantTweaker.LOGGER.info("Remnant交易配置完成！");
    }

    /**
     * 农民交易示例
     */
    private static void exampleFarmerTrades() {
        // 添加交易：16个钻石 -> 1个Elder Engraved Coin
        // RemnantHelper.addTrade(
        //     RemnantHelper.RemnantProfession.FARMER,
        //     Items.DIAMOND, 16,
        //     ACItems.elder_engraved_coin, 1
        // );

        // 添加交易：1个Elder Engraved Coin -> 64个面包
        // RemnantHelper.addTrade(
        //     RemnantHelper.RemnantProfession.FARMER,
        //     ACItems.elder_engraved_coin, 1,
        //     Items.BREAD, 64
        // );

        // 移除农民的某个交易（例如：移除所有输出为剪刀的交易）
        // RemnantHelper.removeTrade(
        //     RemnantHelper.RemnantProfession.FARMER,
        //     null,  // 任意输入
        //     Items.SHEARS  // 输出为剪刀
        // );
    }

    /**
     * 图书管理员交易示例
     */
    private static void exampleLibrarianTrades() {
        // 添加交易：8个Elder Engraved Coin -> 1个钻石
        // RemnantHelper.addTrade(
        //     RemnantHelper.RemnantProfession.LIBRARIAN,
        //     ACItems.elder_engraved_coin, 8,
        //     Items.DIAMOND, 1
        // );

        // 添加交易：1个Necronomicon + 16个Elder Engraved Coin -> 1个附魔书架
        // RemnantHelper.addTrade(
        //     RemnantHelper.RemnantProfession.LIBRARIAN,
        //     ACItems.necronomicon, 1,
        //     ACItems.elder_engraved_coin, 16,
        //     Blocks.ENCHANTING_TABLE, 1
        // );
    }

    /**
     * 铁匠交易示例
     */
    private static void exampleBlacksmithTrades() {
        // 添加交易：32个铁锭 -> 1个Elder Engraved Coin
        // RemnantHelper.addTrade(
        //     RemnantHelper.RemnantProfession.BLACKSMITH,
        //     Items.IRON_INGOT, 32,
        //     ACItems.elder_engraved_coin, 1
        // );

        // 添加交易：10个Elder Engraved Coin -> 1个钻石剑
        // RemnantHelper.addTrade(
        //     RemnantHelper.RemnantProfession.BLACKSMITH,
        //     ACItems.elder_engraved_coin, 10,
        //     Items.DIAMOND_SWORD, 1
        // );
    }

    /**
     * 物品价格修改示例
     */
    private static void examplePriceModifications() {
        // 修改钻石的售卖数量（玩家卖给Remnant）：需要1-2个钻石才能获得1个硬币
        // RemnantHelper.setItemSellingQuantity(Items.DIAMOND, 1, 2);

        // 修改钻石剑的购买价格（玩家从Remnant购买）：需要5-8个硬币
        // RemnantHelper.setCoinSellingPrice(Items.DIAMOND_SWORD, 5, 8);

        // 修改面包的购买价格：负数表示Remnant给玩家钱，玩家购买面包时获得2-4个硬币
        // RemnantHelper.setCoinSellingPrice(Items.BREAD, -4, -2);
    }

    /**
     * 获取所有职业的列表（用于参考）
     */
    public static void printProfessions() {
        ACRemnantTweaker.LOGGER.info("=== Remnant职业列表 ===");
        for (RemnantHelper.RemnantProfession profession : RemnantHelper.RemnantProfession.values()) {
            ACRemnantTweaker.LOGGER.info("  {} ({}) - ID: {}", 
                profession.name(), 
                profession.getDisplayName(), 
                profession.getId());
        }
        ACRemnantTweaker.LOGGER.info("=====================");
    }
}
