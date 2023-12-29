package fr.firstmegagame4.env.driven.assets.mixin.client;

import fr.firstmegagame4.env.driven.assets.client.EDAEnvJsonVisitors;
import fr.firstmegagame4.env.driven.assets.client.EDAUtils;
import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelManagerDuckInterface;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockDustParticle.class)
public class BlockDustParticleMixin {

	@Inject(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDDDDLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At("TAIL"))
	private void setupReference(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state, BlockPos blockPos, CallbackInfo ci) {
		BakedModelManagerDuckInterface ducked = (BakedModelManagerDuckInterface) MinecraftClient.getInstance().getBakedModelManager();
		BakedModel bakedModel = ducked.env_driven_assets$convert(ducked.env_driven_assets$revert(state));
		if (bakedModel instanceof BakedModelDuckInterface duckedModel && duckedModel.env_driven_assets$getEnvJson() != null) {
			Identifier identifier = duckedModel.env_driven_assets$getEnvJson().apply(EDAEnvJsonVisitors.blockVisitor(world, BlockPos.ofFloored(x, y, z)));
			if (identifier != null) {
				((SpriteBillboardParticleAccessor) this).env_driven_assets$setSprite(ducked.env_driven_assets$convert(identifier).getParticleSprite());
			}
		}
	}
}
