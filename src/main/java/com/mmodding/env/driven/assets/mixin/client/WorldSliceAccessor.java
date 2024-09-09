package com.mmodding.env.driven.assets.mixin.client;

import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelSlice.class)
public interface WorldSliceAccessor {

	@Accessor("level")
	ClientWorld env_driven_assets$getWorld();
}
