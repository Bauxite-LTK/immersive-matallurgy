package net.bauxite_ltk.immersive_metallurgy.block.multiblock.process;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import net.bauxite_ltk.immersive_metallurgy.block.multiblock.logic.ContinuousCastingMachineLogic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public class ContinuousCastingMachineProcessInMachine<R extends MultiblockRecipe> extends IMMultiblockProcessInMachine<R>{

    public ContinuousCastingMachineProcessInMachine(ResourceLocation recipeId, BiFunction<Level, ResourceLocation, R> getRecipe, int... inputSlots) {
        super(recipeId, getRecipe, inputSlots);
    }

    public ContinuousCastingMachineProcessInMachine(RecipeHolder<R> recipe, int... inputSlots) {
        super(recipe, inputSlots);
    }

    public ContinuousCastingMachineProcessInMachine(BiFunction<Level, ResourceLocation, R> getRecipe, CompoundTag data) {
        super(getRecipe, data);
    }

    @Override
    public boolean canProcess(ProcessContext.ProcessContextInMachine<R> context, Level level) {
        if(context instanceof ContinuousCastingMachineLogic.State state){
            LevelDependentData<R> levelData = getLevelData(level);
            if(levelData.recipe() ==null){
                return true;
            }
            boolean water = state.canConsumeWater();
            boolean gas = state.getFuelTicks() > 0;

            state.setPlayNoWaterSound(!water);
            state.setPlayNoGasSound(!gas);

            if(!water || !gas) return false;
        }
        return super.canProcess(context, level);
    }

    @Override
    public void doProcessTick(ProcessContext.ProcessContextInMachine<R> context, IMultiblockLevel level) {
        super.doProcessTick(context, level);
        if(context instanceof ContinuousCastingMachineLogic.State state){
            state.decreaseFuelTicks();
            state.consumeWater();
        }
    }
}
