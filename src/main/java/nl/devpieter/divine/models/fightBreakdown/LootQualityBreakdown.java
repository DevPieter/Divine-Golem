package nl.devpieter.divine.models.fightBreakdown;

public record LootQualityBreakdown(
        double damageContribution,
        int zealotContribution,
        int baseQuality,
        int finalQuality
) {
}
