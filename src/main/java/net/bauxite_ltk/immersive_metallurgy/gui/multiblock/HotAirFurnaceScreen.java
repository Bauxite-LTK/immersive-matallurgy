package net.bauxite_ltk.immersive_metallurgy.gui.multiblock;

import blusunrize.immersiveengineering.api.energy.AveragingEnergyStorage;
import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import blusunrize.immersiveengineering.client.gui.info.FluidInfoArea;
import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import com.google.common.collect.ImmutableList;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.List;

public class HotAirFurnaceScreen extends IEContainerScreen<HotAirFurnaceMenu> {
    private static final ResourceLocation TEXTURE = makeTextureLocation("hot_air_furnace");
    private static final ResourceLocation INPUT_TANK = IMUtils.modRL("hot_air_furnace/input_tank");
    private static final ResourceLocation OUTPUT_TANK = IMUtils.modRL("hot_air_furnace/output_tank");
    private static final ResourceLocation PROGRESS = IMUtils.modRL("hot_air_furnace/fire");


    public HotAirFurnaceScreen(HotAirFurnaceMenu container, Inventory inventoryPlayer, Component title) {
        super(container, inventoryPlayer, title, TEXTURE);
    }

    @Nonnull
    @Override
    protected List<InfoArea> makeInfoAreas()
    {
        IEnergyStorage test = new AveragingEnergyStorage(12000);
        test.receiveEnergy(1,false);
        return ImmutableList.of(
                new FluidInfoArea(menu.tanks.inputGas(), new Rect2i(leftPos+33, topPos+18, 8, 41), 18, 49, INPUT_TANK),
                new FluidInfoArea(menu.tanks.outputAir(), new Rect2i(leftPos+135, topPos+18, 8, 41), 18, 49, OUTPUT_TANK)
                );
    }

    public static ResourceLocation makeTextureLocation(String name) {
        return IMUtils.modRL( "textures/gui/"+name+".png");
    }

    @Override
    protected void drawContainerBackgroundPre(@Nonnull GuiGraphics graphics, float f, int mx, int my)
    {
        float process = menu.guiProgress.get();
        //ImmersiveMetallurgy.LOGGER.info("progress:{}", process);
        if(process > 0)
        {
            int w = (int)Math.max(1, process*17);
            graphics.blitSprite(PROGRESS, 22, 17, 0, 17-w, leftPos+76, topPos+15+ 17-w, 22, w);
        }
    }
}
