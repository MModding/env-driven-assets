package fr.firstmegagame4.env.driven.assets.client.duck;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

public interface BakedModelManagerDuckInterface {

	BakedModel env_driven_assets$convert(BakedModel source, Identifier reference);

	Identifier env_driven_assets$revert(BakedModel source);

	BakedModel env_driven_assets$fetch(BlockState source);

	Identifier env_driven_assets$revert(BlockState source);
}
