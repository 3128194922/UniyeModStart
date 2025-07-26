package com.Uniye.Uniyesmod.Item;

import com.Uniye.Uniyesmod.Uniyesmod;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
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


    public static void register(IEventBus bus)
    {
        ITEMS.register(bus);
    }
}
