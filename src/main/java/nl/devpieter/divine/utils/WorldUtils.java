package nl.devpieter.divine.utils;

import nl.devpieter.utilize.client.utils.ClientUtils;

public class WorldUtils {

    private WorldUtils() {
    }

    public static long getWorldTime() {
        if (!ClientUtils.hasLevel()) return -1;
        return ClientUtils.getLevel().getGameTime();
    }
}
