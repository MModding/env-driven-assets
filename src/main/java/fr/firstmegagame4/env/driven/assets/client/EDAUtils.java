package fr.firstmegagame4.env.driven.assets.client;

import fr.firstmegagame4.env.driven.assets.client.duck.JsonUnbakedModelDuckInterface;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.MultipartUnbakedModel;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.WeightedUnbakedModel;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class EDAUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger("env_driven_assets");

	public static <T> boolean compareKeys(RegistryKey<T> left, RegistryKey<T> right) {
		return left.getRegistry() == right.getRegistry() && left.getValue() == right.getValue();
	}

	public static boolean worldIsIn(World entry, TagKey<World> tag) {
		return EDAUtils.isIn(entry.getRegistryManager(), RegistryKeys.WORLD, tag, entry.getRegistryManager().get(RegistryKeys.WORLD).getEntry(entry));
	}

	public static <T> boolean isIn(DynamicRegistryManager manager, RegistryKey<? extends Registry<T>> registry, TagKey<T> tag, RegistryEntry<T> entry) {
		return manager.getOptional(registry).map(r -> r.getEntryList(tag).orElseThrow().contains(entry)).orElse(false);
	}

	public static boolean lookupSubmerged(BlockView world, BlockPos pos, Function<BlockPos, BlockState> stateFinder) {
		BlockPos upPos = pos.up();
		boolean valid = true;
		for (Direction direction : Direction.Type.HORIZONTAL) {
			BlockState state = stateFinder.apply(upPos.offset(direction));
			if (!state.getFluidState().isIn(FluidTags.WATER) && !state.isFullCube(world, pos)) {
				valid = false;
			}
		}
		if (valid) {
			List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
			directions.remove(Direction.DOWN);
			return EDAUtils.lookupAround(pos, stateFinder, state -> state.getFluidState().isIn(FluidTags.WATER), directions.toArray(new Direction[] {}));
		}
		else {
			return false;
		}
	}

	public static boolean lookupAround(BlockPos pos, Function<BlockPos, BlockState> stateFinder, Predicate<BlockState> condition, Direction[] directions) {
		for (Direction direction : directions) {
			if (condition.test(stateFinder.apply(pos.offset(direction)))) {
				return true;
			}
		}
		return false;
	}

	public static List<JsonUnbakedModel> retrieveJsonUnbakedModels(UnbakedModel unbakedModel, Function<Identifier, UnbakedModel> modelLoader) {
		List<JsonUnbakedModel> jsonUnbakedModels = new ArrayList<>();
		if (unbakedModel instanceof MultipartUnbakedModel multipart) {
			multipart.getModels().forEach(weighted -> weighted.getVariants().forEach(variant -> {
				UnbakedModel model = modelLoader.apply(variant.getLocation());
				if (model instanceof JsonUnbakedModel jsonUnbakedModel) {
					jsonUnbakedModels.add(jsonUnbakedModel);
				}
			}));
		}
		if (unbakedModel instanceof WeightedUnbakedModel weighted) {
			weighted.getVariants().forEach(variant -> {
				UnbakedModel model = modelLoader.apply(variant.getLocation());
				if (model instanceof JsonUnbakedModel jsonUnbakedModel) {
					jsonUnbakedModels.add(jsonUnbakedModel);
				}
			});
		}
		else if (unbakedModel instanceof JsonUnbakedModel jsonUnbakedModel) {
			jsonUnbakedModels.add(jsonUnbakedModel);
		}
		return jsonUnbakedModels;
	}

	public static List<JsonUnbakedModelDuckInterface> retrieveJsonUnbakedModelDuckInterfaces(UnbakedModel unbakedModel, Function<Identifier, UnbakedModel> modelLoader) {
		return EDAUtils.retrieveJsonUnbakedModels(unbakedModel, modelLoader).stream().map(model -> (JsonUnbakedModelDuckInterface) model).toList();
	}

	public static class ActionHashMap<K, V> extends Object2ObjectOpenHashMap<K, V> implements Map<K, V> {

		private final Function<K, K> keyAction;
		private final Function<V, V> valueAction;

		public ActionHashMap(Function<K, K> keyAction, Function<V, V> valueAction) {
			super();
			this.keyAction = keyAction;
			this.valueAction = valueAction;
		}

		@Override
		@SuppressWarnings("unchecked")
		public V get(Object key) {
			return this.valueAction.apply(super.get(this.keyAction.apply((K) key)));
		}

		@Override
		@SuppressWarnings("unchecked")
		public V getOrDefault(Object key, V defaultValue) {
			return this.valueAction.apply(super.getOrDefault(this.keyAction.apply((K) key), defaultValue));
		}

		@Override
		public V put(K key, V value) {
			return super.put(this.keyAction.apply(key), this.valueAction.apply(value));
		}
	}
}
