package nl.devpieter.divine.models;

import nl.devpieter.divine.enums.GolemDropType;
import nl.devpieter.divine.enums.Rarity;
import org.jetbrains.annotations.NotNull;

public record GolemDrop(
        GolemDropType type,
        Rarity rarity,
        int quantity
) {
}
