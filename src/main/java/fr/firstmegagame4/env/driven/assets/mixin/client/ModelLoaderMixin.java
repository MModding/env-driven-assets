package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fr.firstmegagame4.env.driven.assets.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.duck.JsonUnbakedModelDuckInterface;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {

	@Shadow
	public abstract UnbakedModel getOrLoadModel(Identifier id);

	@Inject(method = "method_45877", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private void applyEnvJson(BiFunction<Identifier, SpriteIdentifier, Sprite> biFunction, Identifier modelId, CallbackInfo ci, @Local BakedModel bakedModel) {
		if (this.getOrLoadModel(modelId) instanceof JsonUnbakedModelDuckInterface model) {
			((BakedModelDuckInterface) bakedModel).env_driven_assets$setEnvJson(model.env_driven_assets$getEnvJson());
		}
	}
}
