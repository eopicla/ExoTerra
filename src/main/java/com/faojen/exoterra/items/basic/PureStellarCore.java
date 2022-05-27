package com.faojen.exoterra.items.basic;

import com.faojen.exoterra.setup.ModSetup;
import com.faojen.exoterra.setup.Registration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.checkerframework.checker.units.qual.C;

public class PureStellarCore extends Item {

    public PureStellarCore() {
        super(new Item.Properties()
                .tab(ModSetup.ITEM_GROUP)
                .rarity(Rarity.RARE)
                .stacksTo(1)
                .craftRemainder(Registration.INF_STELLAR_CORE.get()));

    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        //return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
        ItemStack sentient = new ItemStack(Registration.SENTIENT_CORE.get(), 1);
        CompoundTag level = new CompoundTag();
        CompoundTag stability = new CompoundTag();
        if(pInteractionTarget.isBaby() && !pPlayer.getLevel().isClientSide() && pInteractionTarget.getHealth() >= 1){
            MobEffect wither = MobEffects.WITHER;

            pInteractionTarget.setHealth(pInteractionTarget.getHealth() / 10);
            pInteractionTarget.addEffect(new MobEffectInstance(wither, 120, 10));

            level.putInt("level",1);
            stability.putInt("stability", SentientCore.maxStability);

            pStack.setCount(0);
            sentient.setTag(level);
            sentient.setTag(stability);

            System.out.println("set level to: " + sentient.getTag().getInt("level") + ", and set stability to: " + sentient.getTag().getInt("stability"));

            pPlayer.getInventory().add(sentient);

            return InteractionResult.PASS;
        } else

        return InteractionResult.FAIL;
    }

}
