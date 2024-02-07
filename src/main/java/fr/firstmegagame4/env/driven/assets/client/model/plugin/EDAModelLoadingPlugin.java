package fr.firstmegagame4.env.driven.assets.client.model.plugin;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.render.model.BakedModel;
import org.jetbrains.annotations.Nullable;

public class EDAModelLoadingPlugin implements ModelLoadingPlugin {

	private static BakedModel modifyModelAfterBake(@Nullable BakedModel model, ModelModifier.AfterBake.Context context) {
		return model;
	}

	@Override
	public void onInitializeModelLoader(Context pluginContext) {
		pluginContext.modifyModelAfterBake().register(EDAModelLoadingPlugin::modifyModelAfterBake);
	}
}
