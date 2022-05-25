package com.faojen.exoterra.items.basic;

import com.faojen.exoterra.api.generic.ScreenUtils;
import com.faojen.exoterra.setup.ModSetup;
import com.faojen.exoterra.setup.Registration;
import com.faojen.exoterra.utils.MagicHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SentientCore extends Item {
    private int iterate;
    public int getIntelligence(ItemStack pStack) {
        return pStack.getTag().getInt("level");
    }

    public void setIntelligence(int intelligence, ItemStack pStack) {
        pStack.getTag().putInt("level", intelligence);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {

    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        if(pStack.hasTag()) {
            if (getIntelligence(pStack) == 69) {
                pTooltipComponents
                        .add(new TextComponent("nice."));
            }

            if (getIntelligence(pStack) < 100) {
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.intelligence", MagicHelpers.withSuffix(getIntelligence(pStack))));
            }

            if (getIntelligence(pStack) == 100) {
                pTooltipComponents
                        .add(new TextComponent("\u00A72Intelligent.\u00A7r"));
            }
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
//        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);

        if(pInteractionTarget.isBaby() && getIntelligence(pStack) < 100 && !pPlayer.getLevel().isClientSide()){
            iterate = getIntelligence(pStack) + 1;

            pInteractionTarget.kill();
            setIntelligence(iterate, pStack);

            return InteractionResult.PASS;
        } else

            return InteractionResult.FAIL;
    }

    public SentientCore() {
            super(new Item.Properties()
                    .tab(ModSetup.ITEM_GROUP)
                    .rarity(Rarity.EPIC)
                    .stacksTo(1));

        }

        @Override
        public boolean isFoil(ItemStack pStack) {
            return true;
        }

}

