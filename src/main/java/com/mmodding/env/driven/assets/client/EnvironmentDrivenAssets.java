package com.mmodding.env.driven.assets.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mmodding.env.driven.assets.client.model.plugin.EDAModelLoadingPlugin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
		ClientCommandRegistrationCallback.EVENT.register(EnvironmentDrivenAssets::registerBakingOutputProducerCommand);
	}

	private static void registerBakingOutputProducerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registries) {
		dispatcher.register(ClientCommandManager.literal("produce-baking-output").executes(EnvironmentDrivenAssets::produceBakingOutput));
	}

	public static int produceBakingOutput(CommandContext<FabricClientCommandSource> source) {
		try {
			FileWriter writer = new FileWriter(new File(
				FabricLoader.getInstance().getGameDir().toFile(),
				new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) + ".eda-output.txt"
			));
			MinecraftClient client = MinecraftClient.getInstance();
			if (client == null) return 0;
			client.getBakedModelManager().getModelManager().settingsCache.forEach(
				(key, model) -> {
					try {
						writer.write(
							"BakedModelCacheKey: {"
								+ "Identifier: " + key.id().toString() + "; "
								+ "AffineTransformation: " + key.transformation() + "; "
								+ "IsUvLocked: " + key.isUvLocked() + ";"
								+ "}\n"
						);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			);
			writer.close();
			source.getSource().sendFeedback(Text.of(".eda-output.txt file got written"));
			return 1;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String id() {
		return "env_driven_assets";
	}

	public static Identifier createId(String path) {
		return new Identifier(EnvironmentDrivenAssets.id(), path);
	}
}
