package fr.firstmegagame4.env.driven.assets.client.duck;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

public interface BakedModelManagerDuckInterface {

	BakedModel env_driven_assets$convert(Identifier reference);

	Identifier env_driven_assets$revert(BakedModel source);

	Identifier env_driven_assets$revert(BlockState source);
}
