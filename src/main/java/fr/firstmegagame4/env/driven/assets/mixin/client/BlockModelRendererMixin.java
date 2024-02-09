package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.json.api.EnvJson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {

	@ModifyVariable(method = "renderSmooth", at = @At("HEAD"), argsOnly = true, index = 2)
	private BakedModel mutateSmooth(BakedModel original, @Local(argsOnly = true) BlockRenderView world, @Local(argsOnly = true) BlockPos pos) {
		return this.mutateBakedModel(original, world, pos);
	}

	@ModifyVariable(method = "renderFlat", at = @At("HEAD"), argsOnly = true, index = 2)
	private BakedModel mutateFlat(BakedModel original, @Local(argsOnly = true) BlockRenderView world, @Local(argsOnly = true) BlockPos pos) {
		return this.mutateBakedModel(original, world, pos);
	}

	@Unique
	private BakedModel mutateBakedModel(BakedModel original, BlockRenderView world, BlockPos pos) {
		BakedModelDuckInterface ducked = (BakedModelDuckInterface) original;
		if (ducked.env_driven_assets$getEnvJson() != null) {
			EnvJson envJson = ducked.env_driven_assets$getEnvJson();
			Identifier identifier = envJson.apply(EDAEnvJsonVisitors.blockVisitor(world, pos));
			if (identifier != null) {
				return MinecraftClient.getInstance().getBakedModelManager().getModelManager().changeModelWithoutSettings(identifier);
			}
		}
		return original;
	}
}
