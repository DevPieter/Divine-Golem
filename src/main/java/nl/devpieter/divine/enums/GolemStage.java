package nl.devpieter.divine.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum GolemStage {

    UNDEFINED("Undefined", -1),
    RESTING("Resting", 0),
    DORMANT("Dormant", 1),
    AGITATED("Agitated", 2),
    DISTURBED("Disturbed", 3),
    AWAKENING("Awakening", 4),
    SUMMONED("Summoned", 5);

    public static final Map<String, GolemStage> STAGE_LOOKUP = Arrays.stream(GolemStage.values())
            .filter(stage -> stage != UNDEFINED)
            .collect(Collectors.toMap(s -> s.stageName().toLowerCase(), s -> s));

    private final String stageName;
    private final int stageNumber;

    GolemStage(String stageName, int stageNumber) {
        this.stageName = stageName;
        this.stageNumber = stageNumber;
    }

    public String stageName() {
        return stageName;
    }

    public int stageNumber() {
        return stageNumber;
    }
}
