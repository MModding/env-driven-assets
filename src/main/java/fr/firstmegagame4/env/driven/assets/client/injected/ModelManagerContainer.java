package fr.firstmegagame4.env.driven.assets.client.injected;

import fr.firstmegagame4.env.driven.assets.client.model.ModelManager;

public interface ModelManagerContainer {

	default ModelManager getModelManager() {
		throw new IllegalStateException();
	}
}
