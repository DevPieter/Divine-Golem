package nl.devpieter.divine.enums;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum GolemLocation {

    UNDEFINED("Undefined", 0, 0, 0),
    MIDDLE_FRONT("Middle Front", -644, -269, 5),
    MIDDLE_CENTER("Middle Center", -689, -273, 5),
    MIDDLE_BEHIND("Middle Behind", -727, -284, 5),
    RIGHT_BEHIND("Right Behind", -678, -332, 5),
    LEFT_FRONT("Left Front", -649, -219, 5),
    RIGHT_FRONT("Right Front", -639, -328, 5);

    public static final Map<GolemLocation, List<BlockPos>> LOCATION_HEAD_POSITIONS = Arrays.stream(GolemLocation.values())
            .filter(loc -> loc != UNDEFINED)
            .collect(Collectors.toMap(loc -> loc, GolemLocation::getPossibleHeadPositions));

    private final String locationName;
    private final int headX;
    private final int headZ;
    private final int headMinY;

    GolemLocation(String locationName, int headX, int headZ, int headMinY) {
        this.locationName = locationName;
        this.headX = headX;
        this.headZ = headZ;
        this.headMinY = headMinY;
    }

    public String locationName() {
        return locationName;
    }

    public int headX() {
        return headX;
    }

    public int headZ() {
        return headZ;
    }

    public int headMinY() {
        return headMinY;
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull BlockPos headPosMin() {
        return new BlockPos(headX, headMinY, headZ);
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull BlockPos headPosMax() {
        return new BlockPos(headX, headMinY + 4, headZ);
    }

    private @NotNull List<BlockPos> getPossibleHeadPositions() {
        List<BlockPos> positions = new ArrayList<>();

        for (int y = headMinY; y <= headMinY + 4; y++) {
            positions.add(new BlockPos(headX, y, headZ));
        }

        return positions;
    }
}
