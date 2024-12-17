package me.liam.echoBoxClanSystem.configs;

import me.liam.echoBoxClanSystem.Main;
import me.liam.echoBoxClanSystem.colors.Adventure;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Config {

    public static void initialize(JavaPlugin plugin) {
        Main.getInstance().saveDefaultConfig();
    }

    public static String getConfigPrefix(){
        return Main.getInstance().getConfig().getString("plugin-prefix");
    }

    public static boolean isDebugEnabled() {
        return Main.getInstance().getConfig().getBoolean("debug");
    }

    public static boolean isAutomaticSavingEnabled() {
        return Main.getInstance().getConfig().getBoolean("automatic-saving");
    }

    public static boolean isLogsEnabled() {
        return Main.getInstance().getConfig().getBoolean("logs");
    }

    public static boolean isFilterDetectionEnabled() {
        return Main.getInstance().getConfig().getBoolean("filter-detection");
    }

    public static int getMaxClanMembers() {
        return Main.getInstance().getConfig().getInt("max-clan-members");
    }

    public static int getMaxClanAdmins() {
        return Main.getInstance().getConfig().getInt("max-clan-admins");
    }

    public static int getMaxClanCoOwners() {
        return Main.getInstance().getConfig().getInt("max-clan-co-owners");
    }

    public static int getMaxClanNameLength(){
        return Main.getInstance().getConfig().getInt("max-length-on-clan-names");
    }

    public static int getMaxInvitesAtOnce() {
        return Main.getInstance().getConfig().getInt("max-clan-invites-at-once");
    }

    public static boolean isClanChestEnabled() {
        return Main.getInstance().getConfig().getBoolean("clanchest");
    }

    public static boolean isLeaderboardsEnabled() {
        return Main.getInstance().getConfig().getBoolean("clan-automatic-leaderboards");
    }

    public static boolean isClanChatEnabled() {
        return Main.getInstance().getConfig().getBoolean("clanchat");
    }

    public static String getOwnerPrefix(){
        return Main.getInstance().getConfig().getString("clan-owner");
    }

    public static String getCoOwnerPrefix(){
        return Main.getInstance().getConfig().getString("clan-co-owner");
    }

    public static String getGuiPreference(){
        return Main.getInstance().getConfig().getString("gui-preference");
    }

    public static boolean isDecorationEnabled(){
        return Main.getInstance().getConfig().getBoolean("gui-decoration");
    }

    public static String getDecorationType(){
        return Main.getInstance().getConfig().getString("gui-decoration-type");
    }

    public static String getAdminPrefix(){
        return Main.getInstance().getConfig().getString("clan-admin");
    }

    public static String getMemberPrefix(){
        return Main.getInstance().getConfig().getString("clan-member");
    }

    public static String getNoRankPrefix(){
        return Main.getInstance().getConfig().getString("clan-no-rank-found-error");
    }

    public static boolean isPlaceholderAPIEnabled(){
        return Main.getInstance().getConfig().getBoolean("placeholderapi");
    }

    public static String getPlaceholderAPIIdentifier(){
        return Main.getInstance().getConfig().getString("placeholder-identifier");
    }

    public static String getPlaceholderAPIFormatterName(){
        return Main.getInstance().getConfig().getString("placeholder-format-playerclan");
    }

    public static String getPlaceholderAPIFormatterSize(){
        return Main.getInstance().getConfig().getString("placeholder-format-clansize");
    }

    public static boolean isReloadConfigCmdEnabled(){
        return Main.getInstance().getConfig().getBoolean("reload-config-cmd");
    }

    public static String getAdminGuiName(){
        return Main.getInstance().getConfig().getString("admin-gui-name");
    }

    public static String getBackgroundDecorationText(){
        return Main.getInstance().getConfig().getString("background-decoration-unicode");
    }

    public static String getClanGuiName(){
        return Main.getInstance().getConfig().getString("clan-gui-name");
    }

    public static boolean isBroadcastEnabled(){
        return Main.getInstance().getConfig().getBoolean("broadcast");
    }

    public static int getPointsPerKill(){
        return Main.getInstance().getConfig().getInt("points-give-per-kill");
    }

    public static int getPointsPerDeath(){
        return Main.getInstance().getConfig().getInt("points-reduce-per-death");
    }

    public static boolean isCooldownEnabled(){
        return Main.getInstance().getConfig().getBoolean("cooldowns");
    }

    public static boolean isCancelSpacesEnabled(){
        return Main.getInstance().getConfig().getBoolean("cancel-spaces");
    }

    public static boolean isEulaChecked(){
        return Main.getInstance().getConfig().getBoolean("eula");
    }

    public static String getMessageNotInClan() {
        return Main.getInstance().getConfig().getString("messages.error_not_in_clan");
    }

    public static String getMessageHigherRanked() {
        return Main.getInstance().getConfig().getString("messages.error_higher_rank_needed");
    }

    public static String getMessageNoPendingInvites() {
        return Main.getInstance().getConfig().getString("messages.error_no_pending_invites");
    }

    public static String getMessageNoClanFound() {
        return Main.getInstance().getConfig().getString("messages.error_no_clan_found");
    }

    public static String getMessageInvalidExecutor() {
        return Main.getInstance().getConfig().getString("messages.error_invalid_executor");
    }

    public static String getMessageInappropiateLanguage() {
        return Main.getInstance().getConfig().getString("messages.error_inappropiate_language");
    }

    public static String getMessageClanAlreadyExists() {
        return Main.getInstance().getConfig().getString("messages.error_clan_already_exists");
    }

    public static String getMessageClanNeedMorePoints() {
        return Main.getInstance().getConfig().getString("messages.error_clan_need_more_points");
    }

    public static String getMessageCooldownANNOUNCEMENT() {
        return Main.getInstance().getConfig().getString("messages.error_you_are_on_cooldown_CLAN_ANNOUNCEMENT");
    }

    public static String getMessageCooldownRENAMES() {
        return Main.getInstance().getConfig().getString("messages.error_you_are_on_cooldown_CLAN_RENAMES");
    }

    public static String getMessageCooldownCLANCREATE() {
        return Main.getInstance().getConfig().getString("messages.error_you_are_on_cooldown_CLAN_CREATIONS");
    }

    public static String getMessageNotOwnerOfClan() {
        return Main.getInstance().getConfig().getString("messages.error_not_the_owner");
    }

    public static String getMessagePendingInvitations() {
        return Main.getInstance().getConfig().getString("messages.pending_invites");
    }

    public static String getMessageAlreadyInClan() {
        return Main.getInstance().getConfig().getString("messages.error_already_in_clan");
    }

    public static String getMessageWaitingForRename() {
        return Main.getInstance().getConfig().getString("messages.waiting_for_rename");
    }

    public static String getMessageWaitingForName() {
        return Main.getInstance().getConfig().getString("messages.waiting_for_name");
    }

    public static String getMessageConfirmDeletion() {
        return Main.getInstance().getConfig().getString("messages.confirm_deletion");
    }

    public static String getMessageClanPoints() {
        return Main.getInstance().getConfig().getString("messages.clan_points");
    }

    public static String getMessageLeaveWhileOwner() {
        return Main.getInstance().getConfig().getString("messages.error_cant_leave_while_owner");
    }

    public static String getMessageLeftClan() {
        return Main.getInstance().getConfig().getString("messages.left_clan");
    }

    public static String getMessageNotificationLeftClan() {
        return Main.getInstance().getConfig().getString("messages.notify_left");
    }

    public static String getMessageFailedToLeaveClan() {
        return Main.getInstance().getConfig().getString("messages.error_failed_to_leave_clan");
    }

    public static String getMessageLogoSetting() {
        return Main.getInstance().getConfig().getString("messages.logo_setting");
    }

    public static String getMessageLogoSet() {
        return Main.getInstance().getConfig().getString("messages.logo_set");
    }

    public static String getMessageClanNameEmpty() {
        return Main.getInstance().getConfig().getString("messages.error_clan_name_empty");
    }

    public static String getMessageClanNameTooLong() {
        return Main.getInstance().getConfig().getString("messages.error_clan_name_too_long");
    }

    public static String getMessageClanNameGotSpecialCharacters() {
        return Main.getInstance().getConfig().getString("messages.error_clan_name_contain_special_characters");
    }

    public static String getMessageClanNameContainSpaces() {
        return Main.getInstance().getConfig().getString("messages.error_clan_name_contain_spaces");
    }

    public static String getMessageMadeClan() {
        return Main.getInstance().getConfig().getString("messages.made_clan");
    }

    public static String getMessageEarnedPoints() {
        return Main.getInstance().getConfig().getString("messages.earned_point");
    }

    public static String getMessageDeletedClan() {
        return Main.getInstance().getConfig().getString("messages.deleted_clan");
    }

    public static String getMessageError() {
        return Main.getInstance().getConfig().getString("messages.error");
    }

    public static String getMessageFailedToDelete() {
        return Main.getInstance().getConfig().getString("messages.failed_to_delete");
    }

    public static String getMessageTransferShipThemself() {
        return Main.getInstance().getConfig().getString("messages.cancel_ownership_transfer_1");
    }

    public static String getMessageTransferShipNotFound() {
        return Main.getInstance().getConfig().getString("messages.cancel_ownership_transfer_2");
    }

    public static String getMessageTransferLeft() {
        return Main.getInstance().getConfig().getString("messages.cancel_ownership_transfer_3");
    }

    public static String getMessageOfferedOwnership() {
        return Main.getInstance().getConfig().getString("messages.offered_ownership_transfer");
    }

    public static String getMessageSentOwnershipTransfer() {
        return Main.getInstance().getConfig().getString("messages.sent_ownership_transfer");
    }

    public static String getMessageAcceptedOwnership() {
        return Main.getInstance().getConfig().getString("messages.accepted_ownership_transfer");
    }

    public static String getMessageOwnershipTransferred() {
        return Main.getInstance().getConfig().getString("messages.ownership_transfered");
    }

    public static String getMessageDeniedOwnershipTransfer() {
        return Main.getInstance().getConfig().getString("messages.denied_ownership_transfer");
    }

    public static String getMessageProcessingTransfer() {
        return Main.getInstance().getConfig().getString("messages.processing_transfership");
    }

    public static String getMessageNotifyOwnerTransfer() {
        return Main.getInstance().getConfig().getString("messages.notify_other_owner_transfer");
    }

    public static String getMessageRenamedClan() {
        return Main.getInstance().getConfig().getString("messages.renamed_clan");
    }

    public static String getMessageConfigReloaded() {
        return Main.getInstance().getConfig().getString("messages.config_reloaded");
    }

    public static String getMessageConfigDisabledFeature() {
        return Main.getInstance().getConfig().getString("messages.config_disabled_feature");
    }

    public static String getMessageClanChatDisabled() {
        return Main.getInstance().getConfig().getString("messages.clanchat_disabled");
    }

    public static String getMessageClanChatEnabled() {
        return Main.getInstance().getConfig().getString("messages.clanchat_enabled");
    }

    public static String getMessageNoPermission() {
        return Main.getInstance().getConfig().getString("messages.no_permission");
    }

    public static String getMessageDataRefreshed() {
        return Main.getInstance().getConfig().getString("messages.data_refreshed");
    }

    public static String getMessageFewArguments() {
        return Main.getInstance().getConfig().getString("messages.few_arguments");
    }

    public static String getMessageAlreadyClanned() {
        return Main.getInstance().getConfig().getString("messages.error_already_clanned");
    }

    public static String getMessageClanMaxed() {
        return Main.getInstance().getConfig().getString("messages.error_clan_maxed");
    }

    public static String getMessagePlayerIsAlreadyClanned() {
        return Main.getInstance().getConfig().getString("messages.error_player_already_clanned");
    }

    public static String getMessageFailedMentionedYourself() {
        return Main.getInstance().getConfig().getString("messages.error_mentioned_yourself");
    }

    public static String getMessageAlreadyMember() {
        return Main.getInstance().getConfig().getString("messages.error_already_member");
    }

    public static String getMessagePlayerMaxedInvitations() {
        return Main.getInstance().getConfig().getString("messages.error_maxed_invitations");
    }

    public static String getMessagePlayerIsAlreadyInvited() {
        return Main.getInstance().getConfig().getString("messages.error_already_invited");
    }

    public static String getMessageKickSuccesful() {
        return Main.getInstance().getConfig().getString("messages.kick_successful");
    }

    public static String kick_notifiy() {
        return Main.getInstance().getConfig().getString("messages.kick_notify");
    }

    public static String getMessageNoPlayerFound() {
        return Main.getInstance().getConfig().getString("messages.no_player_found");
    }

    public static String getMessageInvitedPlayer() {
        return Main.getInstance().getConfig().getString("messages.invited_player");
    }

    public static String getMessageYouWereInvited() {
        return Main.getInstance().getConfig().getString("messages.you_were_invited");
    }

    public static String getMessageYouWereInvitedAcceptMethod() {
        return Main.getInstance().getConfig().getString("messages.you_were_invited1");
    }

    public static String getMessageFriendlyFire() {
        return Main.getInstance().getConfig().getString("messages.friendlyfire");
    }


    public static String getMessageClanAnnouncement() {
        return Main.getInstance().getConfig().getString("messages.clan_announcement");
    }

    public static String getMessageJoinedClan() {
        return Main.getInstance().getConfig().getString("messages.joined_clan");
    }
    public static String getStringSTATUS() {
        String status = Main.getInstance().getConfig().getString("messages.status");
        return (status != null) ? Adventure.parseToLegacy(status) : "";
    }

    public static String getStringONLINE() {
        String online = Main.getInstance().getConfig().getString("messages.online");
        return (online != null) ? Adventure.parseToLegacy(online) : "";
    }

    public static String getStringOFFLINE() {
        String offline = Main.getInstance().getConfig().getString("messages.offline");
        return (offline != null) ? Adventure.parseToLegacy(offline) : "";
    }

    public static String getStringNODATA() {
        String noData = Main.getInstance().getConfig().getString("messages.no_data");
        return (noData != null) ? Adventure.parseToLegacy(noData) : "";
    }

    public static Boolean isDiscordEnabled(){
        return Main.getInstance().getConfig().getBoolean("discord");
    }

    public static String getStringLAST_ONLINE(Integer days, Integer hours, Integer minutes) {
        if (days == null || hours == null || minutes == null) {
            return ChatColor.translateAlternateColorCodes('&', Config.getStringNODATA());
        }

        String format = Main.getInstance().getConfig().getString("messages.last_online");

        if (format == null) {
            format = Config.getStringNODATA();
        }

        String message = format
                .replace("{days}", String.valueOf(days))
                .replace("{hours}", String.valueOf(hours))
                .replace("{minutes}", String.valueOf(minutes));

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getMessageClanColorSet() {
        return Main.getInstance().getConfig().getString("messages.clan_color_set");
    }

    public static String getMessageClanColorNotRanked() {
        return Main.getInstance().getConfig().getString("messages.clan_color_not_ranked");
    }

    public static void reloadConfig(){
        Main.getInstance().reloadConfig();
    }

    public static int getSlot(String path) {
        FileConfiguration config = Main.getInstance().getConfig();
        return config.getInt(path + ".slot");
    }

    public static ItemStack getItemStack(String path, Material defaultMaterial) {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            Main.getInstance().getLogger().warning("Could not find '" + path + "' in the config.yml!");
            return new ItemStack(Material.BARRIER);
        }

        String type = section.getString("type", defaultMaterial.name()).toUpperCase();
        Material material = Material.matchMaterial(type);

        if (material == null) {
            Main.getInstance().getLogger().warning("Invalid material '" + type + "' in '" + path + "'. Using default: " + defaultMaterial.name());
            material = defaultMaterial;
        }

        ItemStack item = new ItemStack(material, 1);

        if (section.isConfigurationSection("view")) {
            ConfigurationSection metaSection = section.getConfigurationSection("view");
            ItemMeta meta = item.getItemMeta();
            if (meta != null && metaSection != null) {
                if (metaSection.contains("name")) {
                    String displayName = Objects.requireNonNull(metaSection.getString("name")).replace("&", "§");
                    String adventureDisplay = Adventure.parseToLegacy(displayName);
                    meta.setDisplayName(adventureDisplay);
                }

                if (metaSection.contains("lore")) {
                    List<String> lore = metaSection.getStringList("lore").stream()
                            .map(line -> line.replace("&", "§"))
                            .map(line -> line.replace("{version}", Main.getInstance().getDescription().getVersion()))
                            .map(Adventure::parseToLegacy)
                            .collect(Collectors.toList());
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    meta.setLore(lore);
                }

                item.setItemMeta(meta);
            }
        }

        return item;
    }
}
