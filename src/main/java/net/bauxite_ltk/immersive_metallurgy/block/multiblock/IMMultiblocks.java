package net.bauxite_ltk.immersive_metallurgy.block.multiblock;

import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.generic.WindowBlock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import com.google.common.collect.ImmutableList;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class IMMultiblocks {
    public static final List<MultiblockHandler.IMultiblock> TFCTH_MULTIBLOCKS = new ArrayList<>();
    public static IETemplateMultiblock BALL_MILL;
    public static IETemplateMultiblock FLOTATION_CELL;
    public static IETemplateMultiblock HYDROCYCLONE;
    public static IETemplateMultiblock THICKENER;
    public static IETemplateMultiblock ELITE_BLAST_FURNACE;


    public static void init()
    {
//        //Add general matcher predicates
//        //Basic blockstate matcher
//        BlockMatcher.addPredicate((expected, found, world, pos) -> expected==found? BlockMatcher.Result.allow(1): BlockMatcher.Result.deny(1));
//        //FourWayBlock (fences etc): allow additional connections
//        List<Property<Boolean>> sideProperties = ImmutableList.of(
//                CrossCollisionBlock.NORTH, CrossCollisionBlock.EAST, CrossCollisionBlock.SOUTH, CrossCollisionBlock.WEST
//        );
//        BlockMatcher.addPreprocessor((expected, found, world, pos) -> {
//            if(expected.getBlock() instanceof CrossCollisionBlock&&expected.getBlock()==found.getBlock())
//                for(Property<Boolean> side : sideProperties)
//                    if(!expected.getValue(side))
//                        found = found.setValue(side, false);
//            return found;
//        });
//        //Tags
//        ImmutableList.Builder<TagKey<Block>> genericTagsBuilder = ImmutableList.builder();
//        for(EnumMetals metal : EnumMetals.values())
//        {
//            IETags.MetalTags tags = IETags.getTagsFor(metal);
//            genericTagsBuilder.add(tags.storage)
//                    .add(tags.sheetmetal);
//        }
//        genericTagsBuilder.add(IETags.scaffoldingAlu);
//        genericTagsBuilder.add(IETags.scaffoldingSteel);
//        genericTagsBuilder.add(IETags.treatedWoodSlab);
//        genericTagsBuilder.add(IETags.treatedWood);
//        genericTagsBuilder.add(IETags.fencesSteel);
//        genericTagsBuilder.add(IETags.fencesAlu);
//        List<TagKey<Block>> genericTags = genericTagsBuilder.build();
//        BlockMatcher.addPredicate((expected, found, world, pos) -> {
//            if(expected.getBlock()!=found.getBlock())
//                for(TagKey<Block> t : genericTags)
//                    if(expected.is(t)&&found.is(t))
//                        return BlockMatcher.Result.allow(2);
//            return BlockMatcher.Result.DEFAULT;
//        });
//        //Ignore hopper facing
//        BlockMatcher.addPreprocessor((expected, found, world, pos) -> {
//            if(expected.getBlock()== Blocks.HOPPER&&found.getBlock()==Blocks.HOPPER)
//                return found.setValue(HopperBlock.FACING, expected.getValue(HopperBlock.FACING));
//            return found;
//        });
//        //Ignore sculk sensor properties
//        BlockMatcher.addPreprocessor((expected, found, world, pos) -> {
//            if(expected.getBlock()==Blocks.CALIBRATED_SCULK_SENSOR&&found.getBlock()==Blocks.CALIBRATED_SCULK_SENSOR)
//                return expected;
//            return found;
//        });
//        //Ignore mirrored windows
//        BlockMatcher.addPreprocessor((expected, found, world, pos) -> {
//            if(expected.getBlock() instanceof WindowBlock &&expected.getBlock()==found.getBlock()
//                    &&expected.getValue(IEProperties.FACING_ALL).getAxis()==found.getValue(IEProperties.FACING_ALL).getAxis())
//                return expected;
//            return found;
//        });
//        //Allow multiblocks to be formed under water
//        BlockMatcher.addPreprocessor((expected, found, world, pos) -> {
//            // Un-waterlog if the expected state is dry, but the found one is not
//            if(expected.hasProperty(WATERLOGGED)&&found.hasProperty(WATERLOGGED)
//                    &&!expected.getValue(WATERLOGGED)&&found.getValue(WATERLOGGED))
//                return found.setValue(WATERLOGGED, false);
//            else
//                return found;
//        });

        //Init IE multiblocks
        BALL_MILL = register(new BallMillMultiblock());
        FLOTATION_CELL = register(new FlotationCellMultiblock());
        HYDROCYCLONE = register(new HydrocycloneMultiblock());
        THICKENER = register(new ThickenerMultiblock());
        ELITE_BLAST_FURNACE = register(new EliteBlastFurnaceMultiblock());
    }


    private static <T extends MultiblockHandler.IMultiblock>
    T register(T multiblock)
    {
        TFCTH_MULTIBLOCKS.add(multiblock);
        MultiblockHandler.registerMultiblock(multiblock);
        return multiblock;
    }
}
