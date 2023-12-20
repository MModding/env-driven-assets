package fr.firstmegagame4.env.driven.assets.impl.env.json;

import fr.firstmegagame4.env.json.api.EnvJsonVisitor;
import fr.firstmegagame4.env.json.api.rule.SkyEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.VoidEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.WaterEnvJsonRule;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class EntityEnvJsonVisitor implements EnvJsonVisitor {

	private final Entity entity;

	public EntityEnvJsonVisitor(Entity entity) {
		this.entity = entity;
	}

	@Override
	public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
		return this.entity.getWorld().getRegistryKey() == dimensionKey;
	}

	@Override
	public boolean applyDimensionTag(TagKey<World> dimensionTag) {
		return false;
	}

	@Override
	public boolean applyBiomeKey(RegistryKey<Biome> biomeKey) {
		return this.entity.getWorld().getBiome(this.entity.getBlockPos()).matchesKey(biomeKey);
	}

	@Override
	public boolean applyBiomeTag(TagKey<Biome> biomeTag) {
		return this.entity.getWorld().getBiome(this.entity.getBlockPos()).isIn(biomeTag);
	}

	@Override
	public boolean applyXCoord(Int2BooleanFunction operation) {
		return operation.get(this.entity.getBlockX());
	}

	@Override
	public boolean applyYCoord(Int2BooleanFunction operation) {
		return operation.get(this.entity.getBlockY());
	}

	@Override
	public boolean applyZCoord(Int2BooleanFunction operation) {
		return operation.get(this.entity.getBlockZ());
	}

	@Override
	public boolean applySubmerged(boolean submerged) {
		return submerged;
	}

	@Override
	public boolean applySky(SkyEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.entity.getBlockY() <= this.entity.getWorld().getTopY();
			case ABOVE -> this.entity.getBlockY() > this.entity.getWorld().getTopY();
		};
	}

	@Override
	public boolean applyWater(WaterEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.entity.getBlockY() <= this.entity.getWorld().getSeaLevel();
			case ABOVE -> this.entity.getBlockY() > this.entity.getWorld().getSeaLevel();
		};
	}

	@Override
	public boolean applyVoid(VoidEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.entity.getBlockY() <= this.entity.getWorld().getBottomY();
			case ABOVE -> this.entity.getBlockY() > this.entity.getWorld().getBottomY();
		};
	}
}
