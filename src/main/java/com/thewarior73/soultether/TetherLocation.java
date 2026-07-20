package com.thewarior73.soultether;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public record TetherLocation(BlockPos pos, String dimension) {

    /**
     * Checks for nbt data related to position and dimension.
     * @param nbt the data to check
     * @return Werther all the data was there, or not.
     */
    public static boolean hasLocationData(CompoundTag nbt) {
        return  nbt.contains("x") && nbt.contains("y") && nbt.contains("z") && nbt.contains("dimension");
    }

    /**
     * Reads data from a nbt tag and returns it if exists
     * @param nbt the nbt data
     * @return the location element and it's state (empty or not)
     */
    public static Optional<TetherLocation> readNbtData(CompoundTag nbt) {
        if (!hasLocationData(nbt)) {
            return Optional.empty();
        }

        Optional<Integer> xOpt = nbt.getInt("x");
        Optional<Integer> yOpt = nbt.getInt("y");
        Optional<Integer> zOpt = nbt.getInt("z");
        Optional<String> dimOpt = nbt.getString("dimension");

        BlockPos nbtPos = null;
        if (xOpt.isPresent() && yOpt.isPresent() && zOpt.isPresent()) {
            nbtPos = new BlockPos(xOpt.get(), yOpt.get(), zOpt.get());
        }

        String nbtDim = dimOpt.orElse("");

        return Optional.of(new TetherLocation(nbtPos, nbtDim));
    }

    /**
     * Writes data to a Compound Tag
     * @param nbt the tag to be edited.
     */
    public void writeToNbt(CompoundTag nbt) {
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putString("dimension", dimension);
    }

}
