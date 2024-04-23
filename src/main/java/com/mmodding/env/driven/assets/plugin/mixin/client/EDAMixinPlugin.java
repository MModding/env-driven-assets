package com.mmodding.env.driven.assets.plugin.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class EDAMixinPlugin implements IMixinConfigPlugin {

	@Override
	public void onLoad(String mixinPackage) {}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (!FabricLoader.getInstance().isModLoaded("sodium")) {
			return !mixinClassName.equals("com.mmodding.env.driven.assets.mixin.client.WorldSliceAccessor");
		}
		if (!FabricLoader.getInstance().isModLoaded("axiom")) {
			BooleanSupplier mapped = () -> mixinClassName.equals("com.mmodding.env.driven.assets.mixin.client.MappedBlockAndTintGetterMixin");
			BooleanSupplier chunked = () -> mixinClassName.equals("com.mmodding.env.driven.assets.mixin.client.ChunkedBlockRegionMixin");
			return !mapped.getAsBoolean() && !chunked.getAsBoolean();
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
