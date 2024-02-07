package fr.firstmegagame4.env.driven.assets.client.impl.axiom;

import com.moulberry.axiom.render.regions.ChunkedBlockRegion;
import com.moulberry.axiom.render.regions.MapBlockAndTintGetter;
import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.json.api.EnvJsonVisitor;
import fr.firstmegagame4.env.json.api.rule.SkyEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.VoidEnvJsonRule;
import fr.firstmegagame4.env.json.api.rule.WaterEnvJsonRule;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
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
			return this.fallback.getRegistryKey() == dimensionKey;
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
			return this.fallback.getRegistryKey() == dimensionKey;
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
}
