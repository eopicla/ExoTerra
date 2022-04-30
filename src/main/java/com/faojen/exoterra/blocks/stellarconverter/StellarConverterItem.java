package com.faojen.exoterra.blocks.stellarconverter;

import com.faojen.exoterra.Config;
import com.faojen.exoterra.utils.MagicHelpers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class StellarConverterItem extends BlockItem {

    public StellarConverterItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        int power = stack.getOrCreateTag().getInt("energy");
        FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
        Integer fluidamount = fluid.getAmount();
        if (power == 0)
            return;

        tooltip.add(new TranslatableComponent("screen.exoterra.energy", MagicHelpers.withSuffix(power), MagicHelpers.withSuffix(Config.GENERAL.chargerMaxPower.get())));
        tooltip.add(new TranslatableComponent("screen.exoterra.stellar", MagicHelpers.withSuffix(fluidamount), MagicHelpers.withSuffix(StellarConverterBE.FLUID_CAP_PUB)));
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level worldIn, @Nullable Player player, ItemStack stack, BlockState state) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof StellarConverterBE) {
        	StellarConverterBE station = (StellarConverterBE) te;
        	FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag());
        	
            station.energyStorage.receiveEnergy(stack.getOrCreateTag().getInt("energy"), false);
            station.fluidStorage.setFluid(fluid);
        }

        return super.updateCustomBlockEntityTag(pos, worldIn, player, stack, state);
    }
}
