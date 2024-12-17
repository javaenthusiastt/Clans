package me.liam.echoBoxClanSystem.configs;

import me.liam.echoBoxClanSystem.colors.Adventure;
import me.liam.echoBoxClanSystem.helpers.Prefixes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadConfigClass
{

    public static void reload(Player player, String prefix) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageConfigReloaded())));
        Config.reloadConfig();
        player.closeInventory();
    }
}
