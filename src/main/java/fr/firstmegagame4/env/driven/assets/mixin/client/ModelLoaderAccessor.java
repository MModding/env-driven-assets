package fr.firstmegagame4.env.driven.assets.mixin.client;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ModelLoader.class)
public interface ModelLoaderAccessor {

	@Accessor("bakedModelCache")
	Map<ModelLoader.BakedModelCacheKey, BakedModel> env_driven_assets$getBakedModelCache();
}
