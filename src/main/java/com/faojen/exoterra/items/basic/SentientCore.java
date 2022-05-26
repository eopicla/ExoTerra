package com.faojen.exoterra.items.basic;

import com.faojen.exoterra.api.generic.ScreenUtils;
import com.faojen.exoterra.setup.ModSetup;
import com.faojen.exoterra.setup.Registration;
import com.faojen.exoterra.utils.MagicHelpers;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SentientCore extends Item {
    private int iterate;
    public static int maxStability = 10000;
    private int internalMaxStability;

    public boolean forceStable;

    /**
     * Gets the current Intelligence value in a given Sentient Core.
     * @param pStack The target core
     * @return The current intelligence level
     */
    public int getIntelligence(ItemStack pStack) {
        return pStack.getTag().getInt("level");
    }

    /**
     * Gets the current Stability value in a given Sentient Core.
     * @param pStack The target core
     * @return The current stability level
     */
    public int getStability(ItemStack pStack) {
        return pStack.getTag().getInt("stability");
    }

    /**
     * Set the Intelligence of a given Sentient Core
     * @param intelligence The new intelligence value
     * @param pStack The target core
     */
    public void setIntelligence(int intelligence, ItemStack pStack) {
        pStack.getTag().putInt("level", intelligence);
    }

    /**
     * Set a new intelligence value and return the value from the NBT tag
     * @param intelligence The new intelligence value
     * @param pStack The target core
     * @return The new intelligence level, taken from the NBT tag
     */
    public int setAndGetIntelligence(int intelligence, ItemStack pStack) {
        pStack.getTag().putInt("level", intelligence);
        return pStack.getTag().getInt("level");
    }

    /**
     * Set the stability of a given Sentient Core
     * @param stability The new stability value
     * @param pStack The target core
     */
    public void setStability(int stability, ItemStack pStack) {
        pStack.getTag().putInt("stability", stability);
    }

    /**
     * Set a new stability value and return the value from the NBT tag
     * @param stability The new stability value
     * @param pStack The target core
     * @return The new stability level, taken from the NBT tag
     */
    public int setAndGetStability(int stability, ItemStack pStack) {
        pStack.getTag().putInt("stability", stability);
        return pStack.getTag().getInt("stability");
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        erodeCore(pStack);

        if(forceStable){
            stabilizeCore(pStack);
        } else if(canStabilize(pStack, false, false)){
            System.out.println("core can stabilize!!!");
            stabilizeCore(pStack);
        }
        if(canCoreCollapse(pStack)){
            System.out.println("core can collapse!!!");
            coreCollapse(pStack, pSlotId, pEntity, pLevel);
        }
    }

    public void stabilizeCore(ItemStack pStack){
        System.out.println("core stable!!!!!!");
        setStability(SentientCore.maxStability,pStack);
    }

    public boolean canStabilize(ItemStack pStack, boolean isBypassingBehavior, boolean bypassAnswer){
        int currentstability = pStack.getTag().getInt("stability");
        int currentintel = pStack.getTag().getInt("level");
        System.out.println("can core stabilize?????");

        if(currentstability > SentientCore.maxStability * 0.1 && currentintel == 100){
            return true;
        } else if(currentstability < SentientCore.maxStability * 0.1 || currentintel < 100){
            return false;
        }

        if(isBypassingBehavior){
            return bypassAnswer;
        }

        return false;
    }

    public void erodeCore(ItemStack pStack){
        int currentstability = pStack.getTag().getInt("stability");
        int currentintel = pStack.getTag().getInt("level");
        int removeamount = currentintel / 10;

        pStack.getTag().putInt("stability", currentstability - removeamount);
    }

    public boolean canCoreCollapse(ItemStack pStack){
        System.out.println("can core collapse?");
        return pStack.getTag().getInt("stability") <= 0;
    }

    public void coreCollapse(ItemStack pStack, int pSlotId, Entity pEntity, Level pLevel){
        System.out.println("core is collapsing !!!! :)");
        ItemStack infcore = new ItemStack(Registration.INF_STELLAR_CORE.get());
        int playerX = pEntity.getBlockX();
        int playerY = pEntity.getBlockY();
        int playerZ = pEntity.getBlockZ();

        pLevel.explode(pEntity, playerX, playerY, playerZ, 16.0f,true, Explosion.BlockInteraction.DESTROY);
        System.out.println("core esploded????? :0");
        pStack.setCount(0);

    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        if(pStack.hasTag()) {
            if (getIntelligence(pStack) == 69) {
                pTooltipComponents
                        .add(new TextComponent("nice."));
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.line_break"));
            }

            if (getIntelligence(pStack) < 100 && getIntelligence(pStack) > 69) {
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.intelligence", MagicHelpers.withSuffix(getIntelligence(pStack))));
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.line_break"));
            }

            if (getIntelligence(pStack) < 69 && getIntelligence(pStack) >= 0) {
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.intelligence", MagicHelpers.withSuffix(getIntelligence(pStack))));
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.line_break"));
            }

            if (getIntelligence(pStack) == 100) {
                pTooltipComponents
                        .add(new TextComponent("\u00A72Intelligent.\u00A7r"));
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.line_break"));
            }

            // Totally Stable
            if(getStability(pStack) == SentientCore.maxStability) {
                pTooltipComponents
                        .add(new TextComponent("\u00A7aCore is stable!\u00A7r"));
            }
            // High Stability
            if(getStability(pStack) >= SentientCore.maxStability * 0.75 && getStability(pStack) < SentientCore.maxStability) {
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.stability_high", MagicHelpers.withSuffix(MagicHelpers.toPercent(getStability(pStack), SentientCore.maxStability))));
            }

            // Mid Stability
            if(getStability(pStack) < SentientCore.maxStability * 0.75 && getStability(pStack) >= SentientCore.maxStability * 0.25) {
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.stability_mid", MagicHelpers.withSuffix(MagicHelpers.toPercent(getStability(pStack), SentientCore.maxStability))));
                pTooltipComponents
                        .add(new TextComponent("(Kept comfortable in a \u00A73Machine Body\u00A7r)"));
            }

            // Low Stability
            if(getStability(pStack) < SentientCore.maxStability * 0.25 && getStability(pStack) >= SentientCore.maxStability * 0.10) {
                pTooltipComponents
                        .add(new TranslatableComponent("itemHover.exoterra.stability_low", MagicHelpers.withSuffix(MagicHelpers.toPercent(getStability(pStack), SentientCore.maxStability))));
                pTooltipComponents
                        .add(new TextComponent("(Kept comfortable in a \u00A73Machine Body\u00A7r)"));
            }

            if(getStability(pStack) < SentientCore.maxStability * 0.1) {
                pTooltipComponents
                        .add(new TextComponent("\u00A74Core unstable!\u00A7r"));
                pTooltipComponents
                        .add(new TextComponent("(Kept comfortable in a \u00A73Machine Body\u00A7r)"));
            }
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
//        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);

        if(pInteractionTarget.isBaby() && pInteractionTarget.getHealth() >= 1 && getIntelligence(pStack) < 100 && !pPlayer.getLevel().isClientSide()){
            iterate = getIntelligence(pStack) + 1;
            MobEffect wither = MobEffects.WITHER;
            MobEffect poison = MobEffects.POISON;

            // pInteractionTarget.kill();
            pInteractionTarget.setHealth(pInteractionTarget.getHealth() / 10);
            pInteractionTarget.addEffect(new MobEffectInstance(wither, 120, 10));

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
            return getIntelligence(pStack) == 100;
        }

}

