package net.bauxite_ltk.immersive_metallurgy.worldgen.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

public class MasonPineFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<MasonPineFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> foliagePlacerParts(instance).and(IntProvider.codec(3,16).fieldOf("height").forGetter(p -> p.height)).apply(instance, MasonPineFoliagePlacer::new)

    );
    protected final IntProvider height;

    public MasonPineFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return IMTreePlacerTypes.MASON_PINE_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration treeConfiguration, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        for(int i = offset; i >= offset - foliageHeight; --i) {
            int layer = offset - i;
            int patternRadius = getPatternRadius(getLayerPattern(layer));
            this.placeLeavesRow(level, foliageSetter, random, treeConfiguration, attachment.pos(), patternRadius, i, attachment.doubleTrunk());
        }
    }

    @Override
    public int foliageHeight(@NotNull RandomSource randomSource, int height, @NotNull TreeConfiguration treeConfiguration) {
        return this.height.sample(randomSource);
    }

    @Override
    protected boolean shouldSkipLocation(@NotNull RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        int yn = Mth.abs(localY - offset.sample(random));
        int patternNum = getLayerPattern(yn);
        int patternRadius = getPatternRadius(patternNum);
        if(patternNum == 1) return false;
        else if(patternNum == 2 || patternNum % 3 == 0) return isSmallPlateIgnoreLocation(patternRadius, localX, localZ);
        else if(patternNum % 3 == 1) return isMiddleCrossIgnoreLocation(patternRadius,localX,localZ);
        else if(patternNum % 3 == 2) return isLargePlateIgnoreLocation(patternRadius,localX,localZ);
        else return false;
    }

    private static int getLayerPattern(int localY){
        if(localY == 0 || localY == 1) return 1;
        else if(localY%3 == 2) return 2+(localY/3)*2;
        else if(localY%3 == 0) return 2+(localY/3)*2;
        else if(localY%3 == 1) return 1+(localY/3)*2;
        return -1;
    }

    private static int getPatternRadius(int patternNum){
        if(patternNum == 1) return 0;
        if(patternNum == 2) return 1;
        else return 2 + (patternNum-3)/3;
    }



    private static boolean isLargePlateIgnoreLocation(int radius, int x, int z){
        if(Mth.abs(x) == radius && z == 0) return true;
        if(Mth.abs(z) == radius && x == 0) return true;
        return false;
    }

    private static boolean isMiddleCrossIgnoreLocation(int radius, int x, int z){
        int xp = Mth.abs(x);
        int zp = Mth.abs(z);
        if((radius - xp) % 2 == 0 && zp == radius) return true;
        else if((radius - zp) % 2 == 0 && xp == radius) return true;
        else if(radius > 2 && (radius - xp) % 2 == 0 && zp == radius-1 && xp < radius) return true;
        else if(radius > 2 && (radius - zp) % 2 == 0 && xp == radius-1 && zp < radius) return true;
        return false;
    }

    private static boolean isSmallPlateIgnoreLocation(int radius, int x, int z){
        int xp = Mth.abs(x);
        int zp = Mth.abs(z);
        if(xp == radius && zp > (radius-1)/2) return true;
        if(zp == radius && xp > (radius-1)/2) return true;
        return false;
    }
}
