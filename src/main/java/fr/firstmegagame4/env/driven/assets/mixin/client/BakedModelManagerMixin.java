package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.client.blockstate.BlockStateManager;
import fr.firstmegagame4.env.driven.assets.client.duck.JsonObjectDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.duck.JsonUnbakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.injected.ManagerContainer;
import fr.firstmegagame4.env.driven.assets.client.model.ModelManager;
import fr.firstmegagame4.env.json.api.resource.ExtendedResource;
import fr.firstmegagame4.env.json.api.resource.ExtendedResourceReader;
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

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

@Mixin(BakedModelManager.class)
public class BakedModelManagerMixin implements ManagerContainer {

	@Unique
	private ModelLoader modelLoader = null;

	// This mixin works, MCDev is just doing weird checks
	@SuppressWarnings("UnresolvedMixinReference")
	@WrapOperation(method = "method_45898", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;of(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;"))
	private static <F, S> Pair<F, S> mutateModelValue(F first, S second, Operation<Pair<F, S>> original, @Local(argsOnly = true) Map.Entry<Identifier, Resource> entry) {
		ExtendedResource resource = ExtendedResource.of(entry.getValue());
		try {
			if (resource.getEnvJson() != null) {
				EDAUtils.LOGGER.info("env.json file detected for model " + entry.getKey());
				((JsonUnbakedModelDuckInterface) second).env_driven_assets$setEnvJson(resource.getEnvJson());
			}
		}
		catch (Exception exception) {
			EDAUtils.LOGGER.error("Failed to load env.json file for model {}", entry.getKey(), exception);
		}
		// noinspection MixinExtrasOperationParameters
		return original.call(first, second);
	}

	@SuppressWarnings("DataFlowIssue")
	@WrapOperation(method = "method_45890", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/JsonHelper;deserialize(Ljava/io/Reader;)Lcom/google/gson/JsonObject;"))
	private static JsonObject hideEnvJson(Reader reader, Operation<JsonObject> original) {
		JsonObject jsonObject = original.call(reader);
		ExtendedResourceReader extended = (ExtendedResourceReader) reader;
		// May need some logs here
		try {
			JsonObjectDuckInterface ducked = (JsonObjectDuckInterface) (Object) jsonObject;
			ducked.env_driven_assets$setEnvJson(extended.getExtendedResource().getEnvJson());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return jsonObject;
	}

	@Inject(method = "upload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelLoader;getBakedModelMap()Ljava/util/Map;"))
	private void hookModelLoader(BakedModelManager.BakingResult bakingResult, Profiler profiler, CallbackInfo ci) {
		this.modelLoader = bakingResult.modelLoader();
	}

	@Override
	@SuppressWarnings("AddedMixinMembersNamePattern")
	public BlockStateManager getBlockStateManager() {
		return this.modelLoader.getBlockStateManager();
	}

	@Override
	@SuppressWarnings("AddedMixinMembersNamePattern")
	public ModelManager getModelManager() {
		return this.modelLoader.getModelManager();
	}
}
