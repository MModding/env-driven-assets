package com.mmodding.env.driven.assets.mixin.client;

import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpriteBillboardParticle.class)
public interface SpriteBillboardParticleAccessor {

	@Invoker("setSprite")
	void env_driven_assets$setSprite(Sprite sprite);
}
