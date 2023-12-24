package fr.firstmegagame4.env.driven.assets;

import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EDACommons implements ModInitializer {

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("env_driven_assets");

	public static boolean worldIsIn(World entry, TagKey<World> tag) {
		return EDACommons.isIn(entry.getRegistryManager(), RegistryKeys.WORLD, tag, entry.getRegistryManager().get(RegistryKeys.WORLD).getEntry(entry));
	}

	public static <T> boolean isIn(DynamicRegistryManager manager, RegistryKey<? extends Registry<T>> registry, TagKey<T> tag, RegistryEntry<T> entry) {
		return manager.get(registry).getEntryList(tag).orElseThrow().contains(entry);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}
