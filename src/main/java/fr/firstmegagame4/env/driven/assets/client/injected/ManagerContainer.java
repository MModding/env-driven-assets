package fr.firstmegagame4.env.driven.assets.client.injected;

import fr.firstmegagame4.env.driven.assets.client.blockstate.BlockStateManager;
import fr.firstmegagame4.env.driven.assets.client.model.ModelManager;

public interface ManagerContainer {

	default BlockStateManager getBlockStateManager() {
		throw new IllegalStateException();
	}

	default ModelManager getModelManager() {
		throw new IllegalStateException();
	}
}
