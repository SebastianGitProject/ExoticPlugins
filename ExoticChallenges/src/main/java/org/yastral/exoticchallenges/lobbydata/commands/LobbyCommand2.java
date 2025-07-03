package org.yastral.exoticchallenges.lobbydata.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.listeners.LobbyManager;
import org.yastral.exoticchallenges.listeners.QueueListener3;
import org.yastral.exoticchallenges.listeners.settings.Settings;
import org.yastral.exoticchallenges.lobbydata.VoidGenerator;

import java.util.Queue;

public class LobbyCommand2 implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                try {
                    int lobbyNumber = Integer.parseInt(args[0]);
                    String lobbyName = "lobby" + lobbyNumber;
                    Queue<Player> queue = QueueListener3.getQueueByWorldName(lobbyName);
                    if (queue != null && queue.size() != Settings.getInstance().getNum_player()) {
                        //player.sendMessage(ChatColor.GREEN + "Stai entrando in " + lobbyName);
                        player.setMetadata("lobby" + lobbyNumber, new FixedMetadataValue(ExoticChallenges.getInstance(), true));
                        teleportPlayerToLobby(player, lobbyNumber);
                    } else {
                        player.sendMessage(ChatColor.RED + "Non esiste la lobby oppure la lobby è piena.");
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Il numero della lobby deve essere un valore numerico.");
                }
            } else {
                player.sendMessage(ChatColor.GRAY + "Uso: /lobby <numero>");
            }
        }
        return true;
    }

    private void teleportPlayerToLobby(Player player, int lobbyNumber) {
        String lobbyWorldName = "lobby" + lobbyNumber;
        player.sendMessage(ChatColor.YELLOW + "Stai per entrare nella lobby #" + lobbyNumber);
        World world = Bukkit.getWorld(lobbyWorldName);
        if (world == null) {
            createLobbyWorld(lobbyWorldName);
            world = Bukkit.getWorld(lobbyWorldName);
        }
        if (world != null) {
            Location loc = new Location(world, 0, 0, 0, 0, 0);
            player.teleport(loc);
            //loc.clone().add(0, -1, 0).getBlock().setType(Material.BARRIER);
        } else {
            player.sendMessage(ChatColor.RED + "Errore nel teletrasporto alla lobby.");
        }
    }

    private void createLobbyWorld(String lobbyWorldName) {
        WorldCreator wc = new WorldCreator(lobbyWorldName);
        wc.generator(new VoidGenerator());
        Bukkit.createWorld(wc);
    }
}
