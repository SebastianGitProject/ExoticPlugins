package org.yastral.exoticchallenges.listeners;


import org.bukkit.*;
import org.bukkit.entity.Player;
import org.yastral.exoticchallenges.ExoticChallenges;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LobbyManager {

    private final ExoticChallenges plugin;
    private final Map<String, List<Player>> lobbies = new HashMap<>();
    private final int MAX_PLAYERS = 10; // Ad esempio, impostiamo un massimo di 10 giocatori per lobby

    public LobbyManager(ExoticChallenges plugin) {
        this.plugin = plugin;
    }

    public ExoticChallenges getLobbyManager() {
        return plugin;
    }

    public String addPlayerToQueue(Player player) {
        for (Map.Entry<String, List<Player>> entry : lobbies.entrySet()) {
            List<Player> queue = entry.getValue();
            if (queue.size() < MAX_PLAYERS) {
                queue.add(player);
                updatePlayerListInLobbyFile(entry.getKey(), player.getName(), true);
                return entry.getKey();
            }
        }

        // Se tutte le lobby sono piene, crea una nuova lobby
        String newLobbyName = "lobby" + (lobbies.size() + 1);
        List<Player> newQueue = new ArrayList<>();
        newQueue.add(player);
        lobbies.put(newLobbyName, newQueue);
        createLobbyWorld(newLobbyName);
        updatePlayerListInLobbyFile(newLobbyName, player.getName(), true);
        return newLobbyName;
    }

    public void addPlayerToQueue(Player player, String lobbyName) {
        if (!lobbies.containsKey(lobbyName)) {
            lobbies.put(lobbyName, new ArrayList<>());
        }
        List<Player> queue = lobbies.get(lobbyName);
        queue.add(player);
        updatePlayerListInLobbyFile(lobbyName, player.getName(), true);
    }

    public String removePlayerFromQueue(Player player) {
        for (Map.Entry<String, List<Player>> entry : lobbies.entrySet()) {
            List<Player> queue = entry.getValue();
            if (queue.remove(player)) {
                updatePlayerListInLobbyFile(entry.getKey(), player.getName(), false);
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean lobbyExists(String lobbyName) {
        return lobbies.containsKey(lobbyName);
    }

    public boolean isLobbyFull(String lobbyName) {
        List<Player> queue = lobbies.get(lobbyName);
        return queue != null && queue.size() >= MAX_PLAYERS;
    }

    public int getLobbyPlayerCount(String lobbyName) {
        return lobbies.getOrDefault(lobbyName, Collections.emptyList()).size();
    }

    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    public boolean isLobbyEmpty(String lobbyName) {
        List<Player> queue = lobbies.get(lobbyName);
        return queue == null || queue.isEmpty();
    }

    public void deleteLobby(String lobbyName) {
        lobbies.remove(lobbyName);
        deleteLobbyFiles(lobbyName);
    }

    private void createLobbyWorld(String lobbyName) {
        // Creazione del mondo della lobby (puoi personalizzare questo metodo)
        World world = Bukkit.createWorld(new WorldCreator(lobbyName));
        if (world != null) {
            world.setSpawnLocation(0, 0, 0);
        }
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
        Path path = Paths.get(plugin.getDataFolder() + "/lobbygen/" + lobbyName + ".yml");
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

