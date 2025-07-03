package org.yastral.exoticchallenges;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
//import org.mineacademy.fo.plugin.SimplePlugin;
import org.yastral.exoticchallenges.board.ScoreHelper;
import org.yastral.exoticchallenges.hook.Packets;
import org.yastral.exoticchallenges.listeners.*;
import org.yastral.exoticchallenges.lobbydata.LobbysGenerator;
import org.yastral.exoticchallenges.lobbydata.commands.*;
import org.yastral.exoticchallenges.listeners.settings.Settings;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import java.io.File;
import java.io.IOException;

public final class ExoticChallenges extends JavaPlugin implements Listener {
    private LobbyManager lobbyManager;
    private static ExoticChallenges plugin;
    private BukkitTask task;
    private ProtocolManager protocolManager;
    //private BukkitTask task2;



    @Override
    public void onEnable() {
        getLogger().info("@ Il plugin si è azionato!");
        File directorys = new File(getDataFolder() + "\\schematic");
        if (!directorys.exists()) {
            directorys.mkdir();
        }

        List<String> worldNames = getWorldNames();
        deleteLobbyWorlds();

        if(task != null && !task.isCancelled()){
            task.cancel();
            //task2.cancel();
        }

        File directoryum = new File("plugins/FastAsyncWorldEdit/schematics");
        if(directoryum.exists()){
            // Ottieni tutti i file nella cartella
            File[] files = directoryum.listFiles();
            // Stampa i nomi dei file
            for (File filey : files) {
                if(filey.getName().startsWith("chunk")){
                    filey.delete();
                }
            }
        }else{
            getLogger().info("[ExoticChallenges] Nessuna schematic chunk di fawe trovata");
        }

        File directory = new File(ExoticChallenges.getInstance().getDataFolder() + "\\lobbygen");
        if(directory.exists()){
            // Ottieni tutti i file nella cartella
            File[] files = directory.listFiles();
            // Stampa i nomi dei file
            for (File filey : files) {
                if(filey.getName().startsWith("lobby")){

                    getLogger().info(ChatColor.GREEN + "[ExoticChallenges] file '" + filey.getName() + "' deleted");
                    //player.sendMessage("file '" + filey.getName() + ".yml' è stato eliminato! path: " + QueueCommand.getPath().getPath());
                    filey.delete();
                }
            }
        }else{
            getLogger().info(ChatColor.RED + "[ExoticChallenges] Not found any lobbys generated");
        }



        File directoryy = new File(ExoticChallenges.getInstance().getDataFolder() + "\\lobbygen\\dataChunk");
        if(directoryy.exists()){
            // Ottieni tutti i file nella cartella
            File[] files = directoryy.listFiles();
            // Stampa i nomi dei file
            for (File filey : files) {
                if(filey.getName().startsWith("chunk")){

                    //getLogger().info(ChatColor.GREEN + "[ExoticChallenges] file '" + filey.getName() + "' deleted");
                    //player.sendMessage("file '" + filey.getName() + ".yml' è stato eliminato! path: " + QueueCommand.getPath().getPath());
                    filey.delete();
                }
            }
        }else{
            getLogger().info(ChatColor.RED + "[ExoticChallenges] Not found any dataChunk .schem files");
        }


        //ButtonReturnBack.setMaterial(CompMaterial.ARROW);
        lobbyManager = new LobbyManager(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new JoinLeave(), this);
        //getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new QueueListener3(), this);

        //getCommand("gui").setExecutor(new GuiCommand());
        getCommand("queue").setExecutor(new QueueCommand2());
        getCommand("leave").setExecutor(new LeaveCommand2());
        getCommand("lobby").setExecutor(new LobbyCommand2());
        getCommand("test").setExecutor(new TestCommand(this));
        getCommand("boh").setExecutor(new Test2Command());

        /*if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null){
            Packets.register();
        }*/
        Settings.getInstance().load();


        plugin = this;
        if(Settings.getInstance().getMode().equals("multiworld")){

            getLogger().info("\n\n" + ChatColor.GREEN + "[ExoticChallenges] Mode = " + Settings.getInstance().getMode() + "\n\n");
            LobbysGenerator.extractFileZip();

        }else if(Settings.getInstance().getMode().equals("bungeecord")){

            if(Settings.getInstance().getType_mode_multiworld().equals("hub")){
                getLogger().info("\n\n" + ChatColor.GREEN + "[ExoticChallenges] Mode = " + Settings.getInstance().getMode() + ", Type = " + Settings.getInstance().getType_mode_multiworld() + "\n\n");
            }else if(Settings.getInstance().getType_mode_multiworld().equals("lobby")){
                getLogger().info("\n\n" + ChatColor.GREEN + "[ExoticChallenges] Mode = " + Settings.getInstance().getMode() + ", Type = " + Settings.getInstance().getType_mode_multiworld() + "\n\n");
                LobbysGenerator.extractFileZip();
            }
        }
        Packets.register();
        Packets.registerInventory();
        //Packets.registerMovement();
        /*protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(
                this,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.CHAT
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                String message = packet.getStrings().read(0);

                if (message.contains("shit") || message.contains("damn")) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("Bad manners!");
                }
            }

        });*/

        new BukkitRunnable() {

            @Override
            public void run() {

                for(Player player : Bukkit.getOnlinePlayers()) {
                    updateScoreboard(player);
                }

            }

        }.runTaskTimer(this, 20L, 20L);



    }

    @Override
    public void onDisable() {
        getLogger().info("@ Il plugin si è disabilitato!");

    }


    private List<String> getWorldNames() {
        List<World> worlds = Bukkit.getWorlds();

        // Filtra i mondi che non iniziano con "lobby" e raccoglie i nomi
        return worlds.stream()
                .map(World::getName)
                .filter(name -> !name.startsWith("lobby"))
                .collect(Collectors.toList());
    }


    private void deleteLobbyWorlds() {
        // Ottiene la directory del server
        File serverDirectory = Bukkit.getWorldContainer();

        // Ottiene i nomi dei mondi caricati nel server
        List<String> loadedWorldNames = Bukkit.getWorlds().stream()
                .map(World::getName)
                .collect(Collectors.toList());

        // Ottiene tutte le directory nella directory del server
        File[] files = serverDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && file.getName().startsWith("lobby") && !loadedWorldNames.contains(file.getName())) {
                    deleteDirectory(file);
                    getLogger().info("Deleted lobby directory: " + file.getName());
                }
            }
        }else{
            getLogger().info(ChatColor.GREEN + "[ExoticChallenges] Not found any lobby world...");
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    if (!file.delete()) {
                        getLogger().info("Failed to delete file: " + file.getPath());
                    }
                }
            }
        }

        if (!directory.delete()) {
            getLogger().info("Failed to delete directory: " + directory.getPath());
        }
    }









    /*@Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new LobbysGenerator(worldName, id);
        //return super.getDefaultWorldGenerator(worldName, id);
    }*/







    public static ExoticChallenges getPlugin() {
        return plugin;
    }


    public static ExoticChallenges getInstance(){
        return getPlugin(ExoticChallenges.class);
    }

    private void createHubScoreboard(Player player) {
        ScoreHelper helper = ScoreHelper.createScore(player);
        World world = player.getWorld();

        helper.setTitle("&6Challenge &6Hub");
        helper.setSlot(9, "&7&m-----------------");
        helper.setSlot(8, " ");
        helper.setSlot(7, "&b\u2BA9 &lInfo:");
        helper.setSlot(6, "&6Name: &e" + player.getName());
        helper.setSlot(5, "&6Lobby: &eHub");
        helper.setSlot(4, "&6Online: &e" + Bukkit.getOnlinePlayers().size());
        //helper.setSlot(4, "&aLocation&f: &e" + getLocation(player));
        helper.setSlot(3, " ");
        helper.setSlot(2, "&emc.name.it");
        helper.setSlot(1, "&7&m-----------------");
    }

    private void createLobbyScoreboard(Player player) {
        ScoreHelper helper = ScoreHelper.createScore(player);
        helper.setTitle("&6Challenge Lobby");
        helper.setSlot(8, "&7&m-------------------------");
        helper.setSlot(7, " ");
        helper.setSlot(6, "&6Server: &e" + player.getWorld().getName());
        helper.setSlot(5, "&6Players: &e(" + QueueListener3.getPlayerQueue(player).size() + "/" + Settings.getInstance().getNum_player() + ")");
        helper.setSlot(4, "&6Status: &eIn lobby...");
        helper.setSlot(3, " ");
        helper.setSlot(2, "&emc.name.it");
        helper.setSlot(1, "&7&m-------------------------");
    }

    private void createGameScoreboard(Player player) {
        ScoreHelper helper = ScoreHelper.createScore(player);
        helper.setTitle("&6Challenge Game &7[ " + player.getWorld().getName() + " ]");
        helper.setSlot(8, "&7&m-------------------------");
        helper.setSlot(7, " ");
        helper.setSlot(6, "&6Players in game: &e(" + QueueListener3.getPlayerQueue(player).size() + "/" + Settings.getInstance().getNum_player() + ")");
        helper.setSlot(5, "&6Remain challenges: &e0\u2611");
        helper.setSlot(4, "&6Current challenge: &e%name%");
        helper.setSlot(3, " ");
        helper.setSlot(3, "&6Player kills: " + player.getStatistic(Statistic.PLAYER_KILLS));
        helper.setSlot(3, " ");
        helper.setSlot(2, "&emc.name.it");
        helper.setSlot(1, "&7&m-------------------------");
    }

    private void updateScoreboard(Player player) {
        if(ScoreHelper.hasScore(player)) {
            ScoreHelper helper = ScoreHelper.getByPlayer(player);
            //helper.setSlot(3, "&aLocation&f: " + getLocation(player));

            if(!player.getWorld().getName().startsWith("lobby")) {
                ScoreHelper.removeScore(player);
                createHubScoreboard(player);
                //helper.removeSlot(4);
                //helper.removeSlot(1);
            }else if(player.getWorld().getName().startsWith("lobby")) {
                ScoreHelper.removeScore(player);
                createLobbyScoreboard(player);
                if(QueueListener3.getPlayerQueue(player).size() == Settings.getInstance().getNum_player()){
                    helper.setSlot(4, "&6Status: &eJoining the game...");
                    /*try{
                        Thread.sleep(10500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }*/
                    Path path = Paths.get(ExoticChallenges.getInstance().getDataFolder() + "/lobbygen/" + player.getWorld().getName() + ".yml");
                    try (BufferedReader br = new BufferedReader(new FileReader(String.valueOf(path)))) {
                        String linea = br.readLine(); // Leggi la prima riga

                        if (linea != null && linea.contains("Status: ready")) {
                            ScoreHelper.removeScore(player);
                            createGameScoreboard(player);
                        } else {
                            ScoreHelper.removeScore(player);
                            createLobbyScoreboard(player);
                            helper.setSlot(4, "&6Status: &eJoining the game...");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    ScoreHelper.removeScore(player);
                    createLobbyScoreboard(player);
                    helper.setSlot(4, "&6Status: &eIn lobby...");
                }

            }
        }
    }

    private String getLocation(Player player) {
        Location l = player.getLocation();
        return l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
    }

    private int getKills(Player player) {
        return player.getStatistic(Statistic.PLAYER_KILLS);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String nomeMondo = player.getWorld().getName();
        createHubScoreboard(player);
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(ScoreHelper.hasScore(player)) {
            ScoreHelper.removeScore(player);
        }

    }







}
