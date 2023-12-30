package fr.firstmegagame4.env.driven.assets.client.model.plugin;

import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.duck.JsonUnbakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.model.EDABakedModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.render.model.BakedModel;
import org.jetbrains.annotations.Nullable;

public class EDAModelLoadingPlugin implements ModelLoadingPlugin {

	private static BakedModel modifyModelAfterBake(@Nullable BakedModel model, ModelModifier.AfterBake.Context context) {
		if (model != null) {
			if (context.sourceModel() instanceof JsonUnbakedModelDuckInterface jum && model instanceof BakedModelDuckInterface ducked) {
				ducked.env_driven_assets$setEnvJson(jum.env_driven_assets$getEnvJson());
				return new EDABakedModel(context.loader(), model);
			}
		}
		return model;
	}

	@Override
	public void onInitializeModelLoader(Context pluginContext) {
		pluginContext.modifyModelAfterBake().register(EDAModelLoadingPlugin::modifyModelAfterBake);
	}
}
