package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.injected.ModelManagerContainer;
import fr.firstmegagame4.env.driven.assets.client.model.ModelManager;
import fr.firstmegagame4.env.json.api.EnvJson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {

	@ModifyVariable(method = "render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLnet/minecraft/util/math/random/Random;JI)V", at = @At("HEAD"), argsOnly = true)
	private BakedModel mutateBakedModel(BakedModel original, @Local BlockRenderView world, @Local BlockPos pos) {
		BakedModelDuckInterface ducked = (BakedModelDuckInterface) original;
		if (ducked.env_driven_assets$getEnvJson() != null) {
			EnvJson envJson = ducked.env_driven_assets$getEnvJson();
			Identifier identifier = envJson.apply(EDAEnvJsonVisitors.blockVisitor(world, pos));
			if (identifier != null) {
				ModelManager manager = ((ModelManagerContainer) MinecraftClient.getInstance().getBakedModelManager()).getModelManager();
				return manager.changeModelWithoutSettings(identifier);
			}
		}
		return original;
	}
}
