package com.Uniye.Uniyesmod.Item;

import com.Uniye.Uniyesmod.Uniyesmod;
import com.Uniye.Uniyesmod.Utils.TagInit;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Uniyesmod.MODID);

    public static final RegistryObject<Item> RUBBER = ITEMS.register("rubber", ()-> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SLIME_ARROW = ITEMS.register("slime_arrow",
            () -> new SlimeArrowItem(new Item.Properties())
    );

    public static final RegistryObject<Item> DEMOCRACY_HELMET = ITEMS.register(
            "democracy_helmet",
            () -> new ArmorItem(ModArmorMaterials.DEMOCRACY, ArmorItem.Type.HELMET, new Item.Properties())
    );

    public static final RegistryObject<Item> DEMOCRACY_CHESTPLATE = ITEMS.register(
            "democracy_chestplate",
            () -> new ArmorItem(ModArmorMaterials.DEMOCRACY, ArmorItem.Type.CHESTPLATE, new Item.Properties())
    );

    public static final RegistryObject<Item> DEMOCRACY_LEGGINGS = ITEMS.register(
            "democracy_leggings",
            () -> new ArmorItem(ModArmorMaterials.DEMOCRACY, ArmorItem.Type.LEGGINGS, new Item.Properties())
    );

    public static final RegistryObject<Item> DEMOCRACY_BOOTS = ITEMS.register(
            "democracy_boots",
            () -> new ArmorItem(ModArmorMaterials.DEMOCRACY, ArmorItem.Type.BOOTS, new Item.Properties())
    );
    public static final RegistryObject<Item> SEEKING_ARROW = ITEMS.register(
            "seeking_arrow",
            () -> new SeekingArrowItem(new Item.Properties())
    );
    public static final RegistryObject<Item> AIRBURST_ARROW = ITEMS.register(
            "airburst_arrow",
            () -> new AirburstArrowItem(new Item.Properties())
    );
    public static final RegistryObject<Item> EXPLODING_ARROW = ITEMS.register(
            "exploding_arrow",
            () -> new ExplodingArrowItem(new Item.Properties())
    );
    public static final RegistryObject<Item> FINAL_EXPLODING_ARROW = ITEMS.register(
            "final_exploding_arrow",
            () -> new FinalExplodingArrowItem(new Item.Properties())
    );
    public static final RegistryObject<Item> WHICH_ARMOR_HELMET = ITEMS.register(
            "which_armor_helmet",
            () -> new WhichArmorItem(ModArmorMaterials.WHICH, ArmorItem.Type.HELMET, new Item.Properties())
    );
    public static final RegistryObject<Item> MASTER_KEY = ITEMS.register(
            "master_key",
            () -> new MasterKeyItem(5, -3.0f,Tiers.NETHERITE, TagInit.MASTERKEY_TAG, new Item.Properties())
    );
    public static void register(IEventBus bus)
    {
        ITEMS.register(bus);
    }
    public static final RegistryObject<Item> GRIEFER_SPEAR = ITEMS.register(
            "griefer_spear",
            () -> new GrieferSpearItem(Tiers.IRON, 4.0F, -2.8F, new Item.Properties()));
}
