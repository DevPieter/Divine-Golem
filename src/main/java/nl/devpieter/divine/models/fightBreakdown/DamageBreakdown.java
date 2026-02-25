package nl.devpieter.divine.models.fightBreakdown;

public record DamageBreakdown(
        MyDamageDetails myDetails,
        double realDps,
        double inGameDps
) {
}
