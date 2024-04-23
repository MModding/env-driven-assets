package com.mmodding.env.driven.assets.client.injected;

import com.mmodding.env.driven.assets.client.blockstate.BlockStateManager;
import com.mmodding.env.driven.assets.client.model.ModelManager;

public interface ManagerContainer {

	default BlockStateManager getBlockStateManager() {
		throw new IllegalStateException();
	}

	default ModelManager getModelManager() {
		throw new IllegalStateException();
	}
}
