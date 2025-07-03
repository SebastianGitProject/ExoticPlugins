package org.yastral.exoticchallenges.lobbydata.commands;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.listeners.LobbyManager;
import org.yastral.exoticchallenges.listeners.QueueListener3;
import org.yastral.exoticchallenges.listeners.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;


public class LeaveCommand2 implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Solo i giocatori possono usare questo comando!");
            return true;
        }

        Player player = (Player) sender;
        Queue<Player> queue = QueueListener3.getPlayerQueue(player);
        String worldName = player.getWorld().getName();//QueueListener3.getWorldName(player);

        if (queue != null && worldName != null) {
            queue.remove(player);
            player.sendMessage(ChatColor.YELLOW + "Hai lasciato " + worldName);
            if(queue == null){
                player.sendMessage("Queue: Non esiste più la lobby di cui facevi parte");
            }else{
                player.sendMessage("Queue: " + queue.size() + " World:" + QueueListener3.getWorldName(player));
            }
            //player.sendMessage("Queue: " + QueueListener3.getPlayerQueue(player).size());

            // Teletrasporta il player nel mondo "world"
            World world = Bukkit.getWorld("world");
            if (world != null) {
                player.teleport(world.getSpawnLocation());
            } else {
                player.sendMessage(ChatColor.RED + "Il mondo 'world' non esiste!");
                return true;
            }

            // Check if the queue is empty after the player leaves
            if (queue.isEmpty()) {
                // Clean up the queue and remove associated files
                QueueListener3.removeQueue(worldName);
                deleteLobbyFiles(worldName);
            }
        } else {
            player.sendMessage(ChatColor.RED + "Non sei in nessuna coda!");
        }

        return true;
    }

    private void deleteLobbyFiles(String lobbyName) {
        Path lobbyFolder = Paths.get(ExoticChallenges.getInstance().getDataFolder() + "/lobbygen/" + lobbyName);
        Path lobbyFile = Paths.get(ExoticChallenges.getInstance().getDataFolder() + "/lobbygen/" + lobbyName + ".yml");

        try {
            if (Files.exists(lobbyFolder)) {
                Files.walk(lobbyFolder)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                Bukkit.getLogger().severe("Errore durante l'eliminazione del file: " + e.getMessage());
                            }
                        });
            }
            if (Files.exists(lobbyFile)) {
                Files.delete(lobbyFile);
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe("Errore durante l'eliminazione del file: " + e.getMessage());
        }
        //delete lobby----------------
        String nomeMondoy = Bukkit.getWorld(lobbyName).getName();
        World worlds = Bukkit.getWorld(nomeMondoy);
        if (worlds == null) {
            System.out.println("Il mondo '" + nomeMondoy + "' non esiste.");
            return;
        }

        Server server = ExoticChallenges.getPlugin(ExoticChallenges.class).getServer();

        // Unload the world.
        server.unloadWorld(worlds, false);

        // Unload the chunks.
        Chunk[] chunks = worlds.getLoadedChunks();
        for (Chunk chunk : chunks) {
            chunk.unload(false);
        }

        // Delete the world's data directory.
        File actives = worlds.getWorldFolder();
        boolean deleted = deleteWorldFolder(actives);


        //File path = new File(ExoticChallenges.getInstance().getDataFolder() + "/lobbygen");
        File directory = new File(ExoticChallenges.getInstance().getDataFolder() + "\\lobbygen");
        // Ottieni tutti i file nella cartella
        File[] files = directory.listFiles();
        // Stampa i nomi dei file
        for (File filey : files) {
            if(filey.getName().equalsIgnoreCase(lobbyName + ".yml")){

                //System.out.println(filey.getName());
                //player.sendMessage("file '" + filey.getName() + ".yml' è stato eliminato! path: " + QueueCommand.getPath().getPath());
                filey.delete();
            }
        }

        //sender.sendMessage("file '" + filey.getName() + "' è stato eliminato! path: " + ExoticChallenges.getInstance().getDataFolder() + "/lobbygen");

        if (deleted) {
            //player.sendMessage("Il mondo '" + nomeMondoy + "' è stato eliminato con successo.");
        } else {
            //player.sendMessage("Errore durante l'eliminazione del mondo '" + nomeMondoy + "'.");
        }
        //----------------------------------
    }


    private static boolean deleteWorldFolder(File worldFolder) {
        if (worldFolder.exists()) {
            File[] files = worldFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorldFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
            return worldFolder.delete();
        }
        return false;
    }


    private void updatePlayerListInLobbyFile(String lobbyName, String playerName, boolean add) {
        Path path = Paths.get(ExoticChallenges.getInstance().getDataFolder() + "/lobbygen/" + lobbyName + ".yml");
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (add) {
                lines.add("Player: " + playerName);
            } else {
                lines.removeIf(line -> line.equals("Player: " + playerName));
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Errore durante la lettura/scrittura del file: " + e.getMessage());
        }
    }
}
