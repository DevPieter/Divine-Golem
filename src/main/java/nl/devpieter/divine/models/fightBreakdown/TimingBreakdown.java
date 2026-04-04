package nl.devpieter.divine.models.fightBreakdown;

public record TimingBreakdown(
        long fightDurationRealMillis,
        long fightDurationInGameMillis,
        long timeBeforeSpawnRealMillis,
        long timeBeforeSpawnInGameMillis
) {
}
