package nl.devpieter.divine.models;

import nl.devpieter.divine.enums.GolemDropType;
import nl.devpieter.divine.enums.Rarity;
import org.jetbrains.annotations.NotNull;

public record GolemDrop(
        GolemDropType type,
        Rarity rarity,
        int quantity
) {

    // TODO - For testing only, remove later
    @Override
    public @NotNull String toString() {
        return String.format("%s x%d [%s]", type.dropName(), quantity, rarity.rarityName());
    }
}
