package nl.devpieter.divine.models.fightBreakdown;

import nl.devpieter.divine.models.fightBreakdown.details.*;
import nl.devpieter.divine.utils.TimeUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProtectorFightBreakdown {

    private final boolean hasWitnessedFightStart;
    private final TimingDetails timingDetails;
    private final List<FightDamageEntry> damageEntries = new ArrayList<>();

    private FinalBlowDetails finalBlow;
    private MyDamageDetails myDamage;
    private MyZealotContributionDetails myZealotContribution;

    private DamageBreakdown cachedDamageBreakdown = null;
    private LootQualityBreakdown cachedLootQualityBreakdown = null;
    private TimingBreakdown cachedTimingBreakdown = null;

    public ProtectorFightBreakdown(boolean hasWitnessedFightStart, TimingDetails timingDetails) {
        this.hasWitnessedFightStart = hasWitnessedFightStart;
        this.timingDetails = timingDetails;
    }

    public void setFinalBlow(FinalBlowDetails finalBlow) {
        this.finalBlow = finalBlow;
    }

    public void setMyDamage(MyDamageDetails myDamage) {
        this.myDamage = myDamage;
    }

    public void setMyZealotContribution(MyZealotContributionDetails myZealotContribution) {
        this.myZealotContribution = myZealotContribution;
    }

    public void addDamageEntry(FightDamageEntry damageEntry) {
        this.damageEntries.add(damageEntry);
    }

    public boolean isComplete() {
        return finalBlow != null
                && myDamage != null
                && myZealotContribution != null
                && !damageEntries.isEmpty();
    }

    public boolean hasWitnessedFightStart() {
        return hasWitnessedFightStart;
    }

    public TimingDetails timingDetails() {
        return timingDetails;
    }

    public FinalBlowDetails finalBlow() {
        return finalBlow;
    }

    public MyDamageDetails myDamage() {
        return myDamage;
    }

    public MyZealotContributionDetails myZealotContribution() {
        return myZealotContribution;
    }

    public List<FightDamageEntry> damageEntries() {
        return damageEntries;
    }

    public @Nullable DamageBreakdown calculateDamageBreakdown() {
        if (cachedDamageBreakdown != null) return cachedDamageBreakdown;
        if (!hasWitnessedFightStart()) return null;

        TimingDetails timing = timingDetails();
        MyDamageDetails mDamage = myDamage();
        if (timing == null || mDamage == null) return null;

        long durationRealSeconds = TimeUtils.msToDurationSeconds(timing.fightStartRealTime(), timing.fightEndRealTime());
        long durationInGameSeconds = TimeUtils.ticksToDurationSeconds(timing.fightStartInGameTime(), timing.fightEndInGameTime());

        double realDps = -1;
        if (durationRealSeconds > 0) realDps = (double) mDamage.damage() / durationRealSeconds;

        double inGameDps = -1;
        if (durationInGameSeconds > 0) inGameDps = (double) mDamage.damage() / durationInGameSeconds;

        DamageBreakdown breakdown = new DamageBreakdown(mDamage, realDps, inGameDps);
        cachedDamageBreakdown = breakdown;
        return breakdown;
    }

    public @Nullable LootQualityBreakdown calculateLootQualityBreakdown() {
        if (cachedLootQualityBreakdown != null) return cachedLootQualityBreakdown;

        MyDamageDetails mDamage = myDamage();
        MyZealotContributionDetails zContribution = myZealotContribution();
        if (mDamage == null || zContribution == null || damageEntries().isEmpty()) return null;

        int baseQuality = switch (mDamage.position()) {
            case 1 -> 200;
            case 2 -> 175;
            case 3 -> 150;
            case 4 -> 125;
            case 5 -> 110;
            case 6, 7, 8 -> 100;
            case 9, 10 -> 90;
            case 11, 12 -> 80;
            default -> mDamage.damage() > 0 ? 70 : 10;
        };

        int firstPlaceDamage = damageEntries().stream()
                .filter(entry -> entry.position() == 1)
                .map(FightDamageEntry::damage)
                .findFirst()
                .orElse(0);

        // Safeguard against division by zero
        if (firstPlaceDamage <= 0) firstPlaceDamage = 1;

        double damageRatio = (double) mDamage.damage() / firstPlaceDamage;
        double damageContribution = 50.0 * damageRatio;
        int zealotContribution = zContribution.contribution();

        int finalQuality = (int) Math.round(baseQuality + damageContribution + zealotContribution);
        LootQualityBreakdown breakdown = new LootQualityBreakdown(
                damageContribution,
                zealotContribution,
                baseQuality,
                finalQuality
        );

        cachedLootQualityBreakdown = breakdown;
        return breakdown;
    }

    public @Nullable TimingBreakdown calculateTimingBreakdown() {
        if (cachedTimingBreakdown != null) return cachedTimingBreakdown;
        if (!hasWitnessedFightStart()) return null;

        TimingDetails timing = timingDetails();
        if (timing == null) return null;

        TimingBreakdown breakdown = new TimingBreakdown(
                TimeUtils.msToDurationMillis(timing.fightStartRealTime(), timing.fightEndRealTime()),
                TimeUtils.ticksToDurationMillis(timing.fightStartInGameTime(), timing.fightEndInGameTime()),
                TimeUtils.msToDurationMillis(timing.fightAboutToStartRealTime(), timing.fightStartRealTime()),
                TimeUtils.ticksToDurationMillis(timing.fightAboutToStartInGameTime(), timing.fightStartInGameTime())
        );

        cachedTimingBreakdown = breakdown;
        return breakdown;
    }
}
