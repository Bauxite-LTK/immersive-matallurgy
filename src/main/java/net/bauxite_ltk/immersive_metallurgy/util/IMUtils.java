package net.bauxite_ltk.immersive_metallurgy.util;

import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.minecraft.resources.ResourceLocation;

public class IMUtils {
    public static final String MOD_ID = ImmersiveMetallurgy.MOD_ID;

    public static ResourceLocation modRL(String path){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
