package org.yastral.exoticchallenges.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.yastral.exoticchallenges.listeners.settings.Settings;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;

public class JoinLeave implements Listener {

    @EventHandler
    public void setJoinMessage(PlayerJoinEvent event){
        ChatColor green = ChatColor.GREEN;
        ChatColor gold = ChatColor.GOLD;
        Player player = event.getPlayer();
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l-----------------------------------"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bWelcome &6 " + player.getName() + " &bto ExoticChallenge!"));
        player.sendMessage("");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + Bukkit.getOnlinePlayers().size() + " &6Players online!"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l-----------------------------------"));
        player.sendMessage("§e§lLobby Loading.... \nLoading: §8[§r" + ProgressBar.getProgressBar(20, 100, 40, '|', GREEN, GRAY) + "§8]");
        int percent = 20 * 100 / 100; //The first 20 is my current amount, the first 100 is just to make it bigger, the second 100 is the maximum you can get, so 100 in this case
        double b = Math.round(percent * 10.0) / 10.0 ;
        player.sendMessage("§e§lPERCENTAGE: " + b + "§e§l%");

        event.setJoinMessage(/*green + event.getPlayer().getName() + " " + Settings.getInstance().getJoin()*/" ");
        if(player.getWorld().getName().startsWith("lobby")){
            QueueListener3.teleportPlayerToWorld(player, "world");
            QueueListener3.handlePlayerLeaveQueue(player, player.getWorld().getName());
        }
        //Bukkit.broadcastMessage(green + event.getPlayer().getName() + " " + Settings.getInstance().getJoin());

        //player.sendMessage(gold + event.getPlayer().getName() + " sei il benvenuto nel server!");
        //System.out.println("Prima");
    }




    @EventHandler//(priority = EventPriority.HIGHEST)
    public void setQuitMessage(PlayerQuitEvent event){
        Player player = event.getPlayer();
        //Bukkit.broadcastMessage(red + event.getPlayer().getName() + " " + Settings.getInstance().getLeave());
        QueueListener3.sendMessageToQueue(player.getWorld().getName(),ChatColor.GOLD + "[" +  player.getWorld().getName()  +  "]" + ChatColor.YELLOW + "" + event.getPlayer().getName() + " ha lasciato la coda! (" + QueueListener3.getPlayerQueue(player).size() + "/" + Settings.getInstance().getNum_player() + ")");
        //QueueListener3.handlePlayerLeaveQueue(player, player.getWorld().getName());
        event.setQuitMessage(/*ChatColor.RED + event.getPlayer().getName() + " " + Settings.getInstance().getLeave()*/" ");
        //System.out.println("Dopo");
    }
}
