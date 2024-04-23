package com.mmodding.env.driven.assets.mixin.client;

import com.google.gson.JsonObject;
import com.mmodding.env.driven.assets.client.duck.JsonObjectDuckInterface;
import com.mmodding.env.json.api.EnvJson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(JsonObject.class)
public class JsonObjectMixin implements JsonObjectDuckInterface {

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
