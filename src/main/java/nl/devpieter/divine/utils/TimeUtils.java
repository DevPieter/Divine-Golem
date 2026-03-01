package nl.devpieter.divine.utils;

public class TimeUtils {

    public static long ticksToMillis(long ticks) {
        return ticks * 50;
    }

    public static long msToDurationMillis(long startTime, long endTime) {
        if (startTime == -1 || endTime == -1) return -1;
        return Math.max(endTime - startTime, 0);
    }

    public static long ticksToDurationMillis(long startTime, long endTime) {
        return msToDurationMillis(ticksToMillis(startTime), ticksToMillis(endTime));
    }

    public static long msToDurationSeconds(long startTime, long endTime) {
        long durationMillis = msToDurationMillis(startTime, endTime);
        if (durationMillis == -1) return -1;

        return Math.max(durationMillis / 1000, 1);
    }

    public static long ticksToDurationSeconds(long startTime, long endTime) {
        return msToDurationSeconds(ticksToMillis(startTime), ticksToMillis(endTime));
    }

    // Format as "M:SS"
    public static String formatDurationMSS(long durationMillis) {
        if (durationMillis == -1) return "N/A";

        long seconds = durationMillis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    // Format as "SS.mmm"
    public static String formatDurationSSmmm(long durationMillis) {
        if (durationMillis == -1) return "N/A";

        long seconds = durationMillis / 1000;
        long milliseconds = durationMillis % 1000;

        return String.format("%d.%03d", seconds, milliseconds);
    }
}
