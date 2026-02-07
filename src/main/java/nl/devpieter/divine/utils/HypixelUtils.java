package nl.devpieter.divine.utils;

import nl.devpieter.divine.models.HypixelLocation;

import java.util.regex.Pattern;

public class HypixelUtils {

    private static final Pattern HYPIXEL_IP_PATTERN = Pattern.compile("^(?:.+\\.)?hypixel\\.net(?::\\d+)?$", Pattern.CASE_INSENSITIVE);

    private static final Pattern SERVER_PATTERN = Pattern.compile("\"server\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern GAMETYPE_PATTERN = Pattern.compile("\"gametype\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern MAP_PATTERN = Pattern.compile("\"map\"\\s*:\\s*\"([^\"]+)\"");

    private HypixelUtils() {
    }

    public static boolean isHypixelServer(String ip) {
        return ip != null && RegexUtils.matches(HYPIXEL_IP_PATTERN, ip.trim());
    }

    public static HypixelLocation parseLocRaw(String message) {
        if (message == null || message.isBlank()) return null;

        String trimmed = message.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) return null;

        String server = RegexUtils.findFirstGroup(SERVER_PATTERN, trimmed);
        String gametype = RegexUtils.findFirstGroup(GAMETYPE_PATTERN, trimmed);
        String map = RegexUtils.findFirstGroup(MAP_PATTERN, trimmed);

        if (server == null || gametype == null) return null;
        return new HypixelLocation(server, gametype, map);
    }
}
