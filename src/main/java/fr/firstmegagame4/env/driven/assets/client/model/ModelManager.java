package fr.firstmegagame4.env.driven.assets.client.model;

import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ModelManager {

	private final Map<Identifier, ModelLoader.BakedModelCacheKey> convertor = new Object2ObjectOpenHashMap<>();
	private final Map<ModelLoader.BakedModelCacheKey, BakedModel> cache = new Object2ObjectOpenHashMap<>();
	private final Map<BakedModel, ModelLoader.BakedModelCacheKey> revertor = new Object2ObjectOpenHashMap<>();

	public BakedModel convert(Identifier reference) {
		return this.cache.get(this.convertor.get(reference));
	}

	public Identifier revert(BakedModel source) {
		return this.revertor.get(source).id();
	}

	public Identifier revert(BlockState source) {
		return this.revert(MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(source));
	}

	public void appendModel(Identifier identifier, ModelLoader.BakedModelCacheKey key, BakedModel model) {
		this.convertor.put(identifier, key);
		this.cache.put(key, model);
	}

	public void reloadRevertor() {
		this.revertor.clear();
		this.revertor.putAll(EDAUtils.reverseMap(this.cache, Object2ObjectOpenHashMap::new));
	}
}
