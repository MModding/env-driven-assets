package fr.firstmegagame4.env.driven.assets.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import fr.firstmegagame4.env.driven.assets.duck.JsonUnbakedModelDuckInterface;
import fr.firstmegagame4.env.json.api.resource.ExtendedResource;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(BakedModelManager.class)
public class BakedModelManagerMixin {

	@Shadow
	@Final
	private static Logger LOGGER;

	@WrapOperation(method = "method_45898", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;of(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;"))
	private static <F, S> Pair<F, S> mutateValue(F first, S second, Operation<Pair<F, S>> original, @Local Map.Entry<Identifier, Resource> entry) {
		ExtendedResource resource = ExtendedResource.of(entry.getValue());
		try {
			((JsonUnbakedModelDuckInterface) second).env_driven_assets$setEnvJson(resource.getEnvJson());
		}
		catch (Exception exception) {
			LOGGER.error("Failed to load env.json for model {}", entry.getKey(), exception);
		}
		// noinspection MixinExtrasOperationParameters
		return original.call(first, second);
	}
}
