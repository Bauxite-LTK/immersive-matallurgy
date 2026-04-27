package net.bauxite_ltk.immersive_metallurgy.callback;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.client.ieobj.BlockCallback;
import blusunrize.immersiveengineering.api.utils.DirectionUtils;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import com.mojang.math.Transformation;
import net.bauxite_ltk.immersive_metallurgy.ImmersiveMetallurgy;
import net.bauxite_ltk.immersive_metallurgy.block.metal.ElectricCableBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.*;

import static net.minecraft.core.Direction.*;

public class ElectricCableCallbacks implements BlockCallback<ElectricCableCallbacks.Key> {

    public static final ElectricCableCallbacks INSTANCE = new ElectricCableCallbacks();

    private static final Key INVALID = new Key(
            Util.make(new EnumMap<>(Direction.class), m -> {
                for(Direction d : DirectionUtils.VALUES)
                    m.put(d, ElectricCableBlockEntity.ConnectionStyle.NO_CONNECTION);
            }), DOWN, null, false, false, new HashMap<>()
    );

    @Override
    public Key extractKey(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, BlockEntity blockEntity)
    {
        if(!(blockEntity instanceof ElectricCableBlockEntity electricCable))
            return getDefaultKey();
        Map<Direction, ElectricCableBlockEntity.ConnectionStyle> connections = new EnumMap<>(Direction.class);
        for(Direction face : DirectionUtils.VALUES)
            connections.put(face, electricCable.getConnectionStyle(face));
        Direction mainDir = electricCable.getMainDir();
        Direction subDir = electricCable.getSubDir();
        boolean mainTerminal = electricCable.isMainTerminal();
        boolean subTerminal = electricCable.isSubTerminal();
        Map<Direction, Direction> connectionAndAttachment = electricCable.getConnectionAndAttachment();
        return new Key(connections, mainDir, subDir, mainTerminal, subTerminal, connectionAndAttachment);
    }

    @Override
    public Key getDefaultKey()
    {
        return INVALID;
    }

    @Override
    public IEProperties.IEObjState getIEOBJState(Key key)
    {
        ImmersiveMetallurgy.LOGGER.info("execute getIEOBJState");
        List<String> parts = new ArrayList<>();
        Matrix4 rotationMatrix = new Matrix4();
        rotationMatrix.translate(0.5, 0.5, 0.5);
        Direction mainDir = key.mainDir;
        Direction subDir = key.subDir;
        boolean mainTerminal = key.isMainTerminal;
        boolean subTerminal = key.isSubTerminal;
        if(mainDir != null){
            String mainDirName = mainDir.getName();
            parts.add("center_" + mainDirName);
            if(mainTerminal) parts.add("terminal_" + mainDirName);
        }
        if(subDir != null){
            String subDirName = subDir.getName();
            parts.add("center_" + subDirName);
            if(subTerminal) parts.add("terminal_" + subDirName);
        }

        Map<Direction, Direction> connectionAndAttachment = key.connectionAndAttachment;
        for(Direction connectionDir : connectionAndAttachment.keySet()){
            String connectionDirName = connectionDir.getName();
            String attachmentDirName = connectionAndAttachment.get(connectionDir).getName();
            parts.add("con_" + attachmentDirName + "_" + connectionDirName);
            ImmersiveMetallurgy.LOGGER.info("add con_{}_{}", attachmentDirName, connectionDirName);
        }








        rotationMatrix.translate(-0.5, -0.5, -0.5);

        return new IEProperties.IEObjState(IEProperties.VisibilityList.show(parts), new Transformation(rotationMatrix.toMatrix4f()));
    }
    
    
    public record Key(
            Map<Direction, ElectricCableBlockEntity.ConnectionStyle> connections,
            Direction mainDir,
            Direction subDir,
            boolean isMainTerminal,
            boolean isSubTerminal,
            Map<Direction, Direction> connectionAndAttachment
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
