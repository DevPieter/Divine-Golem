package nl.devpieter.divine.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import nl.devpieter.divine.enums.GolemLocation;
import nl.devpieter.divine.enums.GolemStage;
import nl.devpieter.utilize.utils.minecraft.WorldUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

public class SkyblockUtils {

    private static final Pattern PROTECTOR_PATTERN = Pattern.compile("Protector:\\s*(\\w+)");

    private SkyblockUtils() {
    }

    public static @Nullable GolemStage getCurrentGolemStage(@NotNull List<Text> playerListDisplayNames) {
        for (Text displayName : playerListDisplayNames) {
            if (displayName == null) continue;

            String stageKey = RegexUtils.findFirstGroup(PROTECTOR_PATTERN, displayName.getString());
            if (stageKey == null) continue;

            return GolemStage.STAGE_LOOKUP.getOrDefault(stageKey.toLowerCase(), GolemStage.UNDEFINED);
        }

        return null;
    }

    public static @Nullable GolemLocation getCurrentGolemLocation() {
        for (GolemLocation location : GolemLocation.values()) {
            if (location == GolemLocation.UNDEFINED) continue;

            List<BlockPos> headPositions = GolemLocation.LOCATION_HEAD_POSITIONS.get(location);
            if (headPositions == null) continue;

            for (BlockPos headPos : headPositions) {
                Block block = WorldUtils.getBlockAt(headPos);
                if (block == null || block != Blocks.PLAYER_HEAD) continue;

                return location;
            }
        }

        return GolemLocation.UNDEFINED;
    }
}
