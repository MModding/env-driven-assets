package fr.firstmegagame4.env.driven.assets.client.model;

import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.mixin.client.ModelLoaderAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ModelManager {

	private final Map<Identifier, ModelLoader.BakedModelCacheKey> convertor = new Object2ObjectOpenHashMap<>();
	private Map<BakedModel, ModelLoader.BakedModelCacheKey> revertor = new Object2ObjectOpenHashMap<>();

	public BakedModel convert(ModelLoader loader, Identifier reference) {
		return ((ModelLoaderAccessor) loader).env_driven_assets$getBakedModelCache().get(this.convertor.get(reference));
	}

	public Identifier revert(BakedModel source) {
		return this.revertor.get(source).id();
	}

	public Identifier revert(BlockState source) {
		return this.revert(MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(source));
	}

	public void appendModelIdentifier(Identifier identifier, ModelLoader.BakedModelCacheKey key) {
		this.convertor.put(identifier, key);
	}

	public void reloadRevertor(ModelLoader loader) {
		this.revertor = EDAUtils.reverseMap(((ModelLoaderAccessor) loader).env_driven_assets$getBakedModelCache(), Object2ObjectOpenHashMap::new);
	}
}
