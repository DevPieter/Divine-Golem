package nl.devpieter.divine.models.fightBreakdown;

public record TimingBreakdown(
        long fightDurationRealMilliseconds,
        long fightDurationInGameMilliseconds,
        long timeBeforeSpawnRealMilliseconds,
        long timeBeforeSpawnInGameMilliseconds
) {
}
