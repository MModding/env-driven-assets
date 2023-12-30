package fr.firstmegagame4.env.driven.assets.client.injected;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.function.Supplier;

public interface BakedModelRedirection extends FabricBakedModel {

	default boolean shouldAllowBlockRedirection(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, boolean isAlreadyRedirected) {
		return true;
	}

	default boolean shouldAllowItemRedirection(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context, boolean isAlreadyRedirected) {
		return true;
	}

	default void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, boolean isRedirected) {
		if (this.shouldAllowBlockRedirection(blockView, state, pos, randomSupplier, context, isRedirected)) {
			this.emitBlockQuads(blockView, state, pos, randomSupplier, context);
		}
		else {
			FabricBakedModel.super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
		}
	}

	default void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context, boolean isRedirected) {
		if (this.shouldAllowItemRedirection(stack, randomSupplier, context, isRedirected)) {
			this.emitItemQuads(stack, randomSupplier, context);
		}
		else {
			FabricBakedModel.super.emitItemQuads(stack, randomSupplier, context);
		}
	}
}
