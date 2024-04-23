package com.mmodding.env.driven.assets.mixin.client;

import com.mmodding.env.driven.assets.client.duck.BakedModelDuckInterface;
import com.mmodding.env.json.api.EnvJson;
import net.minecraft.client.render.model.MultipartBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MultipartBakedModel.class)
public class MultipartBakedModelMixin implements BakedModelDuckInterface {

	@Unique
	private EnvJson envJson = null;

	@Override
	public EnvJson env_driven_assets$getEnvJson() {
		return this.envJson;
	}

	@Override
	public void env_driven_assets$setEnvJson(EnvJson envJson) {
		this.envJson = envJson;
	}
}
