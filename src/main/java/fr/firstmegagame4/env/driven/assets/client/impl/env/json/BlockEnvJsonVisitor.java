package fr.firstmegagame4.env.driven.assets.client.impl.env.json;

import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
import fr.firstmegagame4.env.json.api.EnvJsonVisitor;
import fr.firstmegagame4.env.json.api.rule.SkyEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.VoidEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.WaterEnvJsonRule;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

/**
 * @see EDAEnvJsonVisitors
 */
@ApiStatus.Internal
public class BlockEnvJsonVisitor implements EnvJsonVisitor {

	private final World world;
	private final BlockPos pos;

	public BlockEnvJsonVisitor(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	@Override
	public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
		return this.world.getRegistryKey() == dimensionKey;
	}

	@Override
	public boolean applyDimensionTag(TagKey<World> dimensionTag) {
		return EDAUtils.worldIsIn(this.world, dimensionTag);
	}

	@Override
	public boolean applyBiomeKey(RegistryKey<Biome> biomeKey) {
		return this.world.getBiome(this.pos).matchesKey(biomeKey);
	}

	@Override
	public boolean applyBiomeTag(TagKey<Biome> biomeTag) {
		return this.world.getBiome(this.pos).isIn(biomeTag);
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
			return EDAUtils.lookupSubmerged(this.world, this.pos, this.world::getBlockState);
		}
		else {
			return !EDAUtils.lookupSubmerged(this.world, this.pos, this.world::getBlockState);
		}
	}

	@Override
	public boolean applySky(SkyEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.pos.getY() < this.world.getTopY() - 1;
			case AT -> this.pos.getY() == this.world.getTopY() - 1;
			case ABOVE -> this.pos.getY() > this.world.getTopY() - 1;
		};
	}

	@Override
	public boolean applyWater(WaterEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.pos.getY() < this.world.getSeaLevel() - 1;
			case AT -> this.pos.getY() == this.world.getSeaLevel() - 1;
			case ABOVE -> this.pos.getY() > this.world.getSeaLevel() - 1;
		};
	}

	@Override
	public boolean applyVoid(VoidEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.pos.getY() < this.world.getBottomY();
			case AT -> this.pos.getY() == this.world.getBottomY();
			case ABOVE -> this.pos.getY() > this.world.getBottomY();
		};
	}
}
