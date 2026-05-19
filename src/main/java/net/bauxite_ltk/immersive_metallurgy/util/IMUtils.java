package net.bauxite_ltk.immersive_metallurgy.util;

import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;


public class IMUtils {
    public static final String MOD_ID = ImmersiveMetallurgy.MOD_ID;
    public static final Logger LOGGER = ImmersiveMetallurgy.LOGGER;

    public static ResourceLocation modRL(String path){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
