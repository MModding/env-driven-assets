package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.client.duck.JsonUnbakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.duck.ModelLoaderDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.model.ModelManager;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Function;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin implements ModelLoaderDuckInterface {

	@Unique
	private final ModelManager manager = new ModelManager();

	@Shadow
	@Final
	private Map<Identifier, UnbakedModel> unbakedModels;

	@Shadow
	@Final
	private Map<Identifier, UnbakedModel> modelsToBake;

	@Shadow
	public abstract UnbakedModel getOrLoadModel(Identifier id);

	@Override
	public ModelManager env_driven_assets$getModelManager() {
		return this.manager;
	}

	@Inject(method = "addModel", at = @At("TAIL"))
	private void addEnvJsonModels(ModelIdentifier modelId, CallbackInfo ci, @Local UnbakedModel unbakedModel) {
		EDAUtils.retrieveJsonUnbakedModelDuckInterfaces(unbakedModel, this::getOrLoadModel).forEach(jum -> {
			if (jum.env_driven_assets$getEnvJson() != null) {
				jum.env_driven_assets$getEnvJson().members().forEach(member -> this.addEnvJsonModel(member.result()));
			}
		});
	}

	@Unique
	private void addEnvJsonModel(Identifier identifier) {
		UnbakedModel unbakedModel = this.getOrLoadModel(identifier);
		this.unbakedModels.put(identifier, unbakedModel);
		this.modelsToBake.put(identifier, unbakedModel);
		if (unbakedModel instanceof JsonUnbakedModel jsonUnbakedModel) {
			for (Identifier dependency : jsonUnbakedModel.getModelDependencies()) {
				if (!this.unbakedModels.containsKey(dependency)) {
					this.addEnvJsonModel(identifier);
				}
			}
		}
	}

	@Mixin(ModelLoader.BakerImpl.class)
	public static abstract class BakerImplMixin {

		@Final
		@Shadow
		ModelLoader field_40571;

		@Shadow
		@Final
		private Function<SpriteIdentifier, Sprite> textureGetter;

		@ModifyExpressionValue(method = "bake", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ItemModelGenerator;create(Ljava/util/function/Function;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;"))
		private JsonUnbakedModel passToItem(JsonUnbakedModel original, @Local UnbakedModel unbakedModel) {
			EDAUtils.retrieveJsonUnbakedModelDuckInterfaces(unbakedModel, this.field_40571::getOrLoadModel).forEach(ducked -> ((JsonUnbakedModelDuckInterface) original).env_driven_assets$setEnvJson(ducked.env_driven_assets$getEnvJson()));
			return original;
		}

		@Inject(method = "bake", at = @At("RETURN"))
		private void hookToCache(Identifier id, ModelBakeSettings settings, CallbackInfoReturnable<BakedModel> cir, @Local ModelLoader.BakedModelCacheKey bakedModelCacheKey) {
			((ModelLoaderDuckInterface) this.field_40571).env_driven_assets$getModelManager().appendModel(bakedModelCacheKey, cir.getReturnValue());
		}

		@Inject(method = "bake", at = @At("TAIL"))
		private void bakeEnvJsonModels(Identifier id, ModelBakeSettings settings, CallbackInfoReturnable<BakedModel> cir, @Local UnbakedModel unbakedModel) {
			if (unbakedModel instanceof JsonUnbakedModelDuckInterface jum && jum.env_driven_assets$getEnvJson() != null) {
				jum.env_driven_assets$getEnvJson().members().forEach(member -> {
					UnbakedModel envJsonModel = this.field_40571.getOrLoadModel(member.result());
					BakedModel bakedModel = envJsonModel.bake((Baker) this, this.textureGetter, settings, member.result());
					((ModelLoaderDuckInterface) this.field_40571).env_driven_assets$getModelManager().appendModel(
						BakedModelCacheKeyAccessor.env_driven_assets$init(member.result(), settings.getRotation(), settings.isUvLocked()),
						bakedModel
					);
					System.out.println("SourceModel: " + id + " TargetModel: " + member.result() + " Rotation: " + settings.getRotation() + " UVLocked: " + settings.isUvLocked());
				});
			}
		}
	}
}
