package net.bauxite_ltk.immersive_metallurgy.gui.multiblock;

import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import blusunrize.immersiveengineering.client.gui.info.EnergyInfoArea;
import blusunrize.immersiveengineering.client.gui.info.FluidInfoArea;
import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import com.google.common.collect.ImmutableList;
import net.bauxite_ltk.immersive_metallurgy.gui.info.TemperatureInfoArea;
import net.bauxite_ltk.immersive_metallurgy.util.IMUtils;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;
import java.util.List;

public class EliteBlastFurnaceScreen extends IEContainerScreen<EliteBlastFurnaceMenu> {
    private static final ResourceLocation TEXTURE = makeTextureLocation("elite_blast_furnace");
    private static final ResourceLocation AIR_TANK = IMUtils.modRL("elite_blast_furnace/air_tank");
    private static final ResourceLocation GAS_TANK = IMUtils.modRL("elite_blast_furnace/gas_tank");
    private static final ResourceLocation METAL_TANK = IMUtils.modRL("elite_blast_furnace/metal_tank");

    public EliteBlastFurnaceScreen(EliteBlastFurnaceMenu container, Inventory inventoryPlayer, Component title) {
        super(container, inventoryPlayer, title, TEXTURE);
        this.imageHeight = 196;
        this.inventoryLabelY = 105;
    }

    @Nonnull
    @Override
    protected List<InfoArea> makeInfoAreas()
    {
        return ImmutableList.of(
                new FluidInfoArea(menu.tanks.outputGas(), new Rect2i(leftPos+148, topPos+15, 8, 41), 18, 49, GAS_TANK),
                new FluidInfoArea(menu.tanks.outputMetal(), new Rect2i(leftPos+148, topPos+71, 8, 23), 18, 32, METAL_TANK),
                new FluidInfoArea(menu.tanks.inputAirLeft(), new Rect2i(leftPos+42, topPos+30, 4, 52), 10, 58, AIR_TANK),
                new FluidInfoArea(menu.tanks.inputAirRight(), new Rect2i(leftPos+130, topPos+30, 4, 52), 10, 58, AIR_TANK),
                //new EnergyInfoArea(leftPos+158, topPos+12, menu.energy),
                new TemperatureInfoArea(menu.temperature, leftPos+12, topPos+10)
        );
    }

    public static ResourceLocation makeTextureLocation(String name) {
        return IMUtils.modRL( "textures/gui/"+name+".png");
    }
}
