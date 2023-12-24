package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fr.firstmegagame4.env.driven.assets.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.impl.env.json.BlockEnvJsonVisitor;
import fr.firstmegagame4.env.json.api.EnvJson;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockRenderManager.class)
public abstract class BlockRenderManagerMixin {

	@Shadow
	@Final
	private BlockModels models;

	@WrapOperation(method = "renderBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"))
	private BakedModel changeModel(BlockRenderManager instance, BlockState state, Operation<BakedModel> original, @Local BlockPos pos, @Local BlockView world) {
		BakedModel bakedModel = original.call(instance, state);
		BakedModelDuckInterface ducked = (BakedModelDuckInterface) bakedModel;
		if (ducked.env_driven_assets$getEnvJson() != null) {
			EnvJson envJson = ducked.env_driven_assets$getEnvJson();
			BlockEnvJsonVisitor visitor = new BlockEnvJsonVisitor((ClientWorld) world, pos);
			Identifier identifier = envJson.apply(visitor);
			if (identifier != null) {
				return this.models.getModelManager().getModel(identifier);
			}
		}
		return bakedModel;
	}
}
