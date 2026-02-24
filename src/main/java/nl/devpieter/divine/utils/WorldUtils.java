package nl.devpieter.divine.utils;

import nl.devpieter.utilize.utils.minecraft.ClientUtils;

public class WorldUtils {

    private WorldUtils() {
    }

    public static long getWorldTime() {
        if (ClientUtils.getWorld() == null) return -1;
        return ClientUtils.getWorld().getLevelProperties().getTime();
    }
}
