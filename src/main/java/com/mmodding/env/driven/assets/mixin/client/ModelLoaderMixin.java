package com.mmodding.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mmodding.env.driven.assets.client.EDAUtils;
import com.mmodding.env.driven.assets.client.blockstate.BlockStateManager;
import com.mmodding.env.driven.assets.client.duck.JsonObjectDuckInterface;
import com.mmodding.env.driven.assets.client.duck.JsonUnbakedModelDuckInterface;
import com.mmodding.env.driven.assets.client.injected.ManagerContainer;
import com.mmodding.env.driven.assets.client.model.ModelManager;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin implements ManagerContainer {

	@Unique
	private final BlockStateManager blockStateManager = new BlockStateManager();

	@Unique
	private final ModelManager modelManager = new ModelManager();

	@Shadow
	@Final
	private Map<Identifier, UnbakedModel> unbakedModels;

	@Shadow
	@Final
	private Map<Identifier, UnbakedModel> modelsToBake;

	@Shadow
	public abstract UnbakedModel getOrLoadModel(Identifier id);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void computeStatesIntoManager(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<ModelLoader.SourceTrackedData>> blockStates, CallbackInfo ci) {
		Registries.BLOCK.streamEntries().forEach(ref -> {
			Identifier stateId = ModelLoader.BLOCK_STATES_FINDER.toResourcePath(ref.registryKey().getValue());
			blockStates.get(stateId).forEach(source -> {
				JsonObjectDuckInterface ducked = (JsonObjectDuckInterface) source.data();
				if (ducked.env_driven_assets$getEnvJson() != null) {
					this.blockStateManager.appendBlock(ref.value(), ducked.env_driven_assets$getEnvJson());
				}
			});
		});
	}

	@Inject(method = "addModel", at = @At("TAIL"))
	private void addEnvJsonModels(ModelIdentifier modelId, CallbackInfo ci, @Local UnbakedModel unbakedModel) {
		EDAUtils.retrieveJsonUnbakedModelDuckInterfaces(unbakedModel, this::getOrLoadModel).forEach(jum -> {
			if (jum.env_driven_assets$getEnvJson() != null) {
				jum.env_driven_assets$getEnvJson().members().forEach(member -> this.addEnvJsonModel(member.result()));
			}
		});
	}

	@Override
	@SuppressWarnings("AddedMixinMembersNamePattern")
	public BlockStateManager getBlockStateManager() {
		return this.blockStateManager;
	}

	@Override
	@SuppressWarnings("AddedMixinMembersNamePattern")
	public ModelManager getModelManager() {
		return this.modelManager;
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
			this.field_40571.getModelManager().appendModel(bakedModelCacheKey, cir.getReturnValue());
		}

		@Inject(method = "bake", at = @At("TAIL"))
		private void bakeEnvJsonModels(Identifier id, ModelBakeSettings settings, CallbackInfoReturnable<BakedModel> cir, @Local UnbakedModel unbakedModel) {
			if (unbakedModel instanceof JsonUnbakedModelDuckInterface jum && jum.env_driven_assets$getEnvJson() != null) {
				jum.env_driven_assets$getEnvJson().members().forEach(member -> {
					UnbakedModel envJsonModel = this.field_40571.getOrLoadModel(member.result());
					BakedModel bakedModel = envJsonModel.bake((Baker) this, this.textureGetter, settings, member.result());
					this.field_40571.getModelManager().appendModel(
						BakedModelCacheKeyAccessor.env_driven_assets$init(member.result(), settings.getRotation(), settings.isUvLocked()),
						bakedModel
					);
				});
			}
		}
	}
}
