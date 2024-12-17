package me.liam.echoBoxClanSystem.colors;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class GradientHelper {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Parses MiniMessage format into a Component for use with Adventure.
     *
     * @param miniMessageString The string containing MiniMessage formatting.
     * @return A Component that can be displayed to players.
     */


    public static Component parseMiniMessage(String miniMessageString) {
        return miniMessage.deserialize(miniMessageString);
    }

}
