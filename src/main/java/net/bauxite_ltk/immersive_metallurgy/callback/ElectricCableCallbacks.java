package net.bauxite_ltk.immersive_metallurgy.callback;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.client.ieobj.BlockCallback;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import com.mojang.math.Transformation;
import net.bauxite_ltk.immersive_metallurgy.block.transporter.ElectricCableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.*;

public class ElectricCableCallbacks implements BlockCallback<ElectricCableCallbacks.Key> {

    public static final ElectricCableCallbacks INSTANCE = new ElectricCableCallbacks();

    private static final Key INVALID = new Key(
            new ElectricCableBlockEntity.PhysicalConnectionsInfo(), 0
    );

    @Override
    public Key extractKey(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, BlockEntity blockEntity)
    {
        //ImmersiveMetallurgy.LOGGER.info("execute extractKey");
        if(!(blockEntity instanceof ElectricCableBlockEntity electricCable))
            return getDefaultKey();
        ElectricCableBlockEntity.PhysicalConnectionsInfo physicalConnectionsInfo = electricCable.getPhysicalConnectionsInfo();
        return new Key(physicalConnectionsInfo, Arrays.hashCode(physicalConnectionsInfo.toByteArray()));
    }

    @Override
    public Key getDefaultKey()
    {
        return INVALID;
    }

    @Override
    public IEProperties.IEObjState getIEOBJState(Key key)
    {
        //ImmersiveMetallurgy.LOGGER.info("execute getIEOBJState");
        List<String> parts = new ArrayList<>();
        Matrix4 rotationMatrix = new Matrix4();
        rotationMatrix.translate(0.5, 0.5, 0.5);
        ElectricCableBlockEntity.PhysicalConnectionsInfo physicalConnectionsInfo = key.physicalConnectionsInfo;
        for(int i = 0; i < 36; i ++){
            ElectricCableBlockEntity.PhysicalConnectionsInfo.CableState state = physicalConnectionsInfo.getState(i);
            Direction attachmentDir = Direction.from3DDataValue(i/6);
            Direction connectionDir = Direction.from3DDataValue(i%6);
            if(attachmentDir == connectionDir) {
                if (physicalConnectionsInfo.isDirectionTerminal(attachmentDir)) {
                    parts.add("terminal_" + attachmentDir.getName());
                    //ImmersiveMetallurgy.LOGGER.info("add terminal_{}", attachmentDir.getName());
                } else if (state.isExist()) {
                    parts.add("center_" + attachmentDir.getName());
                    //ImmersiveMetallurgy.LOGGER.info("add center_{}", attachmentDir.getName());
                }
            }
            else if(state.isExist()){
                parts.add("con_" + attachmentDir.getName() + "_" + connectionDir.getName());
            }
        }

        rotationMatrix.translate(-0.5, -0.5, -0.5);

        return new IEProperties.IEObjState(IEProperties.VisibilityList.show(parts), new Transformation(rotationMatrix.toMatrix4f()));
    }

    /** HOLY FUCK THIS
     * It seems that the Renderer will check if this key has changed then decide to update model
     * if we only have *physicalConnectionsInfo*, even the content has changed, the Renderer will not detect this change
     * I did a lot of tries to solve but all in vain, finally I try to add a Hash Code of content, and then it works magically.
     * Maybe only *Explicit Changes* in Key will trigger to update model.
     * Except this method, I haven't found another plan to trigger to update model yet.
     **/
    public record Key(
            ElectricCableBlockEntity.PhysicalConnectionsInfo physicalConnectionsInfo,
            int infoContentCache
    )
    {
//        int numActiveConnections()
//        {
//            int count = 0;
//            for(CastingChannelBlockEntity.ConnectionStyle c : connections.values())
//                if(c!= CastingChannelBlockEntity.ConnectionStyle.NO_CONNECTION)
//                    count++;
//            return count;
//        }

//        public boolean hasActiveConnection(Direction side)
//        {
//            return connections.get(side)!= CastingChannelBlockEntity.ConnectionStyle.NO_CONNECTION;
//        }

//        public boolean any(Direction... sides)
//        {
//            for(Direction side : sides)
//                if(hasActiveConnection(side))
//                    return true;
//            return false;
//        }
//
//        public boolean all(Direction... sides)
//        {
//            for(Direction side : sides)
//                if(!hasActiveConnection(side))
//                    return false;
//            return true;
//        }
    }
}
