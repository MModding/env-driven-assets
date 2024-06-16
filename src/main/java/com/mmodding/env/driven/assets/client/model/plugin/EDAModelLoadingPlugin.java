package com.mmodding.env.driven.assets.client.model.plugin;

import com.mmodding.env.driven.assets.client.duck.BakedModelDuckInterface;
import com.mmodding.env.driven.assets.client.duck.JsonUnbakedModelDuckInterface;
import com.mmodding.env.driven.assets.client.model.EDABakedModel;
import com.mmodding.env.json.api.EnvJson;
import com.mmodding.env.json.api.EnvJsonMember;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.render.model.BakedModel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EDAModelLoadingPlugin implements ModelLoadingPlugin {

	public static final List<EnvJson> MODELS = new ArrayList<>();

	private static BakedModel modifyModelAfterBake(@Nullable BakedModel model, ModelModifier.AfterBake.Context context) {
		if (model != null && !model.isBuiltin() && context.sourceModel() instanceof JsonUnbakedModelDuckInterface jum && model instanceof BakedModelDuckInterface ducked) {
			ducked.env_driven_assets$setEnvJson(jum.env_driven_assets$getEnvJson());
			return new EDABakedModel(context.loader(), model, context.settings());
		}
		return model;
	}

	@Override
	public void onInitializeModelLoader(Context pluginContext) {
		pluginContext.addModels(EDAModelLoadingPlugin.MODELS.stream().flatMap(envJson -> envJson.members().stream().map(EnvJsonMember::result)).collect(Collectors.toSet()));
		pluginContext.modifyModelAfterBake().register(EDAModelLoadingPlugin::modifyModelAfterBake);
	}
}
