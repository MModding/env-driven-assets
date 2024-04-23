package com.mmodding.env.driven.assets.client.impl.env.json;

import com.mmodding.env.json.api.EnvJsonVisitor;
import com.mmodding.env.json.api.rule.SkyEnvJsonRule;
import com.mmodding.env.json.api.rule.VoidEnvJsonRule;
import com.mmodding.env.json.api.rule.WaterEnvJsonRule;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class EmptyVisitor implements EnvJsonVisitor {

	@Override
	public boolean applyDimensionKey(RegistryKey<World> dimensionKey) {
		return false;
	}

	@Override
	public boolean applyDimensionTag(TagKey<World> dimensionTag) {
		return false;
	}

	@Override
	public boolean applyBiomeKey(RegistryKey<Biome> biomeKey) {
		return false;
	}

	@Override
	public boolean applyBiomeTag(TagKey<Biome> biomeTag) {
		return false;
	}

	@Override
	public boolean applyXCoord(Int2BooleanFunction operation) {
		return false;
	}

	@Override
	public boolean applyYCoord(Int2BooleanFunction operation) {
		return false;
	}

	@Override
	public boolean applyZCoord(Int2BooleanFunction operation) {
		return false;
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
}
