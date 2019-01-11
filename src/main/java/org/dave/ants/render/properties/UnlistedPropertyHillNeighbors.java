package org.dave.ants.render.properties;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.HashMap;
import java.util.Map;

public class UnlistedPropertyHillNeighbors implements IUnlistedProperty<UnlistedPropertyHillNeighbors.HillNeighbors> {
    @Override
    public String getName() {
        return "hillNeighbors";
    }

    @Override
    public boolean isValid(HillNeighbors value) {
        return true;
    }

    @Override
    public Class<HillNeighbors> getType() {
        return HillNeighbors.class;
    }

    @Override
    public String valueToString(HillNeighbors value) {
        return value.toString();
    }

    public enum EnumNeighborDirections {
        NORTH(new Vec3i(0, 0, -1)),
        NORTHEAST(new Vec3i(1, 0, -1)),
        EAST(new Vec3i(1, 0, 0)),
        SOUTHEAST(new Vec3i(1, 0, 1)),
        SOUTH(new Vec3i(0, 0, 1)),
        SOUTHWEST(new Vec3i(-1, 0, 1)),
        WEST(new Vec3i(-1, 0, 0)),
        NORTHWEST(new Vec3i(-1, 0, -1)),
        UP(new Vec3i(0, 1, 0)),
        DOWN(new Vec3i(0, -1, 0));

        private final Vec3i directionVec;
        EnumNeighborDirections(Vec3i directionVec) {
            this.directionVec = directionVec;
        }

        public Vec3i getDirectionVec() {
            return directionVec;
        }

        public BlockPos offset(BlockPos pos) {
            return pos.add(this.directionVec);
        }
    }

    public static class HillNeighbors {
        public Map<EnumNeighborDirections, Integer> neighborHeights = new HashMap<>();

        public void setNeighborHeight(EnumNeighborDirections facing, int height) {
            neighborHeights.put(facing, height);
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return toString().equals(obj.toString());
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("NeighborHeights[");
            for(Map.Entry<EnumNeighborDirections, Integer> entry : neighborHeights.entrySet()) {
                if(entry.getValue() == 0) {
                    continue;
                }

                builder.append(String.format("[%s=%d]", entry.getKey().name(), entry.getValue()));
            }
            builder.append(']');

            return builder.toString();
        }

        public int north() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.NORTH, 0);
        }

        public int northeast() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.NORTHEAST, 0);
        }

        public int east() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.EAST, 0);
        }

        public int southeast() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.SOUTHEAST, 0);
        }

        public int south() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.SOUTH, 0);
        }

        public int southwest() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.SOUTHWEST, 0);
        }

        public int west() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.WEST, 0);
        }

        public int northwest() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.NORTHWEST, 0);
        }

        public int up() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.UP, 0);
        }

        public int down() {
            return neighborHeights.getOrDefault(EnumNeighborDirections.DOWN, 0);
        }
    }
}
