package nl.devpieter.divine.models.fightBreakdown;

import java.util.ArrayList;
import java.util.List;

public class ProtectorFightBreakdown {

    private final boolean hasWitnessedFightStart;
    private final TimingDetails timingDetails;
    private final List<FightDamageEntry> damageEntries = new ArrayList<>();

    private FinalBlowDetails finalBlow;
    private MyDamageDetails myDamage;
    private MyZealotContributionDetails myZealotContribution;

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
}
