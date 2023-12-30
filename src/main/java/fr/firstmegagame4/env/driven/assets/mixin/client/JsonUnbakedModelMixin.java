package fr.firstmegagame4.env.driven.assets.mixin.client;

import fr.firstmegagame4.env.driven.assets.client.duck.JsonUnbakedModelDuckInterface;
import fr.firstmegagame4.env.json.api.EnvJson;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(JsonUnbakedModel.class)
public class JsonUnbakedModelMixin implements JsonUnbakedModelDuckInterface {

	@Unique
	private EnvJson envJson = null;

	@Override
	public EnvJson env_driven_assets$getEnvJson() {
		return this.envJson;
	}

	@Override
	public void env_driven_assets$setEnvJson(EnvJson envJson) {
		this.envJson = envJson;
	}

	/* @Inject(method = "deserialize(Ljava/io/Reader;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;", at = @At("TAIL"), cancellable = true)
	private static void retrieveEnvJson(Reader input, CallbackInfoReturnable<JsonUnbakedModel> cir) {
		if (FabricLoader.getInstance().isModLoaded("modernfix")) {
			JsonUnbakedModelDuckInterface ducked = (JsonUnbakedModelDuckInterface) cir.getReturnValue();
			if (input instanceof ExtendedResourceReader reader) {
				try {
					ducked.env_driven_assets$setEnvJson(reader.getExtendedResource().getEnvJson());
				} catch (IOException ioException) {
					throw new JsonParseException(ioException);
				}
			}
			cir.setReturnValue((JsonUnbakedModel) ducked);
		}
	} */

	@Inject(method = "getModelDependencies", at = @At("TAIL"), cancellable = true)
	private void mutateDependencies(CallbackInfoReturnable<Collection<Identifier>> cir) {
		Collection<Identifier> dependencies = cir.getReturnValue();
		if (this.envJson != null) {
			this.envJson.members().forEach(member -> dependencies.add(member.result()));
		}
		cir.setReturnValue(dependencies);
	}
}
