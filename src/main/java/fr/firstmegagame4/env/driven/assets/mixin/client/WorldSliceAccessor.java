package fr.firstmegagame4.env.driven.assets.mixin.client;

import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldSlice.class)
public interface WorldSliceAccessor {

	@Accessor("world")
	ClientWorld env_driven_assets$getWorld();
}
