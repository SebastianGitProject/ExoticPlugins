package org.yastral.exoticchallenges.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.listeners.LobbyManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class PlayerLogoutListener implements Listener {

    /*private final ExoticChallenges plugin;

    public PlayerLogoutListener(ExoticChallenges plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LobbyManager lobbyManager = plugin.getLobbyManager();

        String lobbyName = lobbyManager.removePlayerFromQueue(player);
        if (lobbyName != null) {
            // Aggiorna il file .yml rimuovendo il nome del giocatore
            updatePlayerListInLobbyFile(lobbyName, player.getName(), false);

            // Reindirizza il giocatore nel mondo "world"
            player.teleport(new Location(Bukkit.getWorld("world"), 0, 0, 0));

            if (lobbyManager.isLobbyEmpty(lobbyName)) {
                // Elimina la queue e i file associati
                lobbyManager.deleteLobby(lobbyName);
                deleteLobbyFiles(lobbyName);
            }
        }
    }

    private void deleteLobbyFiles(String lobbyName) {
        Path lobbyFolder = Paths.get(plugin.getDataFolder() + "/lobbygen/" + lobbyName);
        Path lobbyFile = Paths.get(plugin.getDataFolder() + "/lobbygen/" + lobbyName + ".yml");

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
    }*/
}
