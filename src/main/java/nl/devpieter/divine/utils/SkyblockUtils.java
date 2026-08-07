package nl.devpieter.divine.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import nl.devpieter.divine.enums.Rarity;
import nl.devpieter.utilize.client.utils.ClientUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SkyblockUtils {

    private SkyblockUtils() {
    }

    public static @NotNull List<? extends ArmorStand> getNearbyArmorStandsWithCustomName(@NotNull BlockPos center, float distance) {
        if (!ClientUtils.hasLevel()) return new ArrayList<>();

        Level level = ClientUtils.getLevel();
        AABB searchBox = new AABB(center).inflate(distance);

        return level.getEntitiesOfClass(ArmorStand.class, searchBox, entity -> {
            if (entity.isRemoved()) return false;
            return entity.hasCustomName();
        });
    }

    public static @NotNull Rarity rarityFromStyle(@Nullable Style style) {
        if (style == null || style.getColor() == null) return Rarity.UNKNOWN;

        String name = style.getColor().serialize();
        return Rarity.COLOR_RARITY_LOOKUP.getOrDefault(name.toLowerCase(), Rarity.UNKNOWN);
    }
}
