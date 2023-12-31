package fr.firstmegagame4.env.driven.assets.client;

import fr.firstmegagame4.env.driven.assets.client.duck.JsonUnbakedModelDuckInterface;
import net.minecraft.client.render.model.MultipartUnbakedModel;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.WeightedUnbakedModel;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class EDAUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger("env_driven_assets");

	public static boolean worldIsIn(World entry, TagKey<World> tag) {
		return EDAUtils.isIn(entry.getRegistryManager(), RegistryKeys.WORLD, tag, entry.getRegistryManager().get(RegistryKeys.WORLD).getEntry(entry));
	}

	public static <T> boolean isIn(DynamicRegistryManager manager, RegistryKey<? extends Registry<T>> registry, TagKey<T> tag, RegistryEntry<T> entry) {
		return manager.get(registry).getEntryList(tag).orElseThrow().contains(entry);
	}

	public static <K, V> Map<V, K> reverseMap(Map<K, V> original, Supplier<Map<V, K>> creator) {
		Map<V, K> reversed = creator.get();
		original.forEach((key, value) -> reversed.put(value, key));
		return reversed;
	}

	public static List<JsonUnbakedModel> retrieveJsonUnbakedModels(UnbakedModel unbakedModel, Function<Identifier, UnbakedModel> modelLoader) {
		List<JsonUnbakedModel> jsonUnbakedModels = new ArrayList<>();
		/* if (unbakedModel instanceof MultipartUnbakedModel multipart) {
			multipart.getModels().forEach(weighted -> weighted.getVariants().forEach(variant -> {
				UnbakedModel model = modelLoader.apply(variant.getLocation());
				if (model instanceof JsonUnbakedModel jsonUnbakedModel) {
					jsonUnbakedModels.add(jsonUnbakedModel);
				}
			}));
		} */
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
}
