package nl.devpieter.divine.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum GolemDropType {

    END_STONE_ROSE("End Stone Rose"),
    ENCHANTED_END_STONE("Enchanted End Stone"),
    CRYSTAL_FRAGMENT("Crystal Fragment"),
    TIER_BOOST_CORE("Tier Boost Core"),
    GOLEM_PET("Golem Pet");

    public static final Map<String, GolemDropType> DROP_LOOKUP = Arrays.stream(GolemDropType.values())
            .collect(Collectors.toMap(d -> d.dropName().toLowerCase(), d -> d));

    private final String dropName;

    GolemDropType(String dropName) {
        this.dropName = dropName;
    }

    public String dropName() {
        return dropName;
    }
}
