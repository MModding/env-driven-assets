package com.mmodding.env.driven.assets.client.impl.sodium;

import com.mmodding.env.driven.assets.client.EDAEnvJsonVisitors;
import com.mmodding.env.driven.assets.client.EDAUtils;
import com.mmodding.env.driven.assets.mixin.client.WorldSliceAccessor;
import com.mmodding.env.json.api.EnvJsonVisitor;
import com.mmodding.env.json.api.rule.SkyEnvJsonRule;
import com.mmodding.env.json.api.rule.VoidEnvJsonRule;
import com.mmodding.env.json.api.rule.WaterEnvJsonRule;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * @see EDAEnvJsonVisitors
 */
@SuppressWarnings("resource")
public class SodiumBlockEnvJsonVisitor implements EnvJsonVisitor {

	private final WorldSlice slice;
	private final BlockPos pos;

	public SodiumBlockEnvJsonVisitor(WorldSlice slice, BlockPos pos) {
		this.slice = slice;
		this.pos = pos;
	}

	@Override
	public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
		return EDAUtils.compareKeys(((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld().getRegistryKey(), dimensionKey);
	}

	@Override
	public boolean applyDimensionTag(TagKey<World> dimensionTag) {
		return EDAUtils.worldIsIn(((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld(), dimensionTag);
	}

	@Override
	public boolean applyBiomeKey(RegistryKey<Biome> biomeKey) {
		return this.slice.getBiomeFabric(this.pos).matchesKey(biomeKey);
	}

	@Override
	public boolean applyBiomeTag(TagKey<Biome> biomeTag) {
		return this.slice.getBiomeFabric(this.pos).isIn(biomeTag);
	}

	@Override
	public boolean applyXCoord(Int2BooleanFunction operation) {
		return operation.get(this.pos.getX());
	}

	@Override
	public boolean applyYCoord(Int2BooleanFunction operation) {
		return operation.get(this.pos.getY());
	}

	@Override
	public boolean applyZCoord(Int2BooleanFunction operation) {
		return operation.get(this.pos.getZ());
	}

	@Override
	public boolean applySubmerged(boolean submerged) {
		if (submerged) {
			return EDAUtils.lookupSubmerged(((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld(), this.pos, this.slice::getBlockState);
		}
		else {
			return !EDAUtils.lookupSubmerged(((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld(), this.pos, this.slice::getBlockState);
		}
	}

	@Override
	public boolean applySky(SkyEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.pos.getY() < this.slice.getTopY() - 1;
			case AT -> this.pos.getY() == this.slice.getTopY() - 1;
			case ABOVE -> this.pos.getY() > this.slice.getTopY() - 1;
		};
	}

	@Override
	public boolean applyWater(WaterEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.pos.getY() < ((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld().getSeaLevel() - 1;
			case AT -> this.pos.getY() == ((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld().getSeaLevel() - 1;
			case ABOVE -> this.pos.getY() > ((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld().getSeaLevel() - 1;
		};
	}

	@Override
	public boolean applyVoid(VoidEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.pos.getY() < this.slice.getBottomY();
			case AT -> this.pos.getY() == this.slice.getBottomY();
			case ABOVE -> this.pos.getY() > this.slice.getBottomY();
		};
	}
}
