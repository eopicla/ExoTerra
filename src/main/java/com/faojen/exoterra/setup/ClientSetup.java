package com.faojen.exoterra.setup;

import com.faojen.exoterra.blocks.compowerbank.CommonPowerBankScreen;
import com.faojen.exoterra.blocks.crystalcatalyst.CrystalCatalystScreen;
import com.faojen.exoterra.blocks.infpowerbank.InferiorPowerBankScreen;
import com.faojen.exoterra.blocks.purificationbestower.PurificationBestowerScreen;
import com.faojen.exoterra.blocks.superiorpowerbank.SuperiorPowerBankScreen;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent event) {
    	
        event.enqueueWork(() -> {
        	
            MenuScreens.register(Registration.PURIFICATION_BESTOWER_CONTAINER.get(), PurificationBestowerScreen::new);
            MenuScreens.register(Registration.CRYSTAL_CATALYST_CONTAINER.get(), CrystalCatalystScreen::new);
            MenuScreens.register(Registration.INFERIOR_POWER_BANK_CONTAINER.get(), InferiorPowerBankScreen::new);
            MenuScreens.register(Registration.COMMON_POWER_BANK_CONTAINER.get(), CommonPowerBankScreen::new);
            MenuScreens.register(Registration.SUPERIOR_POWER_BANK_CONTAINER.get(), SuperiorPowerBankScreen::new);
            
            ItemBlockRenderTypes.setRenderLayer(Registration.AQUEOUS_STELLAR.get(), renderType -> renderType == RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(Registration.FLOWING_AQUEOUS_STELLAR.get(), renderType -> renderType == RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(Registration.EXO_GLASS_BLOCK.get(), renderType -> renderType == RenderType.cutout());

            
            
        });
    }
}