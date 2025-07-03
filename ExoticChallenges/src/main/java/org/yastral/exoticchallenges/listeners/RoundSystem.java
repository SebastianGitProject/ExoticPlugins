package org.yastral.exoticchallenges.listeners;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.gui.ChallengesGui;
import org.yastral.exoticchallenges.gui.ChallengesInfoGui;
import org.yastral.exoticchallenges.listeners.settings.Settings;
import org.yastral.exoticchallenges.lobbydata.LobbysGenerator;

import org.yastral.exoticchallenges.vote.voteSystem;

import java.util.*;

import static java.lang.System.getLogger;

public class RoundSystem implements Listener{
    private int currentRound;
    private boolean running;
    private final String worldName;
    private final Queue<Player> playerQueue;
    private final int maxRounds;
    //private LobbysGenerator lobbysGenerator;
    private final List<Location> spawnLocations;
    public final Set<Player> fallDamageImmunePlayers;
    private voteSystem voteSystemm;
    public static int timeRound = 0;


    public RoundSystem(String worldName, Queue<Player> playerQueue, int maxRounds) {
        this.worldName = worldName;
        this.playerQueue = playerQueue;
        this.currentRound = 0;
        this.running = false;
        this.maxRounds = maxRounds;
        this.spawnLocations = initializeSpawnLocations();
        this.fallDamageImmunePlayers = new HashSet<>();
        Bukkit.getPluginManager().registerEvents(this, ExoticChallenges.getPlugin());
    }

    private List<Location> initializeSpawnLocations() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().severe("Il mondo '" + worldName + "' non esiste!");
            return Collections.emptyList();
        }

        List<Location> locations = new ArrayList<>();
        locations.add(new Location(world, 1000.500, 36, 1000.500));
        locations.add(new Location(world, 968.500, 36, 1000.500));
        locations.add(new Location(world, 984.500, 36, 984.500));
        locations.add(new Location(world, 952.500, 36, 984.500));
        locations.add(new Location(world, 1000.500, 36, 968.500));
        locations.add(new Location(world, 968.500, 36, 968.500));
        locations.add(new Location(world, 984.500, 36, 952.500));
        locations.add(new Location(world, 952.500, 36, 952.500));

        return locations;
    }

    private void buildGlassCage(World world, Location loc) {
        //loc.getBlock().setType(Material.GLASS);
        loc.clone().add(0, -1, 0).getBlock().setType(Material.GLASS);
        loc.clone().add(1, 0, 0).getBlock().setType(Material.GLASS);
        loc.clone().add(-1, 0, 0).getBlock().setType(Material.GLASS);
        loc.clone().add(0, 0, 1).getBlock().setType(Material.GLASS);
        loc.clone().add(0, 0, -1).getBlock().setType(Material.GLASS);
        loc.clone().add(1, 1, 0).getBlock().setType(Material.GLASS);
        loc.clone().add(-1, 1, 0).getBlock().setType(Material.GLASS);
        loc.clone().add(0, 1, 1).getBlock().setType(Material.GLASS);
        loc.clone().add(0, 1, -1).getBlock().setType(Material.GLASS);
    }
    private void buildAllGlassCages() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().severe("Il mondo '" + worldName + "' non esiste!");
            return;
        }

        for (Location loc : spawnLocations) {
            buildGlassCage(world, loc);
            //System.out.println("Costruzioneeee");
        }
    }



    private void teleportPlayersToSpawns() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().severe("Il mondo '" + worldName + "' non esiste!");
            return;
        }

        List<Location> availableSpawns = new ArrayList<>(spawnLocations);
        Collections.shuffle(availableSpawns); // Mischia le posizioni disponibili


        for (Player player : playerQueue) {
            if (availableSpawns.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Non ci sono abbastanza posizioni di spawn disponibili!");
                continue;
            }

            Location spawnLocation = availableSpawns.remove(0);
            player.teleport(spawnLocation);
            player.setHealth(20);
            player.setSaturation(20);
            player.setGameMode(GameMode.ADVENTURE);

        }

        availableSpawns.clear();


    }



    private void startSpawnCountdown() {
        new BukkitRunnable() {
            int countdown = 5;

            @Override
            public void run() {
                if (countdown > 0) {
                    QueueListener3.sendMessageToQueue(worldName,ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.YELLOW +  " Mancano " + countdown + " secondi per la caduta");
                    for (Player queuedPlayer : playerQueue) {
                        queuedPlayer.playSound(queuedPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                    }
                    countdown--;
                } else {
                    for (Location loc : spawnLocations) {
                        removeBottomGlassBlock(loc);
                    }
                    for (Player queuedPlayer : playerQueue) {
                        queuedPlayer.setGameMode(GameMode.SURVIVAL);
                    }
                    grantFallDamageImmunity();
                    this.cancel();
                }
            }
        }.runTaskTimer(ExoticChallenges.getPlugin(), 0L, 20L);
    }

    private void grantFallDamageImmunity() {
        fallDamageImmunePlayers.addAll(playerQueue);

        new BukkitRunnable() {
            @Override
            public void run() {
                fallDamageImmunePlayers.clear();
                voteSystemm.giveNetherStarToPlayers();
            }
        }.runTaskLater(ExoticChallenges.getPlugin(), 100L); // 100L = 5 secondi
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && fallDamageImmunePlayers.contains(player) && event.getEntity().getWorld().getName().startsWith("lobby")) {
                event.setCancelled(true);
            }
        }
    }

    private void removeBottomGlassBlock(Location loc) {
        loc.clone().add(0, -1, 0).getBlock().setType(Material.AIR);
    }


    public int getCurrentRound() {
        return currentRound;
    }

    public String getWorldName(){
        return worldName;
    }

    public void startRounds() {
        if (running) return;
        buildAllGlassCages();
        teleportPlayersToSpawns();
        startSpawnCountdown();
        voteSystemm = new voteSystem(playerQueue);
        //lobbysGenerator = new LobbysGenerator();
        running = true;
        currentRound = 1;
        startRoundTimer();
        /*QueueListener3.sendMessageToQueue(worldName,ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.GREEN +  " Inizio del " + currentRound + " round!");
        new BukkitRunnable() {
            int timeLeft = 300;  //300 secondi = 5 minuti

            @Override
            public void run() {
                if (!running) {
                    cancel();
                    return;
                }

                if (currentRound < maxRounds) {
                    advanceRound();
                } else {
                    stopRounds();
                }
            }
        }.runTaskTimer(ExoticChallenges.getPlugin(), 20 * 60 * 5, 20 * 60 * 5); // Cambia 5 con il numero di minuti per round*/
    }

    public void advanceRound() {
        if (!running) return;

        if (currentRound < maxRounds) {
            //QueueListener3.sendMessageToQueue(worldName,"Inizio advancedRounds");
            currentRound++;
            startSpawnCountdown();
            startRoundTimer();
        } else {
            stopRounds();
        }
    }

    private void startRoundTimer() {
        timeRound = Settings.getInstance().getTime_rounds();

        QueueListener3.sendMessageToQueue(worldName,ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.GREEN +  " Inizio del " + currentRound + " round!");
        //LobbysGenerator.startRound(worldName);
        /*Location loc = new Location(Bukkit.getWorld(worldName), LobbysGenerator.num, 67, LobbysGenerator.num, 0, 0);
        for (Player queuedPlayer : playerQueue) {
            queuedPlayer.teleport(loc);
        }*/

        new BukkitRunnable() {
            int timeLeft = Settings.getInstance().getTime_rounds(); // 5 minuti (300 secondi)

            @Override
            public void run() {
                if (!running) {
                    cancel();
                    return;
                }

                if (timeLeft > 0) {
                    if (timeLeft < 6) {
                        QueueListener3.sendMessageToQueue(worldName,ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.YELLOW +  " Mancano " + timeLeft + " secondi a fine del round " + currentRound + "!");
                        for (Player queuedPlayer : playerQueue) {
                            queuedPlayer.playSound(queuedPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                        }


                    }
                    timeRound--;
                    timeLeft--;
                } else {
                    QueueListener3.sendMessageToQueue(worldName,ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.RED +  " Fine del " + currentRound + " round!");
                    //advanceRound();
                    startTimeWaitTimer();
                    cancel();
                }
            }
        }.runTaskTimer(ExoticChallenges.getPlugin(), 0L, 20L); // 20L = 1 secondo
    }



    private void startTimeWaitTimer() {
        for (Player queuedPlayer : playerQueue) {
            queuedPlayer.getInventory().clear();
            //System.out.println("CIAOOOOOOOOOOOOOOOOOOOOOOOOOOO");

        }
        new BukkitRunnable() {
            int count = 2;

            @Override
            public void run() {
                if (count > 0) {
                    for (Player queuedPlayer : playerQueue) {
                        QueueListener3.gui(queuedPlayer, queuedPlayer.getWorld().getName());
                    }
                    count--;
                }else {
                    this.cancel();
                }
            }
        }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 1L);

        QueueListener3.sendMessageToQueue(worldName,ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.GREEN +  " Ci sono " + Settings.getInstance().getTime_between_two_rounds() + " secondi al termine della pausa...");
        LobbysGenerator.destroyChunk(worldName);
        buildAllGlassCages();
        teleportPlayersToSpawns();
        LobbysGenerator.genChunk(worldName);
        new BukkitRunnable() {
            int timeLeft = Settings.getInstance().getTime_between_two_rounds(); // 5 minuti (300 secondi)

            @Override
            public void run() {
                if (!running) {
                    cancel();
                    return;
                }

                if (timeLeft > 0) {
                    /*for (Player queuedPlayer : playerQueue) {
                        QueueListener3.gui(queuedPlayer, queuedPlayer.getWorld().getName());

                    }*/
                    System.out.println("Remaining " + timeLeft + " seconds of waiting time for " + worldName + " lobby...");
                    if (timeLeft < 6) {
                        QueueListener3.sendMessageToQueue(worldName,ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.YELLOW +  " Mancano " + timeLeft + " secondi per il tempo di pausa!");
                        for (Player queuedPlayer : playerQueue) {
                            queuedPlayer.playSound(queuedPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                        }


                    }
                    timeLeft--;
                } else if(timeLeft == 0){
                    //QueueListener3.sendMessageToQueue(worldName,ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.RED +  " Fine del " + currentRound + " round!");
                    //LobbysGenerator.cancelChunks(worldName);
                    //QueueListener3.sendMessageToQueue(worldName,"ciaoooo");
                    advanceRound();
                    QueueListener3.open = false;
                    ChallengesInfoGui.open = false;
                    cancel();
                }
            }
        }.runTaskTimer(ExoticChallenges.getPlugin(), 0L, 20L); // 20L = 1 secondo
    }



    public void stopRounds() {
        if (!running) return;

        running = false;
        currentRound = 0;
        System.out.println(ChatColor.RED + "[ExoticChallenge] Il sistema di rounds è stato fermato per " + worldName + ".");
        for (Player player : playerQueue) {
            player.sendMessage(ChatColor.GOLD + "[" +  worldName  +  "]" + ChatColor.RED +  " Partita conclusa.");

        }
    }




}
