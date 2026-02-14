package com.edwinyoung.acremnanttweaker;

import com.shinoow.abyssalcraft.common.entity.EntityRemnant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.*;

/**
 * Helper class for modifying Remnant trading recipes
 * Allows adding, removing, and modifying trades for specific professions
 */
public class RemnantHelper {

    /**
     * Remnant职业枚举
     */
    public enum RemnantProfession {
        FARMER(0, "农民"),
        LIBRARIAN(1, "图书管理员"),
        PRIEST(2, "牧师"),
        BLACKSMITH(3, "铁匠"),
        BUTCHER(4, "屠夫"),
        BANKER(5, "银行家"),
        MASTER_BLACKSMITH(6, "大师铁匠");

        private final int id;
        private final String displayName;

        RemnantProfession(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public int getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static RemnantProfession fromId(int id) {
            for (RemnantProfession profession : values()) {
                if (profession.id == id) {
                    return profession;
                }
            }
            return FARMER;
        }
    }

    /**
     * 存储每个职业的自定义交易
     * Key: 职业ID, Value: 该职业的交易列表
     */
    private static final Map<Integer, List<MerchantRecipe>> CUSTOM_TRADES = new HashMap<>();

    /**
     * 存储要移除的交易（通过匹配输入输出物品）
     * Key: 职业ID, Value: 要移除的交易列表
     */
    private static final Map<Integer, List<TradeRemovalPattern>> TRADE_REMOVALS = new HashMap<>();

    /**
     * 添加一个新的交易到指定职业
     * 
     * @param profession 职业
     * @param input1 第一个输入物品
     * @param output 输出物品
     */
    public static void addTrade(RemnantProfession profession, ItemStack input1, ItemStack output) {
        addTrade(profession, input1, ItemStack.EMPTY, output);
    }

    /**
     * 添加一个新的交易到指定职业（带两个输入物品）
     * 
     * @param profession 职业
     * @param input1 第一个输入物品
     * @param input2 第二个输入物品
     * @param output 输出物品
     */
    public static void addTrade(RemnantProfession profession, ItemStack input1, ItemStack input2, ItemStack output) {
        if (input1.isEmpty() || output.isEmpty()) {
            ACRemnantTweaker.LOGGER.warn("尝试添加无效的交易：输入或输出为空");
            return;
        }

        MerchantRecipe recipe = new MerchantRecipe(input1, input2, output);
        
        CUSTOM_TRADES.computeIfAbsent(profession.getId(), k -> new ArrayList<>()).add(recipe);
        
        ACRemnantTweaker.LOGGER.info("添加交易到职业 {} ({}): {} + {} -> {}", 
            profession.getDisplayName(), profession.getId(),
            input1.getDisplayName(), 
            input2.isEmpty() ? "无" : input2.getDisplayName(), 
            output.getDisplayName());
    }

    /**
     * 添加一个新的交易到指定职业
     * 
     * @param profession 职业
     * @param input1 第一个输入物品
     * @param input1Count 第一个输入物品数量
     * @param output 输出物品
     * @param outputCount 输出物品数量
     */
    public static void addTrade(RemnantProfession profession, Item input1, int input1Count, Item output, int outputCount) {
        addTrade(profession, new ItemStack(input1, input1Count), new ItemStack(output, outputCount));
    }

    /**
     * 添加一个新的交易到指定职业（带两个输入物品）
     * 
     * @param profession 职业
     * @param input1 第一个输入物品
     * @param input1Count 第一个输入物品数量
     * @param input2 第二个输入物品
     * @param input2Count 第二个输入物品数量
     * @param output 输出物品
     * @param outputCount 输出物品数量
     */
    public static void addTrade(RemnantProfession profession, Item input1, int input1Count, Item input2, int input2Count, Item output, int outputCount) {
        addTrade(profession, new ItemStack(input1, input1Count), new ItemStack(input2, input2Count), new ItemStack(output, outputCount));
    }

    /**
     * 移除指定职业中匹配特定输入/输出物品的交易
     * 
     * @param profession 职业
     * @param inputItem 输入物品（null表示任意）
     * @param outputItem 输出物品（null表示任意）
     */
    public static void removeTrade(RemnantProfession profession, Item inputItem, Item outputItem) {
        TradeRemovalPattern pattern = new TradeRemovalPattern(inputItem, outputItem);
        TRADE_REMOVALS.computeIfAbsent(profession.getId(), k -> new ArrayList<>()).add(pattern);
        
        ACRemnantTweaker.LOGGER.info("标记移除职业 {} ({}) 的交易: 输入={}, 输出={}", 
            profession.getDisplayName(), profession.getId(),
            inputItem != null ? inputItem.getRegistryName() : "任意",
            outputItem != null ? outputItem.getRegistryName() : "任意");
    }

    /**
     * 移除指定职业的所有交易
     * 
     * @param profession 职业
     */
    public static void removeAllTrades(RemnantProfession profession) {
        removeTrade(profession, null, null);
    }

    /**
     * 修改物品售卖数量范围（玩家卖给Remnant）
     * 
     * @param item 物品
     * @param min 最小数量
     * @param max 最大数量
     */
    public static void setItemSellingQuantity(Item item, int min, int max) {
        EntityRemnant.itemSellingList.put(item, new Tuple<>(min, max));
        ACRemnantTweaker.LOGGER.info("设置物品 {} 的售卖数量范围: {}-{}", item.getRegistryName(), min, max);
    }

    /**
     * 修改物品购买价格范围（玩家从Remnant购买）
     * 
     * @param item 物品
     * @param min 最小价格（硬币数量，负数表示玩家收钱）
     * @param max 最大价格（硬币数量，负数表示玩家收钱）
     */
    public static void setCoinSellingPrice(Item item, int min, int max) {
        EntityRemnant.coinSellingList.put(item, new Tuple<>(min, max));
        ACRemnantTweaker.LOGGER.info("设置物品 {} 的购买价格范围: {}-{}", item.getRegistryName(), min, max);
    }

    /**
     * 应用自定义交易到交易列表
     * 该方法应该在交易列表生成后调用
     * 
     * @param profession 职业ID
     * @param recipeList 交易列表
     */
    public static void applyCustomTrades(int profession, MerchantRecipeList recipeList) {
        if (recipeList == null) {
            return;
        }

        // 移除标记的交易
        List<TradeRemovalPattern> removals = TRADE_REMOVALS.get(profession);
        if (removals != null && !removals.isEmpty()) {
            Iterator<MerchantRecipe> iterator = recipeList.iterator();
            while (iterator.hasNext()) {
                MerchantRecipe recipe = iterator.next();
                for (TradeRemovalPattern pattern : removals) {
                    if (pattern.matches(recipe)) {
                        iterator.remove();
                        ACRemnantTweaker.LOGGER.debug("移除交易: {} -> {}", 
                            recipe.getItemToBuy().getDisplayName(), 
                            recipe.getItemToSell().getDisplayName());
                        break;
                    }
                }
            }
        }

        // 添加自定义交易
        List<MerchantRecipe> customTrades = CUSTOM_TRADES.get(profession);
        if (customTrades != null && !customTrades.isEmpty()) {
            for (MerchantRecipe recipe : customTrades) {
                // 创建新的副本以避免共享问题
                MerchantRecipe newRecipe = new MerchantRecipe(
                    recipe.getItemToBuy().copy(),
                    recipe.getSecondItemToBuy().isEmpty() ? ItemStack.EMPTY : recipe.getSecondItemToBuy().copy(),
                    recipe.getItemToSell().copy()
                );
                recipeList.add(newRecipe);
                ACRemnantTweaker.LOGGER.debug("添加自定义交易到职业 {}: {} + {} -> {}", 
                    profession,
                    newRecipe.getItemToBuy().getDisplayName(),
                    newRecipe.getSecondItemToBuy().isEmpty() ? "无" : newRecipe.getSecondItemToBuy().getDisplayName(),
                    newRecipe.getItemToSell().getDisplayName());
            }
        }
    }

    /**
     * 清除所有自定义交易配置（用于重载）
     */
    public static void clearAll() {
        CUSTOM_TRADES.clear();
        TRADE_REMOVALS.clear();
        ACRemnantTweaker.LOGGER.info("清除所有自定义Remnant交易配置");
    }

    /**
     * 交易移除匹配模式
     */
    private static class TradeRemovalPattern {
        private final Item inputItem;
        private final Item outputItem;

        public TradeRemovalPattern(Item inputItem, Item outputItem) {
            this.inputItem = inputItem;
            this.outputItem = outputItem;
        }

        public boolean matches(MerchantRecipe recipe) {
            boolean inputMatches = inputItem == null || 
                recipe.getItemToBuy().getItem() == inputItem || 
                (!recipe.getSecondItemToBuy().isEmpty() && recipe.getSecondItemToBuy().getItem() == inputItem);
            
            boolean outputMatches = outputItem == null || 
                recipe.getItemToSell().getItem() == outputItem;

            return inputMatches && outputMatches;
        }
    }
}
