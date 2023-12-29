package fr.firstmegagame4.env.driven.assets.mixin.client;

import fr.firstmegagame4.env.driven.assets.client.duck.BakedModelDuckInterface;
import fr.firstmegagame4.env.json.api.EnvJson;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.WrapperBakedModel;
import net.minecraft.client.render.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ForwardingBakedModel.class)
public class ForwardingBakedModelMixin implements BakedModelDuckInterface {

	@Shadow
	protected BakedModel wrapped;

	@Override
	public EnvJson env_driven_assets$getEnvJson() {
		return ((BakedModelDuckInterface) WrapperBakedModel.unwrap(this.wrapped)).env_driven_assets$getEnvJson();
	}

	@Override
	public void env_driven_assets$setEnvJson(EnvJson envJson) {
		((BakedModelDuckInterface) WrapperBakedModel.unwrap(this.wrapped)).env_driven_assets$setEnvJson(envJson);
	}
}
