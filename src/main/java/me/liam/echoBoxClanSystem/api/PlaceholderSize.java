package me.liam.echoBoxClanSystem.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.liam.echoBoxClanSystem.configs.Config;
import me.liam.echoBoxClanSystem.data.DataManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderSize extends PlaceholderExpansion {

    private final DataManager dataManager;

    public PlaceholderSize(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return Config.getPlaceholderAPIIdentifier();
    }

    @Override
    public @NotNull String getAuthor() {
        return "Sorryplspls";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return null;
        }

        if (identifier.equals(Config.getPlaceholderAPIFormatterSize())) {
            int size = dataManager.getClans().size();
            if(dataManager.getClans() == null){
                return "0";
            }else{
                return Integer.toString(size);
            }
        }

        return null;
    }
}

