package com.faojen.exoterra.setup;

import static com.faojen.exoterra.ExoTerra.MODID;

import com.faojen.exoterra.ExoTerra;
import com.faojen.exoterra.blocks.AqueousStellarBlock;
import com.faojen.exoterra.blocks.StellarConverterBlock;
import com.faojen.exoterra.blocks.container.StellarConverterContainer;
import com.faojen.exoterra.blocks.entity.StellarConverterBE;
import com.faojen.exoterra.fluid.AqueousStellarFluid;
import com.faojen.exoterra.item.AqueousStellarItem;
import com.faojen.exoterra.item.StellarConverterItem;
import com.faojen.exoterra.item.InfRefinedStellar;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {

	
	private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
	private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(bus);
		ITEMS.register(bus);
		BLOCK_ENTITIES.register(bus);
		CONTAINERS.register(bus);
		FLUIDS.register(bus);

	} 
	
	// Some common properties for our blocks and items
	public static final BlockBehaviour.Properties ORE_PROPERTIES = BlockBehaviour.Properties.of(Material.STONE).strength(2f).requiresCorrectToolForDrops();
	public static final BlockBehaviour.Properties SIMPLE_BLOCK = BlockBehaviour.Properties.of(Material.METAL).strength(2f).requiresCorrectToolForDrops();
	public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);
	public static final Item.Properties FUEL_STELLAR = new Item.Properties().tab(ModSetup.ITEM_GROUP).rarity(Rarity.RARE).setNoRepair();

	
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------- //
	// 																										Complex Registers							   																	    //
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------- //
	// Block
		
	// Item
	public static final RegistryObject<InfRefinedStellar> INF_REFINED_STELLAR = ITEMS.register("inf_refined_stellar", InfRefinedStellar::new);
	
	// Fluid
	
	public static final RegistryObject<Fluid> AQUEOUS_STELLAR = FLUIDS.register("aqueous_stellar", () -> new AqueousStellarFluid.Source());
	public static final RegistryObject<Fluid> FLOWING_AQUEOUS_STELLAR = FLUIDS.register("flowing_aqueous_stellar",
			() -> new AqueousStellarFluid.Flowing());
	
	public static final RegistryObject<Block> AQUEOUS_STELLAR_BLOCK = BLOCKS.register("aqueous_stellar", () -> new AqueousStellarBlock());
	
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------//
	public static final RegistryObject<Block> STELLAR_CONVERTER = BLOCKS.register("stellar_converter", StellarConverterBlock::new);

    /**
     * Tile Entities
     */
    public static final RegistryObject<BlockEntityType<StellarConverterBE>> STELLAR_CONVERTER_BE =
            BLOCK_ENTITIES.register("stellar_converter_be", () -> BlockEntityType.Builder.of(StellarConverterBE::new, STELLAR_CONVERTER.get()).build(null));

    /**
     * Containers?
     */
    public static final RegistryObject<MenuType<StellarConverterContainer>> STELLAR_CONVERTER_CONTAINER = CONTAINERS.register("stellar_converter_container", 
    		() -> IForgeMenuType.create(StellarConverterContainer::new));


    /**
     * For now I'm adding items into here, it doesn't make much sense but nor does an items package for a mod with no
     * items... so... when we add items. Move this!
     */
      public static final RegistryObject<Item> STELLAR_CONVERTER_BI = ITEMS.register("stellar_converter", 
    		() -> new StellarConverterItem(STELLAR_CONVERTER.get(), Registration.ITEM_PROPERTIES));
    

	
	
	
	
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------- //
	
/*
 * 		ORES
 */
    public static final RegistryObject<Block> STELLAR_ORE_OVERWORLD = BLOCKS.register("stellar_ore_overworld",() -> new Block(ORE_PROPERTIES));
	public static final RegistryObject<Item> STELLAR_ORE_OVERWORLD_ITEM = fromBlock(STELLAR_ORE_OVERWORLD);
	public static final RegistryObject<Block> STELLAR_ORE_NETHER = BLOCKS.register("stellar_ore_nether",() -> new Block(ORE_PROPERTIES));
	public static final RegistryObject<Item> STELLAR_ORE_NETHER_ITEM = fromBlock(STELLAR_ORE_NETHER);
	public static final RegistryObject<Block> STELLAR_ORE_END = BLOCKS.register("stellar_ore_end",() -> new Block(ORE_PROPERTIES));
	public static final RegistryObject<Item> STELLAR_ORE_END_ITEM = fromBlock(STELLAR_ORE_END);
	public static final RegistryObject<Block> STELLAR_ORE_DEEPSLATE = BLOCKS.register("stellar_ore_deepslate",() -> new Block(ORE_PROPERTIES));
	public static final RegistryObject<Item> STELLAR_ORE_DEEPSLATE_ITEM = fromBlock(STELLAR_ORE_DEEPSLATE);
	
/*
 * 		MACHINE BLOCKS	
 */

	public static final RegistryObject<Block> MACHINE_BODY = BLOCKS.register("machine_body",() -> new Block(SIMPLE_BLOCK));
	public static final RegistryObject<Item> MACHINE_BODY_ITEM = fromBlock(MACHINE_BODY);

/*
 * 		STELLAR
 */
	public static final RegistryObject<Item> INF_RAW_STELLAR = ITEMS.register("inf_raw_stellar", () -> new Item(ITEM_PROPERTIES));
	public static final RegistryObject<Item> AQUEOUS_STELLAR_BUCKET = ITEMS.register("aqueous_stellar_bucket", () -> new AqueousStellarItem());
	
/*
 * 		CRAFT COMPONENTS
 */
	public static final RegistryObject<Item> FACETED_ALLUMINUM_PANEL = ITEMS.register("faceted_alluminum_panel", () -> new Item(ITEM_PROPERTIES));
	public static final RegistryObject<Item> FACETED_ALLUMINUM_PART = ITEMS.register("faceted_alluminum_part", () -> new Item(ITEM_PROPERTIES));
	public static final RegistryObject<Item> FLUID_OUTLET = ITEMS.register("fluid_outlet", () -> new Item(ITEM_PROPERTIES));
	public static final RegistryObject<Item> FRACTURIZER = ITEMS.register("fracturizer", () -> new Item(ITEM_PROPERTIES));
	public static final RegistryObject<Item> INF_STELLAR_CORE = ITEMS.register("inf_stellar_core", () -> new Item(ITEM_PROPERTIES));
	public static final RegistryObject<Item> INF_STELLAR_PART = ITEMS.register("inf_stellar_part", () -> new Item(ITEM_PROPERTIES));
	public static final RegistryObject<Item> INTERFACE_PANEL = ITEMS.register("interface_panel", () -> new Item(ITEM_PROPERTIES));
	
/*
 * 		BLOCK TAGS
 */
	public static final TagKey<Block> EXOTERRA_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(ExoTerra.MODID, "exoterra_blocks"));
	public static final TagKey<Block> STELLAR_ORE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(ExoTerra.MODID, "stellar_ore"));
/*
 * 		ITEM TAGS
 */
	public static final TagKey<Item> EXOTERRA_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(ExoTerra.MODID, "exoterra_items"));
	public static final TagKey<Item> STELLAR_ORE_ITEM = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(ExoTerra.MODID, "stellar_ore"));
	public static final TagKey<Item> STELLAR_REFINED = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(ExoTerra.MODID, "stellar_refined"));	
	public static final TagKey<Item> COMPONENTS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(ExoTerra.MODID, "components"));	
/*
 * 		FLUID TAGS
 */
	public static final TagKey<Fluid> STELLAR_AQUEOUS = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(ExoTerra.MODID, "stellar_aqueous"));
	
	// Convenience function: Take a RegistryObject<Block> and make a corresponding RegistryObject<Item> from it
    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }
}
