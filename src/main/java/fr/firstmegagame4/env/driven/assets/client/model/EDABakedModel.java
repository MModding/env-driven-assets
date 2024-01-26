package fr.firstmegagame4.env.driven.assets.client.model;

import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.duck.ModelLoaderDuckInterface;
import fr.firstmegagame4.env.json.api.EnvJson;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.function.Supplier;

public class EDABakedModel extends ForwardingBakedModel {

	private final ModelManager manager;

	public EDABakedModel(ModelLoader loader, BakedModel wrapped) {
		this.manager = ((ModelLoaderDuckInterface) loader).env_driven_assets$getModelManager();
		this.wrapped = wrapped;
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
		if (this.getEnvJson() != null) {
			Identifier identifier = this.getEnvJson().apply(EDAEnvJsonVisitors.blockVisitor(blockView, pos));
			if (identifier != null) {
				this.manager.changeModelAndKeepSettings(this, identifier).emitBlockQuads(blockView, state, pos, randomSupplier, context, true);
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
				this.manager.changeModelAndKeepSettings(this, identifier).emitItemQuads(stack, randomSupplier, context, true);
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
		return ((BakedModelDuckInterface) this).env_driven_assets$getEnvJson();
	}
}
