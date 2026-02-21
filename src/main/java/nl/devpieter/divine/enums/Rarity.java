package nl.devpieter.divine.enums;

import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Rarity {

    UNKNOWN("Unknown", Formatting.WHITE),
    COMMON("Common", Formatting.WHITE),
    UNCOMMON("Uncommon", Formatting.GREEN),
    RARE("Rare", Formatting.BLUE),
    EPIC("Epic", Formatting.DARK_PURPLE),
    LEGENDARY("Legendary", Formatting.GOLD);

    public static final Map<String, Rarity> COLOR_RARITY_LOOKUP = Arrays.stream(Rarity.values())
            .filter(r -> r != UNKNOWN)
            .collect(Collectors.toMap(r -> r.rarityColor.getName().toLowerCase(), r -> r));

    private final String rarityName;
    private final TextColor rarityColor;

    Rarity(String rarityName, Formatting color) {
        this.rarityName = rarityName;
        this.rarityColor = TextColor.fromFormatting(color);
    }

    public String rarityName() {
        return rarityName;
    }

    public TextColor color() {
        return rarityColor;
    }
}
