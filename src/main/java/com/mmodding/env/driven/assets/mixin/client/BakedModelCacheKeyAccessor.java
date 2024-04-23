package com.mmodding.env.driven.assets.mixin.client;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelLoader.BakedModelCacheKey.class)
public interface BakedModelCacheKeyAccessor {

	@Invoker("<init>")
	static ModelLoader.BakedModelCacheKey env_driven_assets$init(Identifier id, AffineTransformation transformation, boolean isUvLocked) {
		throw new AssertionError();
	}
}
