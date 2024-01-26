package fr.firstmegagame4.env.driven.assets.client.model;

import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.mixin.client.BakedModelCacheKeyAccessor;
import net.fabricmc.fabric.api.renderer.v1.model.WrapperBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ModelManager {

	private final Map<ModelLoader.BakedModelCacheKey, BakedModel> cache = new EDAUtils.ActionHashMap<>(i -> i, WrapperBakedModel::unwrap);

	private final Map<BakedModel, ModelLoader.BakedModelCacheKey> reverted = new EDAUtils.ActionHashMap<>(WrapperBakedModel::unwrap, i -> i);

	public BakedModel changeModelAndKeepSettings(BakedModel source, Identifier reference) {
		ModelLoader.BakedModelCacheKey sourceKey = this.reverted.get(source);
		if (sourceKey == null) {
			throw new IllegalStateException("Could not change model to " + reference);
		}
		ModelLoader.BakedModelCacheKey targetKey = BakedModelCacheKeyAccessor.env_driven_assets$init(reference, sourceKey.transformation(), sourceKey.isUvLocked());
		BakedModel model = this.cache.get(targetKey);
		if (model != null) {
			return model;
		}
		else {
			throw new IllegalArgumentException("Model " + reference + " has not been baked");
		}
	}

	public Identifier idFromModel(BakedModel source) {
		return this.reverted.get(source).id();
	}

	public BakedModel modelFromState(BlockState source) {
		return MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(source);
	}

	public Identifier idFromState(BlockState source) {
		return this.idFromModel(this.modelFromState(source));
	}

	public void appendModel(ModelLoader.BakedModelCacheKey key, BakedModel model) {
		this.cache.put(key, model);
		this.reverted.put(model, key);
	}
}
