package net.bauxite_ltk.immersive_metallurgy.worldgen.tree;

import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.bauxite_ltk.immersive_metallurgy.worldgen.IMConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class IMTreeGrowers {
    public static final TreeGrower MASON_PINE = new TreeGrower(IMUtils.MOD_ID + ":mason_pine",
            Optional.empty(), Optional.of(IMConfiguredFeatures.MASON_PINE_KEY), Optional.empty());
}
