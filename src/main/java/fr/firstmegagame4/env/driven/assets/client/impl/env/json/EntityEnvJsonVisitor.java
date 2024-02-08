package fr.firstmegagame4.env.driven.assets.client.impl.env.json;

import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
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

/**
 * @see EDAEnvJsonVisitors
 */
@ApiStatus.Internal
public class EntityEnvJsonVisitor implements EnvJsonVisitor {

	private final Entity entity;

	public EntityEnvJsonVisitor(Entity entity) {
		this.entity = entity;
	}

	@Override
	public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
		return EDAUtils.compareKeys(this.entity.getWorld().getRegistryKey(), dimensionKey);
	}

	@Override
	public boolean applyDimensionTag(TagKey<World> dimensionTag) {
		return EDAUtils.worldIsIn(this.entity.getWorld(), dimensionTag);
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
		if (submerged) {
			return EDAUtils.lookupSubmerged(this.entity.getWorld(), this.entity.getBlockPos(), this.entity.getWorld()::getBlockState);
		}
		else {
			return !EDAUtils.lookupSubmerged(this.entity.getWorld(), this.entity.getBlockPos(), this.entity.getWorld()::getBlockState);
		}
	}

	@Override
	public boolean applySky(SkyEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.entity.getBlockY() < this.entity.getWorld().getTopY() - 1;
			case AT -> this.entity.getBlockY() == this.entity.getWorld().getTopY() - 1;
			case ABOVE -> this.entity.getBlockY() > this.entity.getWorld().getTopY() - 1;
		};
	}

	@Override
	public boolean applyWater(WaterEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.entity.getBlockY() < this.entity.getWorld().getSeaLevel() - 1;
			case AT -> this.entity.getBlockY() == this.entity.getWorld().getSeaLevel() - 1;
			case ABOVE -> this.entity.getBlockY() > this.entity.getWorld().getSeaLevel() - 1;
		};
	}

	@Override
	public boolean applyVoid(VoidEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.entity.getBlockY() < this.entity.getWorld().getBottomY();
			case AT -> this.entity.getBlockY() == this.entity.getWorld().getBottomY();
			case ABOVE -> this.entity.getBlockY() > this.entity.getWorld().getBottomY();
		};
	}
}
