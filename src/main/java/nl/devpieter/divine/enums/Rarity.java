package nl.devpieter.divine.enums;

import net.minecraft.network.chat.TextColor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Rarity {

    UNKNOWN("Unknown", TextColor.WHITE),
    COMMON("Common", TextColor.WHITE),
    UNCOMMON("Uncommon", TextColor.GREEN),
    RARE("Rare", TextColor.BLUE),
    EPIC("Epic", TextColor.DARK_PURPLE),
    LEGENDARY("Legendary", TextColor.GOLD);

    public static final Map<String, Rarity> COLOR_RARITY_LOOKUP = Arrays.stream(Rarity.values())
            .filter(r -> r != UNKNOWN)
            .collect(Collectors.toMap(r -> r.rarityColor.serialize().toLowerCase(), r -> r));

    private final String rarityName;
    private final TextColor rarityColor;

    Rarity(String rarityName, TextColor color) {
        this.rarityName = rarityName;
        this.rarityColor = color;
    }

    public String rarityName() {
        return rarityName;
    }

    public TextColor color() {
        return rarityColor;
    }
}
