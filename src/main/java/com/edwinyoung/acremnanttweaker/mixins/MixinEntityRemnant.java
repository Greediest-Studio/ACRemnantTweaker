package com.edwinyoung.acremnanttweaker.mixins;

import com.edwinyoung.acremnanttweaker.RemnantHelper;
import com.shinoow.abyssalcraft.common.entity.EntityRemnant;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.Random;

/**
 * Mixin for EntityRemnant to allow modification of trading recipes
 */
@Mixin(value = EntityRemnant.class, remap = false)
public abstract class MixinEntityRemnant {

    // 使用静态 Random 实例用于概率检查
    private static final Random TRADE_RANDOM = new Random();

    @Shadow
    public abstract int getProfession();

    /**
     * 在 shuffle 之前修改传递给 Collections.shuffle() 的列表
     * 直接拦截方法参数，添加自定义交易到候选池
     */
    @ModifyArg(
        method = "addDefaultEquipmentAndRecipies",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Collections;shuffle(Ljava/util/List;)V"
        ),
        index = 0,
        remap = false
    )
    private List<MerchantRecipe> injectCustomTradesBeforeShuffle(List<MerchantRecipe> list) {
        // 将 List 强制转换为 MerchantRecipeList
        if (list instanceof MerchantRecipeList) {
            RemnantHelper.addCustomTradesToPool(getProfession(), (MerchantRecipeList)list, TRADE_RANDOM);
        }
        return list;
    }
}
