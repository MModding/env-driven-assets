package fr.firstmegagame4.env.driven.assets.client;

import fr.firstmegagame4.env.driven.assets.client.model.plugin.EDAModelLoadingPlugin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class EnvironmentDrivenAssets implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EDAUtils.LOGGER.info("Loading Environment Driven Assets");
		ModelLoadingPlugin.register(new EDAModelLoadingPlugin());
	}
}
