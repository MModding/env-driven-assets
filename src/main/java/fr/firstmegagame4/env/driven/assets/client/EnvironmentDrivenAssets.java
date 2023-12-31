package fr.firstmegagame4.env.driven.assets.client;

import fr.firstmegagame4.env.driven.assets.client.model.plugin.EDAModelLoadingPlugin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EnvironmentDrivenAssets implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EDAUtils.LOGGER.info("Loading Environment Driven Assets");
		ModelLoadingPlugin.register(new EDAModelLoadingPlugin());
		FabricLoader.getInstance().getModContainer("env_driven_assets").map(
			container -> ResourceManagerHelper.registerBuiltinResourcePack(
				EnvironmentDrivenAssets.createId("env_driven_new_default"),
				container,
				Text.translatable("resourcePack.env_driven_assets.env_driven_new_default.name"),
				ResourcePackActivationType.DEFAULT_ENABLED
			)
		);
	}

	public static String id() {
		return "env_driven_assets";
	}

	public static Identifier createId(String path) {
		return new Identifier(EnvironmentDrivenAssets.id(), path);
	}
}
