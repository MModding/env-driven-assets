package com.mmodding.env.driven.assets.client.impl.axiom;

import com.moulberry.axiom.render.regions.ChunkedBlockRegion;
import com.moulberry.axiom.render.regions.MapBlockAndTintGetter;
import com.mmodding.env.driven.assets.client.EDAEnvJsonVisitors;
import com.mmodding.env.driven.assets.client.EDAUtils;
import com.mmodding.env.json.api.EnvJsonVisitor;
import com.mmodding.env.json.api.rule.SkyEnvJsonRule;
import com.mmodding.env.json.api.rule.VoidEnvJsonRule;
import com.mmodding.env.json.api.rule.WaterEnvJsonRule;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * @see EDAEnvJsonVisitors
 */
public class AxiomBlockEnvJsonVisitor {

	public static class ChunkedBlockVisitor implements EnvJsonVisitor {

		private final ChunkedBlockRegion region;
		private final ClientWorld fallback;
		private final BlockPos pos;

		public ChunkedBlockVisitor(ChunkedBlockRegion region, BlockPos pos) {
			this.region = region;
			this.fallback = MinecraftClient.getInstance().world;
			this.pos = pos;
		}

		@Override
		public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
			return EDAUtils.compareKeys(this.fallback.getRegistryKey(), dimensionKey);
		}

		@Override
		public boolean applyDimensionTag(TagKey<World> dimensionTag) {
			return EDAUtils.worldIsIn(this.fallback, dimensionTag);
		}

		@Override
		public boolean applyBiomeKey(RegistryKey<Biome> biomeKey) {
			return this.fallback.getBiome(this.pos).matchesKey(biomeKey);
		}

		@Override
		public boolean applyBiomeTag(TagKey<Biome> biomeTag) {
			return this.fallback.getBiome(this.pos).isIn(biomeTag);
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
				return EDAUtils.lookupSubmerged(this.region, this.pos, this.region::getBlockState);
			} else {
				return !EDAUtils.lookupSubmerged(this.region, this.pos, this.region::getBlockState);
			}
		}

		@Override
		public boolean applySky(SkyEnvJsonRule.Localization localization) {
			return switch (localization) {
				case BELOW -> this.pos.getY() < this.region.getTopY() - 1;
				case AT -> this.pos.getY() == this.region.getTopY() - 1;
				case ABOVE -> this.pos.getY() > this.region.getTopY() - 1;
			};
		}

		@Override
		public boolean applyWater(WaterEnvJsonRule.Localization localization) {
			return switch (localization) {
				case BELOW -> this.pos.getY() < this.fallback.getSeaLevel() - 1;
				case AT -> this.pos.getY() == this.fallback.getSeaLevel() - 1;
				case ABOVE -> this.pos.getY() > this.fallback.getSeaLevel() - 1;
			};
		}

		@Override
		public boolean applyVoid(VoidEnvJsonRule.Localization localization) {
			return switch (localization) {
				case BELOW -> this.pos.getY() < this.region.getBottomY();
				case AT -> this.pos.getY() == this.region.getBottomY();
				case ABOVE -> this.pos.getY() > this.region.getBottomY();
			};
		}
	}

	public static class MapBlockVisitor implements EnvJsonVisitor {

		private final MapBlockAndTintGetter map;
		private final ClientWorld fallback;
		private final BlockPos pos;

		public MapBlockVisitor(MapBlockAndTintGetter map, BlockPos pos) {
			this.map = map;
			this.fallback = MinecraftClient.getInstance().world;
			this.pos = pos;
		}

		@Override
		public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
			return EDAUtils.compareKeys(this.fallback.getRegistryKey(), dimensionKey);
		}

		@Override
		public boolean applyDimensionTag(TagKey<World> dimensionTag) {
			return EDAUtils.worldIsIn(this.fallback, dimensionTag);
		}

		@Override
		public boolean applyBiomeKey(RegistryKey<Biome> biomeKey) {
			return this.fallback.getBiome(this.pos).matchesKey(biomeKey);
		}

		@Override
		public boolean applyBiomeTag(TagKey<Biome> biomeTag) {
			return this.fallback.getBiome(this.pos).isIn(biomeTag);
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
				return EDAUtils.lookupSubmerged(this.map, this.pos, this.map::getBlockState);
			} else {
				return !EDAUtils.lookupSubmerged(this.map, this.pos, this.map::getBlockState);
			}
		}

		@Override
		public boolean applySky(SkyEnvJsonRule.Localization localization) {
			return switch (localization) {
				case BELOW -> this.pos.getY() < this.fallback.getTopY() - 1;
				case AT -> this.pos.getY() == this.fallback.getTopY() - 1;
				case ABOVE -> this.pos.getY() > this.fallback.getTopY() - 1;
			};
		}

		@Override
		public boolean applyWater(WaterEnvJsonRule.Localization localization) {
			return switch (localization) {
				case BELOW -> this.pos.getY() < this.fallback.getSeaLevel() - 1;
				case AT -> this.pos.getY() == this.fallback.getSeaLevel() - 1;
				case ABOVE -> this.pos.getY() > this.fallback.getSeaLevel() - 1;
			};
		}

		@Override
		public boolean applyVoid(VoidEnvJsonRule.Localization localization) {
			return switch (localization) {
				case BELOW -> this.pos.getY() < this.fallback.getBottomY();
				case AT -> this.pos.getY() == this.fallback.getBottomY();
				case ABOVE -> this.pos.getY() > this.fallback.getBottomY();
			};
		}
	}

	public static class MappedBlockVisitor implements EnvJsonVisitor {

		private final BlockRenderView view;
		private final World world;
		private final BlockPos pos;

		public MappedBlockVisitor(MappedBlockAndTintGetterInstance mapped, BlockPos pos) {
			this.view = mapped.getView();
			this.world = mapped.getWorld();
			this.pos = pos;
		}

		@Override
		public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
			return EDAUtils.compareKeys(this.world.getRegistryKey(), dimensionKey);
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
				return EDAUtils.lookupSubmerged(this.view, this.pos, this.view::getBlockState);
			} else {
				return !EDAUtils.lookupSubmerged(this.view, this.pos, this.view::getBlockState);
			}
		}

		@Override
		public boolean applySky(SkyEnvJsonRule.Localization localization) {
			return switch (localization) {
				case BELOW -> this.pos.getY() < this.view.getTopY() - 1;
				case AT -> this.pos.getY() == this.view.getTopY() - 1;
				case ABOVE -> this.pos.getY() > this.view.getTopY() - 1;
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
				case BELOW -> this.pos.getY() < this.view.getBottomY();
				case AT -> this.pos.getY() == this.view.getBottomY();
				case ABOVE -> this.pos.getY() > this.view.getBottomY();
			};
		}
	}
}
