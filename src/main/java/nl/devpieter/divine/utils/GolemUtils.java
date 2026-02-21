package nl.devpieter.divine.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import nl.devpieter.divine.enums.GolemDropType;
import nl.devpieter.divine.enums.GolemLocation;
import nl.devpieter.divine.enums.GolemStage;
import nl.devpieter.divine.enums.Rarity;
import nl.devpieter.divine.models.GolemDrop;
import nl.devpieter.utilize.utils.minecraft.WorldUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GolemUtils {

    private static final Pattern PROTECTOR_PATTERN = Pattern.compile("Protector:\\s*(\\w+)");

    private GolemUtils() {
    }

    public static @Nullable GolemStage getCurrentStage(@NotNull List<Text> playerListDisplayNames) {
        for (Text displayName : playerListDisplayNames) {
            if (displayName == null) continue;

            String stageKey = RegexUtils.findFirstGroup(PROTECTOR_PATTERN, displayName.getString());
            if (stageKey == null) continue;

            return GolemStage.STAGE_LOOKUP.getOrDefault(stageKey.toLowerCase(), GolemStage.UNDEFINED);
        }

        return null;
    }

    public static @NotNull GolemLocation getCurrentLocation() {
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

    // empty[style={!italic}, siblings=[literal{Crystal Fragment }[style={color=dark_purple}], literal{x2}[style={color=dark_gray}]]]
    // empty[style={!italic}, siblings=[literal{End Stone Rose}[style={color=blue}]]]
    // empty[style={!italic}, siblings=[literal{Enchanted End Stone }[style={color=green}], literal{x10}[style={color=dark_gray}]]]

    public static @NotNull List<GolemDrop> findDrops(GolemLocation location) {
        List<? extends ArmorStandEntity> armorStands = SkyblockUtils.getNearbyArmorStandsWithCustomName(location.headPosMin(), 8);
        if (armorStands.isEmpty()) return new ArrayList<>();

        List<GolemDrop> drops = new ArrayList<>();

        for (ArmorStandEntity armorStand : armorStands) {
            Text name = armorStand.getCustomName();
            if (name == null) continue;

            List<Text> siblings = name.getSiblings();
            if (siblings.isEmpty()) continue;

            Text firstSibling = siblings.getFirst();
            if (firstSibling == null) continue;

            GolemDropType dropType = dropTypeFromName(firstSibling.getString().trim());
            if (dropType == null) continue;

            Text secondSibling = siblings.size() < 2 ? null : siblings.get(1);

            String quantityStr = secondSibling == null ? null : secondSibling.getString().trim();
            int quantity = dropQuantityFromString(quantityStr);

            Rarity rarity = SkyblockUtils.rarityFromStyle(firstSibling.getStyle());
            drops.add(new GolemDrop(dropType, rarity, quantity));
        }

        return drops;
    }

    private static @Nullable GolemDropType dropTypeFromName(String name) {
        return GolemDropType.DROP_LOOKUP.get(name.toLowerCase());
    }

    private static int dropQuantityFromString(String quantityStr) {
        if (quantityStr == null || !quantityStr.startsWith("x")) return 1;

        try {
            return Integer.parseInt(quantityStr.substring(1));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
