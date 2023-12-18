package fr.firstmegagame4.env.driven.assets.mixin.client;

import fr.firstmegagame4.env.json.api.EnvJsonVisitor;
import fr.firstmegagame4.env.json.api.rule.SkyEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.VoidEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.WaterEnvJsonRule;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

public class ClientPlayerVisitor implements EnvJsonVisitor {

	@Override
	public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
		return this.player().isPresent() && this.player().get().clientWorld.getRegistryKey() == dimensionKey;
	}

	@Override
	public boolean applyDimensionTag(TagKey<World> dimensionTag) {
		return false;
	}

	@Override
	public boolean applyBiomeKey(RegistryKey<Biome> biomeKey) {
		return this.player().isPresent() && this.player().get().clientWorld.getBiome(this.player().get().getBlockPos()) == biomeKey;
	}

	@Override
	public boolean applyBiomeTag(TagKey<Biome> biomeTag) {
		return this.player().isPresent() && this.player().get().clientWorld.getBiome(this.player().get().getBlockPos()).isIn(biomeTag);
	}

	@Override
	public boolean applyXCoord(Int2BooleanFunction operation) {
		return this.player().isPresent() && operation.get(this.player().get().getBlockX());
	}

	@Override
	public boolean applyYCoord(Int2BooleanFunction operation) {
		return this.player().isPresent() && operation.get(this.player().get().getBlockY());
	}

	@Override
	public boolean applyZCoord(Int2BooleanFunction operation) {
		return this.player().isPresent() && operation.get(this.player().get().getBlockZ());
	}

	@Override
	public boolean applySubmerged(boolean submerged) {
		return false;
	}

	@Override
	public boolean applySky(SkyEnvJsonRule.Localization localization) {
		return false;
	}

	@Override
	public boolean applyWater(WaterEnvJsonRule.Localization localization) {
		return false;
	}

	@Override
	public boolean applyVoid(VoidEnvJsonRule.Localization localization) {
		return false;
	}

	private Optional<MinecraftClient> client() {
		return Optional.ofNullable(MinecraftClient.getInstance());
	}

	private Optional<ClientPlayerEntity> player() {
		if (this.client().isPresent()) {
			return Optional.ofNullable(this.client().get().player);
		}
		else {
			return Optional.empty();
		}
	}
}
