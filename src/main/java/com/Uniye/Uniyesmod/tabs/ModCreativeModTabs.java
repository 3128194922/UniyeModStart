package com.Uniye.Uniyesmod.tabs;

import com.Uniye.Uniyesmod.Item.ModItems;
import com.Uniye.Uniyesmod.Uniyesmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Uniyesmod.MODID);

    public static final RegistryObject<CreativeModeTab> UNIYESMOD_TAB = CREATIVE_MODE_TABS.register("uniyesmod_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.RUBBER.get()))
                    .title(Component.translatable("creativetab.uniyesmod_tab"))
                    .displayItems((parameters, output) -> {
                        // 添加您的物品到创造标签页
                        output.accept(ModItems.RUBBER.get());
                        output.accept(ModItems.DEMOCRACY_HELMET.get());
                        output.accept(ModItems.DEMOCRACY_BOOTS.get());
                        output.accept(ModItems.DEMOCRACY_CHESTPLATE.get());
                        output.accept(ModItems.DEMOCRACY_LEGGINGS.get());
                        output.accept(ModItems.SLIME_ARROW.get());
                        output.accept(ModItems.SEEKING_ARROW.get());
                        output.accept(ModItems.AIRBURST_ARROW.get());
                        output.accept(ModItems.EXPLODING_ARROW.get());
                        output.accept(ModItems.FINAL_EXPLODING_ARROW.get());
                        output.accept(ModItems.WHICH_ARMOR_HELMET.get());
                    })
                    .build()
    );

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
