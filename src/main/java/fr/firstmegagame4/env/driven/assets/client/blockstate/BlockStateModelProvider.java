package fr.firstmegagame4.env.driven.assets.client.blockstate;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;

import java.util.Map;

public class BlockStateModelProvider {

	private final Map<BlockState, BakedModel> provider = new Object2ObjectOpenHashMap<>();

	private BlockStateModelProvider() {}

	public BakedModel apply(BlockState state) {
		return this.provider.get(state);
	}

	public static class Builder {

		private final Map<BlockState, BakedModel> elements = new Object2ObjectOpenHashMap<>();

		public Builder append(BlockState state, BakedModel model) {
			this.elements.put(state, model);
			return this;
		}

		public Builder append(Map<BlockState, BakedModel> elements) {
			this.elements.putAll(elements);
			return this;
		}

		public BlockStateModelProvider build() {
			BlockStateModelProvider provider = new BlockStateModelProvider();
			provider.provider.putAll(this.elements);
			return provider;
		}
	}
}
