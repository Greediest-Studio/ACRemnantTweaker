package com.edwinyoung.acremnanttweaker.integration.crafttweaker;

import com.edwinyoung.acremnanttweaker.ACRemnantTweaker;
import com.edwinyoung.acremnanttweaker.RemnantHelper;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * CraftTweaker集成类 - 允许通过ZenScript修改Remnant交易
 * 
 * ZenScript类名: mods.acremnanttweaker.Remnant
 * 
 * 使用示例:
 * <pre>
 * import mods.acremnanttweaker.Remnant;
 * 
 * // 添加交易
 * Remnant.addTrade("farmer", <minecraft:diamond> * 16, <abyssalcraft:coin:3>);
 * 
 * // 移除交易
 * Remnant.removeTrade("blacksmith", <minecraft:iron_ingot>, <abyssalcraft:ethaxiumsword>);
 * </pre>
 */
@ZenClass("mods.acremnanttweaker.Remnant")
@ZenRegister
@ModOnly("abyssalcraft")
public class Remnant {

    /**
     * 添加一个新的交易到指定职业（单个输入物品）
     * 
     * @param profession 职业名称 (farmer/librarian/priest/blacksmith/butcher/banker/master_blacksmith)
     * @param input 输入物品
     * @param output 输出物品
     * 
     * 示例:
     * <pre>
     * Remnant.addTrade("farmer", <minecraft:diamond> * 16, <abyssalcraft:coin:3>);
     * </pre>
     */
    @ZenMethod
    public static void addTrade(String profession, IItemStack input, IItemStack output) {
        ACRemnantTweaker.LOGGER.info("[CraftTweaker] Calling addTrade: profession={}, input={}, output={}", 
            profession, input, output);
        try {
            RemnantHelper.RemnantProfession prof = parseProfession(profession);
            ItemStack mcInput = CraftTweakerMC.getItemStack(input);
            ItemStack mcOutput = CraftTweakerMC.getItemStack(output);
            ACRemnantTweaker.LOGGER.info("[CraftTweaker] Converted ItemStack: input={} (isEmpty={}), output={} (isEmpty={})",
                mcInput, mcInput.isEmpty(), mcOutput, mcOutput.isEmpty());
            RemnantHelper.addTrade(
                prof,
                mcInput,
                mcOutput
            );
        } catch (Exception e) {
            ACRemnantTweaker.LOGGER.error("Failed to add Remnant trade: " + e.getMessage(), e);
        }
    }

    /**
     * 添加一个新的交易到指定职业（两个输入物品）
     * 
     * @param profession 职业名称
     * @param input1 第一个输入物品
     * @param input2 第二个输入物品
     * @param output 输出物品
     * 
     * 示例:
     * <pre>
     * Remnant.addTrade("librarian", <abyssalcraft:necronomicon>, <abyssalcraft:coin:3> * 16, <minecraft:enchanted_book>);
     * </pre>
     */
    @ZenMethod
    public static void addTrade(String profession, IItemStack input1, IItemStack input2, IItemStack output) {
        ACRemnantTweaker.LOGGER.info("[CraftTweaker] Calling addTrade(3 params): profession={}, input1={}, input2={}, output={}", 
            profession, input1, input2, output);
        try {
            RemnantHelper.RemnantProfession prof = parseProfession(profession);
            ItemStack mcInput1 = CraftTweakerMC.getItemStack(input1);
            ItemStack mcInput2 = CraftTweakerMC.getItemStack(input2);
            ItemStack mcOutput = CraftTweakerMC.getItemStack(output);
            ACRemnantTweaker.LOGGER.info("[CraftTweaker] Converted ItemStack: input1={} (isEmpty={}), input2={} (isEmpty={}), output={} (isEmpty={})",
                mcInput1, mcInput1.isEmpty(), mcInput2, mcInput2.isEmpty(), mcOutput, mcOutput.isEmpty());
            RemnantHelper.addTrade(
                prof,
                mcInput1,
                mcInput2,
                mcOutput
            );
        } catch (Exception e) {
            ACRemnantTweaker.LOGGER.error("Failed to add Remnant trade: " + e.getMessage(), e);
        }
    }

    /**
     * 添加一个新的交易到指定职业（单个输入物品，自定义概率）
     * 
     * @param profession 职业名称
     * @param input 输入物品
     * @param output 输出物品
     * @param probability 交易出现的概率，范围 0.0～1.0（0.0=永不出现，1.0=必定出现，0.5=50%概率）
     * 
     * 示例:
     * <pre>
     * // 低概率稀有交易（10%概率）
     * Remnant.addTrade("cleric", <abyssalcraft:coin:3> * 32, <minecraft:diamond>, 0.1);
     * // 高概率常见交易（80%概率）  
     * Remnant.addTrade("blacksmith", <abyssalcraft:coin:3> * 8, <minecraft:iron_ingot>, 0.8);
     * </pre>
     */
    @ZenMethod
    public static void addTrade(String profession, IItemStack input, IItemStack output, float probability) {
        ACRemnantTweaker.LOGGER.info("[CraftTweaker] Calling addTrade with probability: profession={}, input={}, output={}, probability={}", 
            profession, input, output, probability);
        try {
            RemnantHelper.RemnantProfession prof = parseProfession(profession);
            ItemStack mcInput = CraftTweakerMC.getItemStack(input);
            ItemStack mcOutput = CraftTweakerMC.getItemStack(output);
            RemnantHelper.addTrade(
                prof,
                mcInput,
                mcOutput,
                probability
            );
        } catch (Exception e) {
            ACRemnantTweaker.LOGGER.error("Failed to add Remnant trade: " + e.getMessage(), e);
        }
    }

    /**
     * 添加一个新的交易到指定职业（两个输入物品，自定义概率）
     * 
     * @param profession 职业名称
     * @param input1 第一个输入物品
     * @param input2 第二个输入物品
     * @param output 输出物品
     * @param probability 交易出现的概率，范围 0.0～1.0（0.0=永不出现，1.0=必定出现，0.5=50%概率）
     * 
     * 示例:
     * <pre>
     * // 低概率稀有交易（20%概率）
     * Remnant.addTrade("librarian", <abyssalcraft:necronomicon>, <abyssalcraft:coin:3> * 16, <minecraft:enchanted_book>, 0.2);
     * // 必定出现的交易（100%概率，等同于不指定概率）
     * Remnant.addTrade("butcher", <abyssalcraft:coin:3> * 20, <abyssalcraft:dreadcloth> * 8, <additions:nefrath_cloth>, 1.0);
     * </pre>
     */
    @ZenMethod
    public static void addTrade(String profession, IItemStack input1, IItemStack input2, IItemStack output, float probability) {
        ACRemnantTweaker.LOGGER.info("[CraftTweaker] Calling addTrade(3 params) with probability: profession={}, input1={}, input2={}, output={}, probability={}", 
            profession, input1, input2, output, probability);
        try {
            RemnantHelper.RemnantProfession prof = parseProfession(profession);
            ItemStack mcInput1 = CraftTweakerMC.getItemStack(input1);
            ItemStack mcInput2 = CraftTweakerMC.getItemStack(input2);
            ItemStack mcOutput = CraftTweakerMC.getItemStack(output);
            RemnantHelper.addTrade(
                prof,
                mcInput1,
                mcInput2,
                mcOutput,
                probability
            );
        } catch (Exception e) {
            ACRemnantTweaker.LOGGER.error("Failed to add Remnant trade: " + e.getMessage(), e);
        }
    }

    /**
     * 移除指定职业中匹配特定输入/输出物品的交易
     * 
     * @param profession 职业名称
     * @param input 输入物品（null表示任意）
     * @param output 输出物品（null表示任意）
     * 
     * 示例:
     * <pre>
     * // 移除农民所有输出为剪刀的交易
     * Remnant.removeTrade("farmer", null, <minecraft:shears>);
     * 
     * // 移除铁匠所有使用钻石作为输入的交易
     * Remnant.removeTrade("blacksmith", <minecraft:diamond>, null);
     * </pre>
     */
    @ZenMethod
    public static void removeTrade(String profession, @Optional IItemStack input, @Optional IItemStack output) {
        try {
            RemnantHelper.RemnantProfession prof = parseProfession(profession);
            RemnantHelper.removeTrade(
                prof,
                input == null ? null : CraftTweakerMC.getItemStack(input).getItem(),
                output == null ? null : CraftTweakerMC.getItemStack(output).getItem()
            );
        } catch (Exception e) {
            ACRemnantTweaker.LOGGER.error("移除Remnant交易失败: " + e.getMessage(), e);
        }
    }

    /**
     * 移除指定职业的所有交易
     * 
     * @param profession 职业名称
     * 
     * 示例:
     * <pre>
     * Remnant.removeAllTrades("farmer");
     * </pre>
     */
    @ZenMethod
    public static void removeAllTrades(String profession) {
        try {
            RemnantHelper.RemnantProfession prof = parseProfession(profession);
            RemnantHelper.removeAllTrades(prof);
        } catch (Exception e) {
            ACRemnantTweaker.LOGGER.error("移除所有Remnant交易失败: " + e.getMessage(), e);
        }
    }

    /**
     * 修改物品售卖数量范围（玩家卖给Remnant）
     * 
     * @param item 物品
     * @param min 最小数量
     * @param max 最大数量
     * 
     * 示例:
     * <pre>
     * // 设置钻石的售卖数量：玩家需要卖1-2个钻石才能获得1个硬币
     * Remnant.setItemSellingQuantity(<minecraft:diamond>, 1, 2);
     * </pre>
     */
    @ZenMethod
    public static void setItemSellingQuantity(IItemStack item, int min, int max) {
        try {
            RemnantHelper.setItemSellingQuantity(
                CraftTweakerMC.getItemStack(item).getItem(),
                min,
                max
            );
        } catch (Exception e) {
            ACRemnantTweaker.LOGGER.error("设置物品售卖数量失败: " + e.getMessage(), e);
        }
    }

    /**
     * 修改物品购买价格范围（玩家从Remnant购买）
     * 
     * @param item 物品
     * @param min 最小价格（硬币数量，负数表示玩家收钱）
     * @param max 最大价格（硬币数量，负数表示玩家收钱）
     * 
     * 示例:
     * <pre>
     * // 设置钻石剑的购买价格：需要5-8个硬币
     * Remnant.setCoinSellingPrice(<minecraft:diamond_sword>, 5, 8);
     * 
     * // 设置面包的价格：玩家购买时获得钱（Remnant给钱）
     * Remnant.setCoinSellingPrice(<minecraft:bread>, -4, -2);
     * </pre>
     */
    @ZenMethod
    public static void setCoinSellingPrice(IItemStack item, int min, int max) {
        try {
            RemnantHelper.setCoinSellingPrice(
                CraftTweakerMC.getItemStack(item).getItem(),
                min,
                max
            );
        } catch (Exception e) {
            ACRemnantTweaker.LOGGER.error("设置硬币售卖价格失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解析职业名称为职业枚举
     * 
     * @param professionName 职业名称
     * @return 职业枚举
     */
    private static RemnantHelper.RemnantProfession parseProfession(String professionName) {
        if (professionName == null) {
            throw new IllegalArgumentException("职业名称不能为null");
        }

        switch (professionName.toLowerCase().trim()) {
            case "farmer":
            case "农民":
                return RemnantHelper.RemnantProfession.FARMER;
            case "librarian":
            case "图书管理员":
                return RemnantHelper.RemnantProfession.LIBRARIAN;
            case "priest":
            case "牧师":
                return RemnantHelper.RemnantProfession.PRIEST;
            case "blacksmith":
            case "铁匠":
                return RemnantHelper.RemnantProfession.BLACKSMITH;
            case "butcher":
            case "屠夫":
                return RemnantHelper.RemnantProfession.BUTCHER;
            case "banker":
            case "银行家":
                return RemnantHelper.RemnantProfession.BANKER;
            case "master_blacksmith":
            case "masterblacksmith":
            case "大师铁匠":
                return RemnantHelper.RemnantProfession.MASTER_BLACKSMITH;
            default:
                throw new IllegalArgumentException("未知的职业名称: " + professionName + 
                    ". 有效值: farmer, librarian, priest, blacksmith, butcher, banker, master_blacksmith");
        }
    }
}
