package fr.firstmegagame4.env.driven.assets.client.model;

import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
import fr.firstmegagame4.env.driven.assets.client.blockstate.BlockStateModelProvider;
import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.injected.ManagerContainer;
import fr.firstmegagame4.env.json.api.EnvJson;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.WrapperBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.function.Supplier;

public class EDABakedModel extends ForwardingBakedModel {

	private final ManagerContainer container;
	private final ModelBakeSettings settings;

	public EDABakedModel(ModelLoader loader, BakedModel wrapped, ModelBakeSettings settings) {
		this.container = loader;
		this.wrapped = wrapped;
		this.settings = settings;
	}

	@Override
	public boolean shouldAllowBlockRedirection(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, boolean isAlreadyRedirected) {
		return !isAlreadyRedirected;
	}

	@Override
	public boolean shouldAllowItemRedirection(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context, boolean isAlreadyRedirected) {
		return !isAlreadyRedirected;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		EnvJson envJson = this.container.getBlockStateManager().getStateEnvJson(state);
		if (envJson != null) {
			Identifier identifier = this.getEnvJson().apply(EDAEnvJsonVisitors.blockVisitor(blockView, pos));
			if (identifier != null) {
				BlockStateModelProvider provider = this.container.getBlockStateManager().getProvider(identifier);
				provider.apply(state).emitBlockQuads(blockView, state, pos, randomSupplier, context);
				return;
			}
		}
		if (this.getEnvJson() != null) {
			Identifier identifier = this.getEnvJson().apply(EDAEnvJsonVisitors.blockVisitor(blockView, pos));
			if (identifier != null) {
				this.container.getModelManager().changeModelWithSettings(identifier, this.settings).emitBlockQuads(blockView, state, pos, randomSupplier, context, true);
				return;
			}
		}
		super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		if (this.getEnvJson() != null) {
			Identifier identifier = this.getEnvJson().apply(EDAEnvJsonVisitors.clientVisitor(MinecraftClient.getInstance()));
			if (identifier != null) {
				this.container.getModelManager().changeModelWithSettings(identifier, this.settings).emitItemQuads(stack, randomSupplier, context, true);
				return;
			}
		}
		super.emitItemQuads(stack, randomSupplier, context);
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	private EnvJson getEnvJson() {
		return ((BakedModelDuckInterface) WrapperBakedModel.unwrap(this)).env_driven_assets$getEnvJson();
	}
}
