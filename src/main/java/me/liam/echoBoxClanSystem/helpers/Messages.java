package me.liam.echoBoxClanSystem.helpers;

import me.liam.echoBoxClanSystem.colors.Adventure;
import me.liam.echoBoxClanSystem.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {



    public static void sendClanNeeded(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageNotInClan())));
    }

    public static void sendHigherRank(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageHigherRanked())));
    }

    public static void sendNoPendingInvites(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageNoPendingInvites())));
    }

    public static void sendNoClanFound(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageNoClanFound())));
    }

    public static void sendInappropiateLanguage(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageInappropiateLanguage())));
    }

    public static void sendClanAlreadyExists(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanAlreadyExists())));
    }

    public static void sendClanNeedMorePoints(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanNeedMorePoints())));
    }

    public static void sendYouAreOnCooldownCLANANNOUNCEMENT(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageCooldownANNOUNCEMENT())));
    }

    public static void sendYouAreOnCooldownRENAMES(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageCooldownRENAMES())));
    }

    public static void sendYouAreOnCooldownCREATIONS(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageCooldownCLANCREATE())));
    }

    public static void sendYouAreNotTheOwnerOfClan(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageNotOwnerOfClan())));
    }

    public static void sendYouAreAlreadyInAClan(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageAlreadyInClan())));
    }

    public static void sendWaitingForRename(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageWaitingForRename())));
    }

    public static void sendWaitingForName(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageWaitingForName())));
    }

    public static void sendConfirmDeletion(Player player, String clan) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageConfirmDeletion().replace("{get_clan}", clan))));
    }

    public static void sendPlayerClanPoints(Player player, int points) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanPoints().replace("{get_points}", String.valueOf(points)))));
    }

    public static void sendCantLeaveWhileOwner(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageLeaveWhileOwner())));
    }

    public static void sendLeftClan(Player player, String clanName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageLeftClan().replace("{get_clan}", clanName))));

    }

    public static void sendNotifyLeftClan(Player player, String playerName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageNotificationLeftClan().replace("{player}", playerName))));
    }

    public static void sendFailedToLeaveClan(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageFailedToLeaveClan())));
    }

    public static void sendHowToSetLogo(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageLogoSetting())));

    }

    public static void sendLogoSet(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageLogoSet())));
    }

    public static void sendClanNameEmpty(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanNameEmpty())));
    }

    public static void sendClanNameTooLong(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanNameTooLong())));
    }

    public static void sendClanNameContainSpecialCharacters(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanNameGotSpecialCharacters())));
    }

    public static void SendClanNameContainSpaces(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanNameContainSpaces())));
    }

    public static void sendEarnedPoint(Player player, String point) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageEarnedPoints().replace("{get_points}", String.valueOf(point)))));
    }

    public static void sendDeletedClan(Player player, String clanName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageDeletedClan().replace("{get_clan}", clanName))));
    }

    public static void sendError(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageError())));
    }

    public static void sendFailedToDeleteClan(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageFailedToDelete())));
    }

    public static void sendTransferFailedToThemself(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageTransferShipThemself())));
    }

    public static void sendTransferFailedNotFound(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageTransferShipNotFound())));
    }

    public static void sendTransferFailedNewOwnerLeft(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageTransferLeft())));
    }

    public static void sendTransferOffered(Player player, String clanName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageOfferedOwnership().replace("{get_clan}", clanName))));
    }

    public static void sendOwnershipRequestSent(Player player, String playerName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageSentOwnershipTransfer().replace("{player}", playerName))));
    }

    public static void sendAcceptedOwnership(Player player, String clanName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageAcceptedOwnership().replace("{get_clan}", clanName))));
    }

    public static void sendKickNotify(Player player, String clanName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.kick_notifiy().replace("{get_clan}", clanName))));
    }

    public static void sendKickSuccesful(Player player, String targetName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageKickSuccesful().replace("{target}", targetName))));
    }

    public static void sendOwnershipTransferred(Player player, String clanName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageOwnershipTransferred().replace("{get_clan}", clanName))));
    }

    public static void sendAnnouncement(String clanName, String playerName) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanAnnouncement().replace("{get_clan}", clanName).replace("{player}", playerName))));
    }

    public static void sendDeniedOwnership(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageDeniedOwnershipTransfer())));
    }

    public static void sendProcessingTransfer(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageProcessingTransfer())));
    }

    public static void sendNotifyDeniedOwnership(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageNotifyOwnerTransfer())));
    }

    public static void sendConfigDisabledFeature(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageConfigDisabledFeature())));
    }

    public static void sendClanColorSet(Player player, ChatColor color, String selectedName) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanColorSet().replace("{color}", color.toString()).replace("{selected_name}", selectedName))));
    }

    public static void sendClanColorFailedNOTHIGHRANKED(Player player, String place) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanColorNotRanked().replace("{required}", place))));
    }

    public static void sendClanChatEnabled(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanChatEnabled())));
    }

    public static void sendClanChatDisabled(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanChatDisabled())));
    }

    public static void sendNoPermission(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageNoPermission())));
    }

    public static void sendNoPermission(CommandSender sender){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageNoPermission())));
    }

    public static void sendDataRefreshed(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageDataRefreshed())));
    }

    public static void sendTooLittleArguments(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageFewArguments())));
    }

    public static void sendNoPlayerFound(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageNoPlayerFound())));
    }

    public static void sendAlreadyInClan(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageAlreadyClanned())));
    }

    public static void sendClanMaxed(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageClanMaxed())));
    }

    public static void sendPlayerIsAlreadyInClan(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessagePlayerIsAlreadyClanned())));
    }

    public static void sendMentionedYourselfCantUse(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageFailedMentionedYourself())));
    }

    public static void sendAlreadyMember(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageAlreadyMember())));
    }

    public static void sendPlayerHasMaxedInvitations(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessagePlayerMaxedInvitations())));
    }

    public static void sendPlayerIsAlreadyInvited(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessagePlayerIsAlreadyInvited())));
    }

    public static void sendPlayerFriendlyFire(Player player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageFriendlyFire())));
    }

    public static void sendPlayerInvitedToClan(Player player, String playerName){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageInvitedPlayer().replace("{player}", playerName))));
    }

    public static void sendYouWereInvited(Player player, String clanName){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageYouWereInvited().replace("{get_clan}", clanName))));
    }

    public static void sendYouWereInvitedHowToAccept(Player player, String clanName){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageYouWereInvitedAcceptMethod().replace("{get_clan}", clanName))));
    }

    public static void sendJoinedClan(Player player, String clanName){
        if(Config.isBroadcastEnabled()){
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageJoinedClan().replace("{get_clan}", clanName).replace("{player}", player.getName()))));
        }else{
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageJoinedClan().replace("{get_clan}", clanName).replace("{player}", player.getName()))));
        }
    }


    public static void sendRenamedClan(Player player, String clanName) {
        if(Config.isBroadcastEnabled()){
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageRenamedClan().replace("{get_clan}", clanName).replace("{player}", player.getName()))));
        }else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageRenamedClan().replace("{get_clan}", clanName).replace("{player}", player.getName()))));
        }
    }

    public static void sendMadeClan(Player player, String clanName) {
        if(Config.isBroadcastEnabled()){
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageMadeClan().replace("{get_clan}", clanName).replace("{player}", player.getName()))));
        }else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageMadeClan().replace("{get_clan}", clanName).replace("{player}", player.getName()))));
        }
    }

    public static void sendPendingInvites(Player player, int numberOfPendingInvites) {
        String message = Adventure.parseToLegacy(Config.getMessagePendingInvitations().replace("{pending}", String.valueOf(numberOfPendingInvites)));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + message));
    }

    public static void sendInvalidExecutor(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Prefixes.getPrefix() + Adventure.parseToLegacy(Config.getMessageInvalidExecutor())));
    }
}
