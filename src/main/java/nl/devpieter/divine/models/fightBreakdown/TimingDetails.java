package nl.devpieter.divine.models.fightBreakdown;

public record TimingDetails(
        long fightAboutToStartRealTime,
        long fightAboutToStartInGameTime,
        long fightStartRealTime,
        long fightStartInGameTime,
        long fightEndRealTime,
        long fightEndInGameTime
) {
    public boolean hasAboutToStartTiming() {
        return fightAboutToStartRealTime != -1 && fightAboutToStartInGameTime != -1;
    }

    public boolean hasFightStartTiming() {
        return fightStartRealTime != -1 && fightStartInGameTime != -1;
    }

    public boolean hasFightEndTiming() {
        return fightEndRealTime != -1 && fightEndInGameTime != -1;
    }
}
