package fr.firstmegagame4.env.driven.assets.client;

import fr.firstmegagame4.env.driven.assets.client.impl.env.json.BlockEnvJsonVisitor;
import fr.firstmegagame4.env.driven.assets.client.impl.env.json.ClientEnvJsonVisitor;
import fr.firstmegagame4.env.driven.assets.client.impl.env.json.EntityEnvJsonVisitor;
import fr.firstmegagame4.env.driven.assets.client.impl.sodium.SodiumBlockEnvJsonVisitor;
import fr.firstmegagame4.env.driven.assets.mixin.client.ChunkRendererRegionAccessor;
import fr.firstmegagame4.env.json.api.EnvJsonVisitor;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

public class EDAEnvJsonVisitors {

	public static EnvJsonVisitor blockVisitor(BlockRenderView view, BlockPos pos) {
		if (view instanceof ChunkRendererRegion region) {
			return new BlockEnvJsonVisitor(((ChunkRendererRegionAccessor) region).env_driven_assets$getWorld(), pos);
		}
		else if (FabricLoader.getInstance().isModLoaded("sodium")) {
			if (view instanceof WorldSlice slice) {
				return new SodiumBlockEnvJsonVisitor(slice, pos);
			}
		}
		return new BlockEnvJsonVisitor((World) view, pos);
	}

	public static EnvJsonVisitor clientVisitor(MinecraftClient client) {
		return new ClientEnvJsonVisitor(client);
	}

	public static EnvJsonVisitor entityVisitor(Entity entity) {
		return new EntityEnvJsonVisitor(entity);
	}
}
