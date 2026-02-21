package nl.devpieter.divine.utils;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Style;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import nl.devpieter.divine.enums.Rarity;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SkyblockUtils {

    private SkyblockUtils() {
    }

    public static @NotNull List<? extends ArmorStandEntity> getNearbyArmorStandsWithCustomName(@NotNull BlockPos center, float distance) {
        if (!ClientUtils.hasWorld()) return new ArrayList<>();

        World world = ClientUtils.getWorld();
        Box searchBox = new Box(center).expand(distance);

        return world.getEntitiesByClass(ArmorStandEntity.class, searchBox, entity -> {
            if (entity.isRemoved()) return false;
            return entity.hasCustomName();
        });
    }

    public static @NotNull Rarity rarityFromStyle(@Nullable Style style) {
        if (style == null || style.getColor() == null) return Rarity.UNKNOWN;

        String name = style.getColor().getName();
        if (name == null) return Rarity.UNKNOWN;

        return Rarity.COLOR_RARITY_LOOKUP.getOrDefault(name.toLowerCase(), Rarity.UNKNOWN);
    }
}
