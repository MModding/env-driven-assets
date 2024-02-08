package fr.firstmegagame4.env.driven.assets.mixin.client;

import fr.firstmegagame4.env.driven.assets.client.impl.axiom.MappedBlockAndTintGetterInstance;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(targets = "com.moulberry.axiom.render.ChunkRenderOverrider$MappedBlockAndTintGetter")
public class MappedBlockAndTintGetterMixin implements MappedBlockAndTintGetterInstance {

	@Shadow
	@Final
	private World level;

	@Override
	public BlockRenderView getView() {
		return (BlockRenderView) this;
	}

	@Override
	public World getWorld() {
		return this.level;
	}
}
