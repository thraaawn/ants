package org.dave.ants.render;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import org.dave.ants.Ants;

import java.util.HashMap;
import java.util.Map;

public class BakedModelLoader implements ICustomModelLoader {
    private Map<ResourceLocation, IModel> modelMap;

    public BakedModelLoader() {
        this.modelMap = new HashMap<>();

        this.modelMap.put(new ResourceLocation(Ants.MODID, "bakedchamberhill"), new ChamberHillModel());
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelMap.containsKey(new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath()));
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return modelMap.get(new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath()));
    }
}
