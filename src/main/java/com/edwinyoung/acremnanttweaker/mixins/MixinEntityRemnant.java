package com.edwinyoung.acremnanttweaker.mixins;

import com.edwinyoung.acremnanttweaker.RemnantHelper;
import com.shinoow.abyssalcraft.common.entity.EntityRemnant;
import net.minecraft.village.MerchantRecipeList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for EntityRemnant to allow modification of trading recipes
 */
@Mixin(value = EntityRemnant.class, remap = false)
public abstract class MixinEntityRemnant {

    @Shadow
    private MerchantRecipeList tradingList;

    @Shadow
    public abstract int getProfession();

    /**
     * 在addDefaultEquipmentAndRecipies方法结束时注入，应用自定义交易
     */
    @Inject(method = "addDefaultEquipmentAndRecipies", at = @At("RETURN"), remap = false)
    private void onAddDefaultEquipmentAndRecipies(int par1, CallbackInfo ci) {
        if (tradingList != null) {
            RemnantHelper.applyCustomTrades(getProfession(), tradingList);
        }
    }
}
