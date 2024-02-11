package fr.firstmegagame4.env.driven.assets.client.blockstate;

import fr.firstmegagame4.env.json.api.EnvJson;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Function;

public class BlockStateManager {

	public final Map<Block, EnvJson> envJsonBlocks = new Object2ObjectOpenHashMap<>();

	public final Map<Identifier, BlockStateModelProvider> modelProviders = new Object2ObjectOpenHashMap<>();

	public EnvJson getStateEnvJson(BlockState state) {
		return this.envJsonBlocks.get(state.getBlock());
	}

	public BlockStateModelProvider getProvider(Identifier identifier) {
		return this.modelProviders.get(identifier);
	}

	public void appendBlock(Block block, EnvJson envJson) {
		this.envJsonBlocks.put(block, envJson);
	}

	public void provideIdentifiers(Block block, Function<Map<BlockState, ModelIdentifier>, Map<BlockState, BakedModel>> baker) {
		if (this.envJsonBlocks.containsKey(block)) {
			EnvJson envJson = this.envJsonBlocks.get(block);
			envJson.members().forEach(member -> {
				BlockStateModelProvider.Builder builder = new BlockStateModelProvider.Builder();
				Map<BlockState, ModelIdentifier> map = new Object2ObjectOpenHashMap<>();
				block.getStateManager().getStates().forEach(state -> map.put(state, BlockModels.getModelId(member.result(), state)));
				builder.append(baker.apply(map));
				this.modelProviders.put(member.result(), builder.build());
			});
		}
	}
}
