package nl.devpieter.divine.models.fightBreakdown;

import nl.devpieter.divine.models.fightBreakdown.details.MyDamageDetails;

public record DamageBreakdown(
        MyDamageDetails myDetails,
        double realDps,
        double inGameDps
) {
}
