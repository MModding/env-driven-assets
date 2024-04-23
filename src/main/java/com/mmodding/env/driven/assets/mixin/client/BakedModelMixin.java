package com.mmodding.env.driven.assets.mixin.client;

import com.mmodding.env.driven.assets.client.injected.BakedModelRedirection;
import net.minecraft.client.render.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BakedModel.class)
public interface BakedModelMixin extends BakedModelRedirection {}
