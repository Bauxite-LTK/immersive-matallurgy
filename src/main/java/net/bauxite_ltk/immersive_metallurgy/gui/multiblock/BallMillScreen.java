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

public class BallMillScreen extends IEContainerScreen<BallMillMenu> {
    private static final ResourceLocation TEXTURE = makeTextureLocation("ball_mill");
    private static final ResourceLocation TANK = IMUtils.modRL("ball_mill/tank_overlay");

    public BallMillScreen(BallMillMenu container, Inventory inventoryPlayer, Component title) {
        super(container, inventoryPlayer, title, TEXTURE);
    }

    @Nonnull
    @Override
    protected List<InfoArea> makeInfoAreas()
    {
        return ImmutableList.of(
                new FluidInfoArea(menu.tanks.input(), new Rect2i(leftPos+10, topPos+12, 16, 47), 20, 51, TANK),
                new FluidInfoArea(menu.tanks.output(), new Rect2i(leftPos+118, topPos+12, 16, 47), 20, 51, TANK),
                new EnergyInfoArea(leftPos+158, topPos+12, menu.energy),
                new TemperatureInfoArea(GetterAndSetter.constant(1000),leftPos,topPos)
        );
    }

    public static ResourceLocation makeTextureLocation(String name) {
        return IMUtils.modRL( "textures/gui/"+name+".png");
    }
}
