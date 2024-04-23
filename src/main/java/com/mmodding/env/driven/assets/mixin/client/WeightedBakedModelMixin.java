package com.mmodding.env.driven.assets.mixin.client;

import com.mmodding.env.driven.assets.client.duck.BakedModelDuckInterface;
import com.mmodding.env.json.api.EnvJson;
import net.fabricmc.fabric.api.renderer.v1.model.WrapperBakedModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.WeightedBakedModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WeightedBakedModel.class)
public class WeightedBakedModelMixin implements BakedModelDuckInterface {

	@Shadow
	@Final
	private BakedModel defaultModel;

	@Override
	public EnvJson env_driven_assets$getEnvJson() {
		return ((BakedModelDuckInterface) WrapperBakedModel.unwrap(this.defaultModel)).env_driven_assets$getEnvJson();
	}

	@Override
	public void env_driven_assets$setEnvJson(EnvJson envJson) {
		((BakedModelDuckInterface) WrapperBakedModel.unwrap(this.defaultModel)).env_driven_assets$setEnvJson(envJson);
	}
}
