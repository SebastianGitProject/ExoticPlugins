package org.yastral.exoticchallenges.vote;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.hook.Packets;
import org.yastral.exoticchallenges.listeners.QueueListener3;
import org.yastral.exoticchallenges.listeners.RoundSystem;
import org.yastral.exoticchallenges.lobbydata.LobbysGenerator;

import java.util.*;

public class voteSystem implements Listener {
    private final Queue<Player> playerQueue;
    private final Map<Player, Boolean> voteMap;
    private final Set<Player> fallDamageImmunePlayers;


    public voteSystem(Queue<Player> playerQueue) {
        this.playerQueue = playerQueue;
        this.voteMap = new HashMap<>();
        this.fallDamageImmunePlayers = new HashSet<>();
        for (Player player : playerQueue) {
            voteMap.put(player, false);
        }
        //giveNetherStarToPlayers();
        ExoticChallenges.getPlugin().getServer().getPluginManager().registerEvents(this, ExoticChallenges.getPlugin());
    }

    public void giveNetherStarToPlayers() {
        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = netherStar.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Change Area");
            meta.setUnbreakable(true);
            meta.setEnchantmentGlintOverride(true);
            meta.setLore(Arrays.asList(" ",ChatColor.GOLD + "Right click to vote for change the area"," "));
            netherStar.setItemMeta(meta);
        }

        for (Player player : playerQueue) {
            player.getInventory().setItem(8, netherStar); // 9th slot is index 8
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() != null && event.getItem().getType() == Material.NETHER_STAR && event.getPlayer().getWorld().getName().startsWith("lobby")) {
            if (event.getItem().getItemMeta() != null && (ChatColor.GREEN + "Change Area").equals(event.getItem().getItemMeta().getDisplayName())) {
                event.setCancelled(true);
                if(RoundSystem.timeRound > 30){
                    if (!voteMap.get(player)) {
                        voteMap.put(player, true);
                        player.sendMessage(ChatColor.GREEN + "You voted for changing area!");

                        int voteCount = (int) voteMap.values().stream().filter(v -> v).count();
                        int playerCount = playerQueue.size();
                        QueueListener3.sendMessageToQueue(player.getWorld().getName(), ChatColor.GOLD + "[" +  player.getWorld().getName()  +  "]" + ChatColor.YELLOW +  " Vote: (" + voteCount + "/" + playerCount + ")");
                        if (voteCount >= Math.ceil(playerCount / 2.0)) {
                            executeChangeAreaEvent();
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You already voted!");
                    }
                }else if(RoundSystem.timeRound <= 30){
                    player.sendMessage(ChatColor.RED + "You cannot vote if there are less than 30 seconds left until the end of the round");
                }

            }
        }
    }

    private void executeChangeAreaEvent() {
        String worldName = "";
        for (Player player : playerQueue) {
            Location currentLocation = player.getLocation();
            Location newLocation = currentLocation.clone().add(0, 40, 0);
            player.teleport(newLocation);
            Packets.spawnCreeperAndSetCamera(player);
            player.sendMessage(ChatColor.GOLD + "Regenerating area...");
            worldName = player.getWorld().getName();
        }
        LobbysGenerator.destroyChunk(worldName);
        LobbysGenerator.genChunk(worldName);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : playerQueue) {
                    Packets.resetCameraAndRemoveCreeper(player);
                }
                grantFallDamageImmunity();

            }
        }.runTaskLater(ExoticChallenges.getPlugin(), 240L); // 100L = 5 secondi
        voteMap.clear();
        // Implement the actual event/action to be performed here
    }

    private void grantFallDamageImmunity() {
        fallDamageImmunePlayers.addAll(playerQueue);

        new BukkitRunnable() {
            @Override
            public void run() {
                fallDamageImmunePlayers.clear();
            }
        }.runTaskLater(ExoticChallenges.getPlugin(), 340L); // 100L = 5 secondi
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.NETHER_STAR && event.getWhoClicked().getWorld().getName().startsWith("lobby")) {
            if (event.getCurrentItem().getItemMeta() != null && (ChatColor.GREEN + "Change Area").equals(event.getCurrentItem().getItemMeta().getDisplayName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR && event.getPlayer().getWorld().getName().startsWith("lobby")) {
            if ((ChatColor.GREEN + "Change Area").equals(event.getItemDrop().getItemStack().getItemMeta().getDisplayName())) {
                event.setCancelled(true);
            }
        }
    }

    public void removePlayer(Player player) {
        voteMap.remove(player);
    }
}
