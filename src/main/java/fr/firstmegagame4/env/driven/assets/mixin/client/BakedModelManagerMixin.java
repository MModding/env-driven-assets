package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelManagerDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.duck.JsonUnbakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.duck.ModelLoaderDuckInterface;
import fr.firstmegagame4.env.json.api.resource.ExtendedResource;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BakedModelManager.class)
public class BakedModelManagerMixin implements BakedModelManagerDuckInterface {

	@Unique
	private ModelLoader modelLoader = null;

	@WrapOperation(method = "method_45898", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;of(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;"))
	private static <F, S> Pair<F, S> mutateModelValue(F first, S second, Operation<Pair<F, S>> original, @Local Map.Entry<Identifier, Resource> entry) {
		ExtendedResource resource = ExtendedResource.of(entry.getValue());
		try {
			if (resource.getEnvJson() != null) EDAUtils.LOGGER.info("env.json file detected for model " + entry.getKey());
			((JsonUnbakedModelDuckInterface) second).env_driven_assets$setEnvJson(resource.getEnvJson());
		}
		catch (Exception exception) {
			EDAUtils.LOGGER.error("Failed to load env.json file for model {}", entry.getKey(), exception);
		}
		// noinspection MixinExtrasOperationParameters
		return original.call(first, second);
	}

	@Inject(method = "upload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelLoader;getBakedModelMap()Ljava/util/Map;"))
	private void hookModelLoader(BakedModelManager.BakingResult bakingResult, Profiler profiler, CallbackInfo ci) {
		this.modelLoader = bakingResult.modelLoader();
		((ModelLoaderDuckInterface) this.modelLoader).env_driven_assets$getModelManager().reloadRevertor(this.modelLoader);
	}

	@Override
	public BakedModel env_driven_assets$convert(Identifier reference) {
		return ((ModelLoaderDuckInterface) this.modelLoader).env_driven_assets$getModelManager().convert(this.modelLoader, reference);
	}

	@Override
	public Identifier env_driven_assets$revert(BakedModel source) {
		return ((ModelLoaderDuckInterface) this.modelLoader).env_driven_assets$getModelManager().revert(source);
	}

	@Override
	public Identifier env_driven_assets$revert(BlockState source) {
		return ((ModelLoaderDuckInterface) this.modelLoader).env_driven_assets$getModelManager().revert(source);
	}
}
