package me.liam.echoBoxClanSystem.handling;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Map<String, Cooldown>> playerCooldowns = new HashMap<>();

    public void setCooldown(UUID playerId, Duration duration, String reason) {
        playerCooldowns.computeIfAbsent(playerId, k -> new HashMap<>())
                .put(reason, new Cooldown(duration, reason));
    }

    public boolean isOnCooldown(UUID playerId, String reason) {
        Map<String, Cooldown> cooldowns = playerCooldowns.get(playerId);
        if (cooldowns == null) {
            return false;
        }
        Cooldown cooldown = cooldowns.get(reason);
        return cooldown != null && cooldown.getEndTime().isAfter(Instant.now());
    }

    public Duration getRemainingCooldown(UUID playerId, String reason) {
        Map<String, Cooldown> cooldowns = playerCooldowns.get(playerId);
        if (cooldowns == null) {
            return Duration.ZERO;
        }
        Cooldown cooldown = cooldowns.get(reason);
        if (cooldown == null) {
            return Duration.ZERO;
        }
        return Duration.between(Instant.now(), cooldown.getEndTime());
    }

    private static class Cooldown {
        private final Instant endTime;
        private final String reason;

        public Cooldown(Duration duration, String reason) {
            this.endTime = Instant.now().plus(duration);
            this.reason = reason;
        }

        public Instant getEndTime() {
            return endTime;
        }

    }
}




