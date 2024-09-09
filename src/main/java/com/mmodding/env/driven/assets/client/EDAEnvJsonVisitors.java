package com.mmodding.env.driven.assets.client;

import com.moulberry.axiom.render.regions.ChunkedBlockRegion;
import com.moulberry.axiom.render.regions.MapBlockAndTintGetter;
import com.mmodding.env.driven.assets.client.impl.axiom.AxiomBlockEnvJsonVisitor;
import com.mmodding.env.driven.assets.client.impl.axiom.MappedBlockAndTintGetterInstance;
import com.mmodding.env.driven.assets.client.impl.env.json.BlockEnvJsonVisitor;
import com.mmodding.env.driven.assets.client.impl.env.json.ClientEnvJsonVisitor;
import com.mmodding.env.driven.assets.client.impl.env.json.EntityEnvJsonVisitor;
import com.mmodding.env.driven.assets.client.impl.env.json.EmptyVisitor;
import com.mmodding.env.driven.assets.client.impl.sodium.SodiumBlockEnvJsonVisitor;
import com.mmodding.env.driven.assets.mixin.client.ChunkRendererRegionAccessor;
import com.mmodding.env.json.api.EnvJsonVisitor;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
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
		else if (view instanceof World world) {
			return new BlockEnvJsonVisitor(world, pos);
		}
		else if (FabricLoader.getInstance().isModLoaded("sodium")) {
			if (view instanceof LevelSlice slice) {
				return new SodiumBlockEnvJsonVisitor(slice, pos);
			}
		}
		else if (FabricLoader.getInstance().isModLoaded("axiom")) {
			if (view instanceof ChunkedBlockRegion region) {
				return new AxiomBlockEnvJsonVisitor.ChunkedBlockVisitor(region, pos);
			}
			else if (view instanceof MapBlockAndTintGetter map) {
				return new AxiomBlockEnvJsonVisitor.MapBlockVisitor(map, pos);
			}
			else if (view instanceof MappedBlockAndTintGetterInstance mapped) {
				return new AxiomBlockEnvJsonVisitor.MappedBlockVisitor(mapped, pos);
			}
		}
		return new EmptyVisitor();
	}

	public static EnvJsonVisitor clientVisitor(MinecraftClient client) {
		return new ClientEnvJsonVisitor(client);
	}

	public static EnvJsonVisitor entityVisitor(Entity entity) {
		return new EntityEnvJsonVisitor(entity);
	}
}
