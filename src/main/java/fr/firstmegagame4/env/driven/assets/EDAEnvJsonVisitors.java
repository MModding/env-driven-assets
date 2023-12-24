package fr.firstmegagame4.env.driven.assets;

import fr.firstmegagame4.env.driven.assets.impl.env.json.BlockEnvJsonVisitor;
import fr.firstmegagame4.env.driven.assets.impl.env.json.ClientEnvJsonVisitor;
import fr.firstmegagame4.env.driven.assets.impl.env.json.EntityEnvJsonVisitor;
import fr.firstmegagame4.env.json.api.EnvJsonVisitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EDAEnvJsonVisitors {

	public static EnvJsonVisitor blockVisitor(World world, BlockPos pos) {
		return new BlockEnvJsonVisitor(world, pos);
	}

	public static EnvJsonVisitor clientVisitor(MinecraftClient client) {
		return new ClientEnvJsonVisitor(client);
	}

	public static EnvJsonVisitor entityVisitor(Entity entity) {
		return new EntityEnvJsonVisitor(entity);
	}
}
