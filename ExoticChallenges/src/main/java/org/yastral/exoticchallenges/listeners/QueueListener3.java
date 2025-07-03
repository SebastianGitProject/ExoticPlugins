package org.yastral.exoticchallenges.listeners;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.challenge.ChallengeSystem;
import org.yastral.exoticchallenges.gui.ChallengesGui;
import org.yastral.exoticchallenges.gui.ChallengesInfoGui;
import org.yastral.exoticchallenges.listeners.settings.Settings;
import org.yastral.exoticchallenges.board.Board;
import org.yastral.exoticchallenges.lobbydata.LobbysGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class QueueListener3 implements Listener {

    public static boolean game;
    public static final Map<String, Queue<Player>> playerQueues = new HashMap<>();
    public static final Map<String, Queue<Player>> playerAlive = new HashMap<>();
    public static final Map<String, RoundSystem> roundSystems = new HashMap<>();
    private static int MAX_PLAYERS;
    private static int MAX_ROUNDS;
    private static String guiTitle = "Challenges";
    //private final ChallengeSystem challengeSystem = new ChallengeSystem(ExoticChallenges.getPlugin());

    public static Boolean open = false;

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        MAX_PLAYERS = Settings.getInstance().getNum_player();
        MAX_ROUNDS = Settings.getInstance().getNum_rounds();
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        if (!worldName.startsWith("lobby")) {
            for (Queue<Player> queue : playerQueues.values()) {
                if (queue.contains(player)) {
                    handlePlayerLeaveQueue(player, worldName);
                    return;
                }
            }
        }

        if (!playerQueues.containsKey(worldName)) {
            playerQueues.put(worldName, new LinkedList<>());
            playerAlive.put(worldName, new LinkedList<>());
            roundSystems.put(worldName, new RoundSystem(worldName, playerQueues.get(worldName), MAX_ROUNDS));
            updateLobbyStatusFile(worldName, "none");
        }

        if (player.hasMetadata(worldName)) {
            handlePlayerJoinQueue(player, worldName);
        } else {
            handlePlayerLeaveQueue(player, worldName);
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        //System.out.println("mondo del player quittayo: " + worldName);
        if (worldName != null && worldName.startsWith("lobby")) {
            handlePlayerLeaveQueue(player, worldName);
            player.getInventory().clear();
            Queue<Player> queue = playerQueues.get(worldName);
            Queue<Player> alive = playerAlive.get(worldName);
            alive.remove(player);
            if (queue.isEmpty()) {
                removeQueue(worldName);
                deleteLobbyFiles(worldName);
            } else {
                sendMessageToQueue(worldName, ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.YELLOW + " " + player.getName() + " ha lasciato la lobby.");
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        Queue<Player> alive = playerAlive.get(worldName);
        Queue<Player> queue = playerQueues.get(worldName);



        if (worldName != null && worldName.startsWith("lobby")) {
            alive.remove(player);
            queue.remove(player);
        }
        //TODO fare il sistema di morte come spectator


    }

    public static Map<String, RoundSystem> getRoundSystems() {
        return roundSystems;
    }

    public static void handlePlayerJoinQueue(Player player, String worldName) {
        Queue<Player> queue = playerQueues.get(worldName);
        Queue<Player> alive = playerAlive.get(worldName);
        if (queue.size() < MAX_PLAYERS) {
            queue.add(player);
            alive.add(player);
            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(20);
            player.setSaturation(20);
            player.getInventory().clear();
            if (queue.size() == 1) {
                loadSchematic(worldName);
            }
            player.sendMessage(ChatColor.YELLOW + "Sei entrato in " + ChatColor.BOLD + "" + worldName);
            sendMessageToQueue(worldName, ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.YELLOW + " " + player.getName() + " è entrato nella coda! (" + queue.size() + "/" + MAX_PLAYERS + ")");
            if (queue.size() == MAX_PLAYERS) {
                startGameCountdown(worldName);
            }
        } else {
            player.sendMessage(ChatColor.RED + "La coda è piena! (" + queue.size() + "/" + MAX_PLAYERS + ")");
            teleportPlayerToWorld(player, "world");
        }
    }

    public static void handlePlayerLeaveQueue(Player player, String worldName) {
        Queue<Player> queue = playerQueues.get(worldName);
        Queue<Player> alive = playerAlive.get(worldName);
        if (queue != null && queue.remove(player) && alive.remove(player)) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatColor.YELLOW + "Hai lasciato la coda!");
            player.getInventory().clear();
            teleportPlayerToWorld(player, "world");
        } else {
            //player.sendMessage("Non sei nella coda!");
        }
    }

    public static void updateLobbyStatusFile(String worldName, String status) {
        Path path = Paths.get(ExoticChallenges.getPlugin().getDataFolder() + "/lobbygen/" + worldName + ".yml");
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                lines.add("Status: " + status);
            } else {
                lines.set(0, "Status: " + status);
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Errore durante la lettura/scrittura del file: " + e.getMessage());
        }
    }

    public static void loadSchematic(String worldName) {
        String commandFile = Settings.getInstance().getName_schem();
        if (Settings.getInstance().isSchemFile()) {
            String result = commandFile.substring(0, commandFile.indexOf("."));
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/fast");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/schem load " + result);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldName);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 0,0,0");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 0,0,0");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste");  //paste -o
        } else {
            Bukkit.getLogger().severe("Non è stato possibile caricare il file lobby");
        }
    }

    public static void sendMessageToQueue(String worldName, String message) {
        Queue<Player> queue = playerQueues.get(worldName);
        if (queue != null) {
            for (Player queuedPlayer : queue) {
                queuedPlayer.sendMessage(message);
            }
        }
    }

    public static void removeQueue(String worldName) {
        playerAlive.remove(worldName);
        playerQueues.remove(worldName);
        //roundSystems.remove(worldName);
        RoundSystem roundSystem = roundSystems.remove(worldName);
        roundSystem.stopRounds();

    }

    public static void startGameCountdown(String worldName) {
        //ChallengesGui challengesGui = new ChallengesGui(roundSystems, playerQueues);
        //ChallengesGui challengesGui = new ChallengesGui(playerAlive);
        Queue<Player> queue = playerQueues.get(worldName);
        RoundSystem roundSystem = roundSystems.get(worldName);
        for (Player queuedPlayer : queue) {
            queuedPlayer.sendMessage(ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.YELLOW + "" + ChatColor.BOLD + " La partita inizierà tra 10 secondi...");



        }

        new BukkitRunnable() { //vengono eseguiti 2 bukkitRunnable. deve essere eseguito solamente 1
            int count = 5;

            @Override
            public void run() {
                if (count > 0) {
                    for (Player queuedPlayer : queue) {
                        queuedPlayer.sendTitle(ChatColor.YELLOW + Integer.toString(count), ChatColor.YELLOW + "Secondi rimanenti", 0, 20, 0);
                        queuedPlayer.playSound(queuedPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                    }
                    count--;
                } else {
                    this.cancel();
                    updateLobbyStatusFile(worldName, "ready");
                    sendMessageToQueue(worldName,ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.YELLOW + "" + ChatColor.BOLD + " Inizio del primo round tra " + Settings.getInstance().getTime_between_two_rounds() + " secondi...");





                    LobbysGenerator.genChunk(worldName);
                    for (Player queuedPlayer : queue) {
                        gui(queuedPlayer, worldName);
                    }

                        /*}else{  //se il file schem per le lobby non ha lo stesso nome inserito in settings.yml, la lobby andrà eliminata perchè non potra generare la piattaforma. oppure non ci sono i 1000 file chunk.schem
                            teleportPlayerToWorld(queuedPlayer, "world");
                            removeQueue(worldName);
                            deleteLobbyFiles(worldName);
                        }*/



                    new BukkitRunnable() {
                        int count = Settings.getInstance().getTime_between_two_rounds(); //min 25 secondi di primo tempo

                        @Override
                        public void run() {
                            if (count > 0) {
                                //queuedPlayer.playSound(queuedPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                //queuedPlayer.performCommand("gui");
                                //ChallengesGui.gui(queuedPlayer);

                                if (count < 6) {
                                    sendMessageToQueue(worldName, ChatColor.GOLD + "[" + worldName + "]" + ChatColor.YELLOW + " Mancano " + count + " secondi per il primo round!");
                                    //for (Player queuedPlayer : queue) {

                                    for (Player queuedPlayer : queue) {
                                        queuedPlayer.playSound(queuedPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                                    }
                                    //}
                                }
                                count--;
                            }else {
                                this.cancel();
                                if (roundSystem != null) {
                                    open = false;
                                    Location locuuu = new Location(Bukkit.getWorld(worldName), 1000, 36, 1000, 0, 0);
                                    for (Player queuedPlayer : queue) {
                                        queuedPlayer.teleport(locuuu);
                                    }
                                    LobbysGenerator.setWorldBorder(worldName, 64);
                                    roundSystem.startRounds();
                                    for (Player queuedPlayer : queue) {
                                        queuedPlayer.setGameMode(GameMode.SURVIVAL);
                                    }
                                }
                            }
                        }
                    }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 20L);
                }
            }
        }.runTaskTimer(ExoticChallenges.getPlugin(), 100L, 20L);
    }

    public static void teleportPlayerToWorld(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            Location loc = new Location(world, 0, 0, 0, 0, 0);
            player.teleport(loc);
        } else {
            player.sendMessage("Il mondo '" + worldName + "' non esiste!");
        }
    }

    public static Queue<Player> getPlayerQueue(Player player) {
        for (Map.Entry<String, Queue<Player>> entry : playerQueues.entrySet()) {
            if (entry.getValue().contains(player)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static String getWorldName(Player player) {
        for (Map.Entry<String, Queue<Player>> entry : playerQueues.entrySet()) {
            if (entry.getValue().contains(player)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Queue<Player> getQueueByWorldName(String worldName) {
        return playerQueues.get(worldName);
    }

    private static void deleteLobbyFiles(String lobbyName) {
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




    public static void gui(Player player, String worldName){
        Queue<Player> queue = playerQueues.get(worldName);
        RoundSystem roundSystem = roundSystems.get(worldName);
        open = true;
        String[] guiSetup2 = {
                "  s i z  ",
                "  ggggg  ",
                "  fpdnl  ",
                "         ",
                "asdfghjkl",
                "   t n   ",
        };


        String[] guiSetup = {
                "puuuuuuuy",
                "         ",
                "         ",
                "         ",
                "         ",
                "uuutunuuu",
        };

        StringBuilder secondRow = new StringBuilder(guiSetup[1]);
        StringBuilder fourthRow = new StringBuilder(guiSetup[3]);

        for (int i = 1; i <= Settings.getInstance().getNumChallenges(); i++) {
            if (i <= 5) {
                // Aggiungi "i" alla seconda riga
                int position = 4 - (i - 1); // Posizione centrale basata su i
                secondRow.setCharAt(position * 2, 'i');
            } else {
                // Aggiungi "i" alla quarta riga
                int position = 4 - ((i - 6) * 2); // Posizione centrale basata su i
                fourthRow.setCharAt(position, 'i');
            }
        }

        // Aggiorna le righe nel setup originale
        guiSetup[1] = secondRow.toString();
        guiSetup[3] = fourthRow.toString();
        InventoryGui gui = new InventoryGui(ExoticChallenges.getPlugin(), ChatColor.GOLD + guiTitle, guiSetup);
        //gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS, 1)); // fill the empty slots with this



        //static element

        gui.addElement(new StaticGuiElement('t',
                new ItemStack(Material.GOLD_BLOCK),
                ChatColor.GOLD + "\u2BA9 Top players in " + ChatColor.GRAY + player.getWorld().getName() + ChatColor.GOLD + ": ",
                "",
                //queuedPlayer.getName() + "\n",
                ChatColor.YELLOW + "" + String.join(ChatColor.YELLOW + "\n- ", queue.stream().map(Player::getName).collect(Collectors.toList())),
                ""
        ));


        int result = (roundSystem.getCurrentRound() == 0) ? 1 : roundSystem.getCurrentRound()+1;

        gui.addElement(new DynamicGuiElement('n', (viewer) -> {
            return new StaticGuiElement('n', new ItemStack (Material.NETHER_STAR),
                    click -> {
                        click.getGui().draw(); // Update the GUI
                        return true;
                    },
                    " ",
                    ChatColor.AQUA + "\u2BA9 Info lobby:",
                    ChatColor.YELLOW + "Total of players: " + Settings.getInstance().getNum_player(),
                    ChatColor.YELLOW + "Players alive: " + playerAlive.size(),
                    ChatColor.YELLOW + "Time: " + ChatColor.GRAY + new SimpleDateFormat("HH:mm:ss").format(new Date()),
                    " ",
                    ChatColor.AQUA + "\u2BA9 Info Rounds: ",
                    ChatColor.YELLOW + "Current round: (" + result + "/" + Settings.getInstance().getNum_rounds() + ")",
                    ChatColor.YELLOW + "Total of challenges: " + Settings.getInstance().getNumChallenges(),
                    ChatColor.YELLOW + "Challenges completed: %num%",
                    " ",
                    ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to reload");
        }));

            gui.addElement(new StaticGuiElement('p',
                    new ItemStack(Material.RED_STAINED_GLASS_PANE),
                    ChatColor.AQUA + "Info of previous challenge:",
                    "",
                    ChatColor.YELLOW + "---",
                    ChatColor.YELLOW + "---",
                    ChatColor.YELLOW + "---"
            ));

            gui.addElement(new StaticGuiElement('y',
                    new ItemStack(Material.LIME_STAINED_GLASS_PANE),
                    ChatColor.AQUA + "Next challenge: %name%"
            ));

            //faccio un ciclo for per tutte le challenge, e se una challenge ha nel nome una particolare frase, sarà un enchanted_book
            gui.addElement(new StaticGuiElement('i',
                    new ItemStack(Material.BOOK),
                    click -> {
                        open = false;
                        gui.close();
                        ChallengesInfoGui.guiInfo(player);
                        return true; // returning false will not cancel the initial click event to the gui
                    },
                    ChatColor.AQUA + "#%n% Quest name: %name%",
                    " ",
                    " ",
                    ChatColor.YELLOW + "Click to see the personal statistics",
                    ChatColor.YELLOW + "for this challenge."
            ));

        /*GuiElementGroup group = new GuiElementGroup('i');
        String[] texts = {"ciao", "Hello", "Bibbo" , "Suca", "ALEEEEE"};  //aggiungere nell'array i nomi delle quest e pian piano inserirli nelle lettere i
        for (String text : texts) {
            // Add an element to the group
            // Elements are in the order they got added to the group and don't need to have the same type.
            group.addElement((new StaticGuiElement('e', new ItemStack(Material.DIRT), text)));
        }
        gui.addElement(group);*/


            gui.addElement(new StaticGuiElement('u',
                    new ItemStack(Material.WHITE_STAINED_GLASS_PANE),
                    " "
            ));


            gui.show(player);

        }


}

