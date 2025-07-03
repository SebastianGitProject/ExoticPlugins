package org.yastral.exoticchallenges.lobbydata.commands;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.gui.ChallengesGui;
import org.yastral.exoticchallenges.listeners.ProgressBar;
import org.yastral.exoticchallenges.listeners.settings.Settings;
import org.yastral.exoticchallenges.lobbydata.LobbysGenerator;
import org.yastral.exoticchallenges.lobbydata.VoidGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;

public class QueueCommand2 implements CommandExecutor {

    private static final File path = new File(ExoticChallenges.getInstance().getDataFolder() + "/lobbygen");
    private static int numeroStanza;
    private static boolean startExtractFiles = false;

    public static File getPath() {
        return path;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Solo i player possono usare questo comando!");
            return true;
        }

        Player player = (Player) sender;
        numeroStanza = Settings.getInstance().getNum_player();

        File lobby = getNextLobbyFile();
        if (lobby != null) {
            try {
                if (!lobby.exists()) {
                    lobby.createNewFile();
                }
                int lobbyNumber = getLobbyNumber(lobby.getName());
                player.setMetadata("lobby" + lobbyNumber, new FixedMetadataValue(ExoticChallenges.getInstance(), true));
                teleportPlayerToLobby(player, lobbyNumber);
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "Errore durante la creazione della lobby.");
                e.printStackTrace();
            }
        }
        return true;

    }

    private File getNextLobbyFile() {
        if (!path.exists()) {
            path.mkdir();
        }
        Collection<File> files = FileUtils.listFiles(path, new String[]{"yml"}, false);
        int highestNumber = files.stream()
                .map(file -> StringUtils.substringBefore(StringUtils.substringAfter(file.getName(), "lobby"), ".yml"))
                .filter(StringUtils::isNumeric)
                .mapToInt(Integer::parseInt)
                .max().orElse(0);
        return new File(path, "lobby" + (highestNumber + 1) + ".yml");
    }

    private int getLobbyNumber(String lobbyName) {
        return Integer.parseInt(StringUtils.substringBefore(StringUtils.substringAfter(lobbyName, "lobby"), ".yml"));
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
            loc.clone().add(0, -1, 0).getBlock().setType(Material.BARRIER);
            new BukkitRunnable() {
                int count = 10;

                @Override
                public void run() {
                    if (count > 0) {
                        player.teleport(loc);
                        count--;
                    }else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 1L);
            /*loc.clone().add(1, -1, 0).getBlock().setType(Material.BARRIER);
            loc.clone().add(0, -1, 1).getBlock().setType(Material.BARRIER);
            loc.clone().add(-1, -1, 0).getBlock().setType(Material.BARRIER);
            loc.clone().add(0, -1, -1).getBlock().setType(Material.BARRIER);
            loc.clone().add(-1, -1, 1).getBlock().setType(Material.BARRIER);
            loc.clone().add(-1, -1, -1).getBlock().setType(Material.BARRIER);
            loc.clone().add(1, -1, 1).getBlock().setType(Material.BARRIER);
            loc.clone().add(1, -1, -1).getBlock().setType(Material.BARRIER);*/
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
