package fr.firstmegagame4.env.driven.assets.client.impl.sodium;

import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.client.impl.env.json.BlockEnvJsonVisitor;
import fr.firstmegagame4.env.driven.assets.mixin.client.WorldSliceAccessor;
import fr.firstmegagame4.env.json.api.EnvJsonVisitor;
import fr.firstmegagame4.env.json.api.rule.SkyEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.VoidEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.WaterEnvJsonRule;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.FluidTags;
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
		return ((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld().getRegistryKey() == dimensionKey;
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
		return submerged && this.slice.getBlockState(this.pos.up()).getFluidState().isIn(FluidTags.WATER);
	}

	@Override
	public boolean applySky(SkyEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.pos.getY() <= this.slice.getTopY();
			case ABOVE -> this.pos.getY() > this.slice.getTopY();
		};
	}

	@Override
	public boolean applyWater(WaterEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.pos.getY() <= ((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld().getSeaLevel();
			case ABOVE -> this.pos.getY() > ((WorldSliceAccessor) (Object) this.slice).env_driven_assets$getWorld().getSeaLevel();
		};
	}

	@Override
	public boolean applyVoid(VoidEnvJsonRule.Localization localization) {
		return switch (localization) {
			case BELOW -> this.pos.getY() <= this.slice.getBottomY();
			case ABOVE -> this.pos.getY() > this.slice.getBottomY();
		};
	}
}
