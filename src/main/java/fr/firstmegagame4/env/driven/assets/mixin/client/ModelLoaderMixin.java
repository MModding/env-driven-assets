package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fr.firstmegagame4.env.driven.assets.client.model.ModelManager;
import fr.firstmegagame4.env.driven.assets.client.duck.ModelLoaderDuckInterface;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin implements ModelLoaderDuckInterface {

	@Unique
	private final ModelManager manager = new ModelManager();

	@Override
	public ModelManager env_driven_assets$getModelManager() {
		return this.manager;
	}

	@Mixin(ModelLoader.BakerImpl.class)
	public static class BakerImplMixin {

		@Final
		@Shadow
		ModelLoader field_40571;

		@Inject(method = "bake", at = @At("RETURN"))
		private void hookToCache(Identifier id, ModelBakeSettings settings, CallbackInfoReturnable<BakedModel> cir, @Local(ordinal = 0) ModelLoader.BakedModelCacheKey bakedModelCacheKey) {
			((ModelLoaderDuckInterface) this.field_40571).env_driven_assets$getModelManager().appendModel(id, bakedModelCacheKey, cir.getReturnValue());
		}
	}
}
