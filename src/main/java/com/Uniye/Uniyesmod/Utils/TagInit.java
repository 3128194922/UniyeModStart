package com.Uniye.Uniyesmod.Utils;

import com.Uniye.Uniyesmod.Uniyesmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TagInit {
    public static final TagKey<Block> MASTERKEY_TAG = TagKey.create(
            Registries.BLOCK,
            new ResourceLocation(Uniyesmod.MODID, "master_key")
    );
}
