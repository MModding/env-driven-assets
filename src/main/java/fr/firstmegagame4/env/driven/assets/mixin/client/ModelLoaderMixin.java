package fr.firstmegagame4.env.driven.assets.mixin.client;

import fr.firstmegagame4.env.driven.assets.client.model.ModelManager;
import fr.firstmegagame4.env.driven.assets.client.duck.ModelLoaderDuckInterface;
import net.minecraft.client.render.model.ModelLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin implements ModelLoaderDuckInterface {

	@Unique
	private final ModelManager manager = new ModelManager();

	@Override
	public ModelManager env_driven_assets$getModelManager() {
		return this.manager;
	}
}
