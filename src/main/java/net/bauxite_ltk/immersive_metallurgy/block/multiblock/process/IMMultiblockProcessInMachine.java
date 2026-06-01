package net.bauxite_ltk.immersive_metallurgy.block.multiblock.process;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInMachine;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.List;
import java.util.function.BiFunction;

public class IMMultiblockProcessInMachine<R extends MultiblockRecipe>
        extends MultiblockProcessInMachine<R> {


    public IMMultiblockProcessInMachine(ResourceLocation recipeId, BiFunction<Level, ResourceLocation, R> getRecipe, int... inputSlots) {
        super(recipeId, getRecipe, inputSlots);
    }

    public IMMultiblockProcessInMachine(RecipeHolder<R> recipe, int... inputSlots) {
        super(recipe, inputSlots);
    }

    public IMMultiblockProcessInMachine(BiFunction<Level, ResourceLocation, R> getRecipe, CompoundTag data) {
        super(getRecipe, data);
    }

    @Override
    protected void outputItem(ProcessContext.ProcessContextInMachine<R> context, ItemStack output, IMultiblockLevel level)
    {
        int[] outputSlots = context.getOutputSlots();
        for(int iOutputSlot : outputSlots)
        {
            final IItemHandlerModifiable inv = context.getInventory();
            ItemStack s = inv.getStackInSlot(iOutputSlot);
            //TFCTrihydrate.LOGGER.info("{}", s);
            //TFCTrihydrate.LOGGER.info("{}", output);
            if(s.isEmpty())
            {
                inv.setStackInSlot(iOutputSlot, output.copy());
                break;
            }
            else if(ItemStack.isSameItem(s, output)&&s.getCount()+output.getCount() <= inv.getSlotLimit(iOutputSlot))
            {
                s.grow(output.getCount());
                break;
            }
        }
    }

    @Override
    protected boolean canOutputItem(ProcessContext.ProcessContextInMachine<R> context, ItemStack output)
    {
        int[] outputSlots = context.getOutputSlots();
        for(int iOutputSlot : outputSlots)
        {
            final IItemHandlerModifiable inv = context.getInventory();
            ItemStack s = inv.getStackInSlot(iOutputSlot);
            if(s.isEmpty())
                return true;
            final boolean match = ItemStack.isSameItem(s, output);
            if(match&&s.getCount()+output.getCount() <= inv.getSlotLimit(iOutputSlot))
                return true;
        }
        return false;
    }

    @Override
    public boolean canProcess(ProcessContext.ProcessContextInMachine<R> context, Level level)
    {
        LevelDependentData<R> levelData = getLevelData(level);
        if(levelData.recipe() ==null)
            return true;
        if(context.getEnergy().extractEnergy(levelData.energyPerTick(), true)==levelData.energyPerTick())
        {
            List<ItemStack> outputs = getRecipeItemOutputs(level, context);
            if(outputs!=null)
                for(ItemStack output : outputs)
                    if(!output.isEmpty()&&!canOutputItem(context, output))
                        return false;
            List<FluidStack> fluidOutputs = levelData.recipe().getFluidOutputs();
            if(fluidOutputs!=null)
                for(int i = 0; i < fluidOutputs.size(); i++){
                    FluidStack output = fluidOutputs.get(i);
                    if(!canOutputFluidOrdered(context,output, i))
                        return false;
                }
            return context.additionalCanProcessCheck(this, level);
        }
        return false;
    }

    protected boolean canOutputFluidOrdered(ProcessContext.ProcessContextInMachine<R> context, FluidStack output, int order)
    {
        IFluidTank[] tanks = context.getInternalTanks();
        int[] outputTanks = context.getOutputTanks();
        int outputIndex = outputTanks[order];
        if(tanks[outputIndex].getFluidAmount() == tanks[outputIndex].getCapacity())
            return false;

        return tanks[outputIndex].fill(output, IFluidHandler.FluidAction.SIMULATE) == output.getAmount();
    }

}
