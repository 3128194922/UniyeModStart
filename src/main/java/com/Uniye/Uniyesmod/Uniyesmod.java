package com.Uniye.Uniyesmod;

import com.Uniye.Uniyesmod.Utils.ModAttributes;
import com.Uniye.Uniyesmod.Utils.Scheduler;
import com.Uniye.Uniyesmod.effects.ModEffects;
import com.Uniye.Uniyesmod.network.NetworkHandler;
import com.Uniye.Uniyesmod.tabs.ModCreativeModTabs;
import com.Uniye.Uniyesmod.Item.ModItems;
import com.Uniye.Uniyesmod.entity.ModEntities;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Uniyesmod.MODID)
public class Uniyesmod
{
    public static final String MODID = "uniyesmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Uniyesmod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        NetworkHandler.register();

        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModAttributes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Scheduler());

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
