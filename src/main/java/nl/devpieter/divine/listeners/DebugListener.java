package nl.devpieter.divine.listeners;

import nl.devpieter.divine.events.skyblock.SkyblockLocationUpdateEvent;
import nl.devpieter.divine.events.skyblock.protector.*;
import nl.devpieter.sees.annotations.SEventListener;
import nl.devpieter.sees.listener.SListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugListener implements SListener {

    private final Logger logger = LoggerFactory.getLogger(DebugListener.class);

    @SEventListener
    private void onProtectorAboutToSpawn(ProtectorAboutToSpawnEvent event) {
        logger.info("Protector is about to spawn! Predicted spawn time: {} ms", event.predictedSpawnTimeMillis());
    }

    @SEventListener
    private void onProtectorFightEnd(ProtectorFightEndEvent event) {
        logger.info("Protector fight ended! Canceled: {}", event.canceled());
    }

    @SEventListener
    private void onProtectorFightStart(ProtectorFightStartEvent event) {
        logger.info("Protector fight started!");
    }

    @SEventListener
    private void onProtectorLocationUpdate(ProtectorLocationUpdateEvent event) {
        logger.info("Protector location updated! Previous location: {}, New location: {}", event.previousLocation(), event.location());
    }

    @SEventListener
    private void onProtectorMilestoneReached(ProtectorMilestoneReachedEvent event) {
        logger.info("Protector milestone reached!");
    }

    @SEventListener
    private void onProtectorStageUpdate(ProtectorStageUpdateEvent event) {
        logger.info("Protector stage updated! Previous stage: {}, New stage: {}", event.previousStage(), event.stage());
    }

    @SEventListener
    private void onSkyblockLocationUpdate(SkyblockLocationUpdateEvent event) {
        logger.info("Skyblock location updated! Previous location: {}, New location: {}", event.previousLocation(), event.location());
    }
}
