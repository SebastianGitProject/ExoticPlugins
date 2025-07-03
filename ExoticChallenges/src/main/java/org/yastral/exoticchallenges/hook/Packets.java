package org.yastral.exoticchallenges.hook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.gui.ChallengesGui;
import org.yastral.exoticchallenges.gui.ChallengesInfoGui;
import org.yastral.exoticchallenges.listeners.QueueListener3;


public class Packets implements Listener {
    private static ProtocolManager manager;
    private static Map<UUID, Entity> playerCreeperMap = new HashMap<>();

    public static void register() {

        manager = ProtocolLibrary.getProtocolManager();

        manager.addPacketListener(new PacketAdapter(ExoticChallenges.getInstance(), PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                String message = event.getPacket().getStrings().read(0);

                if (message.equalsIgnoreCase("vision")) {

                    spawnCreeperAndSetCamera(player);

                } else if (message.equalsIgnoreCase("out")) {
                    resetCameraAndRemoveCreeper(player);
                }
            }
        });



    }


    public static void registerInventory(){

        manager = ProtocolLibrary.getProtocolManager();

        manager.addPacketListener(new PacketAdapter(ExoticChallenges.getPlugin(), ListenerPriority.NORMAL,
                com.comphenix.protocol.PacketType.Play.Client.CLOSE_WINDOW) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();


                // Verifica se il titolo dell'inventario è "test"
                if (QueueListener3.open == true || ChallengesInfoGui.open == true) {
                    // Riapri la GUI
                    Bukkit.getScheduler().runTaskLater(ExoticChallenges.getPlugin(), () -> openGui(player), 1L);
                }
            }
        });


    }

    private static void openGui(Player player) {
        // Definizione dello schema della GUI
        QueueListener3.gui(player, player.getWorld().getName());
    }


    public static void registerMovement(){
        manager = ProtocolLibrary.getProtocolManager();

        manager.addPacketListener(new PacketAdapter(ExoticChallenges.getPlugin(), ListenerPriority.NORMAL,
                PacketType.Play.Client.POSITION_LOOK) {

            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();


                // Verifica se il titolo dell'inventario è "test"
                if (ChallengesGui.open == true) {
                    // Riapri la GUI
                    event.setCancelled(true);
                }
                if (ChallengesInfoGui.open == true) {
                    // Riapri la GUI
                    event.setCancelled(true);
                }
            }
        });
    }

    @EventHandler
    public void onMov(PlayerMoveEvent event){
        if (ChallengesGui.open == true) {
            // Riapri la GUI
            event.setCancelled(true);
        }
    }



    public static void spawnCreeperAndSetCamera(Player player) {
        Location location = player.getLocation();
        location.setYaw(0);
        location.setPitch(90);//getLocation().clone().add(player.getLocation().getDirection().multiply(2));
        /*Location loc = player.getLocation();
        loc.setPitch(90);
        loc.setYaw(0);
        player.teleport(loc);*/
        // Sposta l'aggiunta dell'entità sul thread principale di Minecraft
        new BukkitRunnable() {
            @Override
            public void run() {

                Skeleton skeleton = (Skeleton) player.getWorld().spawnEntity(location, EntityType.SKELETON);

                // Make the creeper invisible and without AI
                skeleton.setInvisible(true);
                skeleton.setAI(false);
                skeleton.setInvulnerable(true);
                player.setInvisible(true);

                // Store the creeper in the map
                playerCreeperMap.put(player.getUniqueId(), skeleton);

                // Send the camera packet
                sendCameraPacket(player, skeleton);
                //player.sendMessage("You are now viewing through the eyes of a creeper!");
            }
        }.runTask(ExoticChallenges.getInstance());
    }
    public static void resetCameraAndRemoveCreeper(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Entity skeleton = playerCreeperMap.remove(player.getUniqueId());
                if (skeleton != null) {
                    sendCameraPacket(player, player); // Reset camera to player
                    skeleton.remove();
                    //player.sendMessage("You are back to your own view and the creeper has been removed.");
                    player.setInvisible(false);
                } else {
                    //player.sendMessage("No creeper found to reset the view.");
                }

            }
        }.runTask(ExoticChallenges.getInstance());
    }
    public static void sendCameraPacket(Player player, Entity targetEntity) {
        PacketContainer cameraPacket = manager.createPacket(PacketType.Play.Server.CAMERA);

        cameraPacket.getIntegers().write(0, targetEntity.getEntityId());

        try {
            manager.sendServerPacket(player, cameraPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
