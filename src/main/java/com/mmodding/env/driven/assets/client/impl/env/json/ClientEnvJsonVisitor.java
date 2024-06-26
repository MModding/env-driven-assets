package com.mmodding.env.driven.assets.client.impl.env.json;

import com.mmodding.env.driven.assets.client.EDAUtils;
import com.mmodding.env.driven.assets.client.EDAEnvJsonVisitors;
import com.mmodding.env.json.api.EnvJsonVisitor;
import com.mmodding.env.json.api.rule.SkyEnvJsonRule;
import com.mmodding.env.json.api.rule.VoidEnvJsonRule;
import com.mmodding.env.json.api.rule.WaterEnvJsonRule;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * @see EDAEnvJsonVisitors
 */
@ApiStatus.Internal
public class ClientEnvJsonVisitor implements EnvJsonVisitor {

	private final MinecraftClient client;

	public ClientEnvJsonVisitor(MinecraftClient client) {
		this.client = client;
	}

	@Override
	public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
		return this.player().isPresent() && EDAUtils.compareKeys(this.player().get().clientWorld.getRegistryKey(), dimensionKey);
	}

	@Override
	public boolean applyDimensionTag(TagKey<World> dimensionTag) {
		if (this.player().isPresent()) {
			ClientWorld world = this.player().get().clientWorld;
			return EDAUtils.worldIsIn(world, dimensionTag);
		}
		else {
			return false;
		}
	}

	@Override
	public boolean applyBiomeKey(RegistryKey<Biome> biomeKey) {
		return this.player().isPresent() && this.player().get().clientWorld.getBiome(this.player().get().getBlockPos()).matchesKey(biomeKey);
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
		if (this.player().isPresent()) {
			if (submerged) {
				return EDAUtils.lookupSubmerged(this.player().get().clientWorld, this.player().get().getBlockPos(), this.player().get().clientWorld::getBlockState);
			}
			else {
				return !EDAUtils.lookupSubmerged(this.player().get().clientWorld, this.player().get().getBlockPos(), this.player().get().clientWorld::getBlockState);
			}
		}
		else {
			return false;
		}
	}

	@Override
	public boolean applySky(SkyEnvJsonRule.Localization localization) {
		return this.player().isPresent() && switch (localization) {
			case BELOW -> this.player().get().getBlockY() < this.player().get().clientWorld.getTopY() - 1;
			case AT -> this.player().get().getBlockY() == this.player().get().clientWorld.getTopY() - 1;
			case ABOVE -> this.player().get().getBlockY() > this.player().get().clientWorld.getTopY() - 1;
		};
	}

	@Override
	public boolean applyWater(WaterEnvJsonRule.Localization localization) {
		return this.player().isPresent() && switch (localization) {
			case BELOW -> this.player().get().getBlockY() < this.player().get().clientWorld.getSeaLevel() - 1;
			case AT -> this.player().get().getBlockY() == this.player().get().clientWorld.getSeaLevel() - 1;
			case ABOVE -> this.player().get().getBlockY() > this.player().get().clientWorld.getSeaLevel() - 1;
		};
	}

	@Override
	public boolean applyVoid(VoidEnvJsonRule.Localization localization) {
		return this.player().isPresent() && switch (localization) {
			case BELOW -> this.player().get().getBlockY() < this.player().get().clientWorld.getBottomY();
			case AT -> this.player().get().getBlockY() == this.player().get().clientWorld.getBottomY();
			case ABOVE -> this.player().get().getBlockY() > this.player().get().clientWorld.getBottomY();
		};
	}

	private Optional<ClientPlayerEntity> player() {
		return Optional.ofNullable(this.client.player);
	}
}
