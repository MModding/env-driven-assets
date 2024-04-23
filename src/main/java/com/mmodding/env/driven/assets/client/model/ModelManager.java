package com.mmodding.env.driven.assets.client.model;

import com.mmodding.env.driven.assets.client.EDAUtils;
import com.mmodding.env.driven.assets.mixin.client.BakedModelCacheKeyAccessor;
import net.fabricmc.fabric.api.renderer.v1.model.WrapperBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ModelManager {

	public final Map<ModelLoader.BakedModelCacheKey, BakedModel> settingsCache = new EDAUtils.ActionHashMap<>(i -> i, WrapperBakedModel::unwrap);

	private final Map<Identifier, BakedModel> identifierCache = new EDAUtils.ActionHashMap<>(i -> i, WrapperBakedModel::unwrap);

	public BakedModel changeModelWithSettings(Identifier reference, ModelBakeSettings settings) {
		ModelLoader.BakedModelCacheKey targetKey = BakedModelCacheKeyAccessor.env_driven_assets$init(reference, settings.getRotation(), settings.isUvLocked());
		BakedModel model = this.settingsCache.get(targetKey);
		if (model != null) {
			return model;
		}
		else {
			throw new IllegalArgumentException("Model " + reference + " has not been baked");
		}
	}

	public BakedModel changeModelWithoutSettings(Identifier reference) {
		return this.identifierCache.get(reference);
	}

	public BakedModel modelFromState(BlockState source) {
		return MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(source);
	}

	public void appendModel(ModelLoader.BakedModelCacheKey key, BakedModel model) {
		this.settingsCache.put(key, model);
		this.identifierCache.put(key.id(), model);
	}
}
