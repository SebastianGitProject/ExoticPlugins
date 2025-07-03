package org.yastral.exoticchallenges.lobbydata.commands;

import net.minecraft.network.protocol.game.PacketPlayOutCamera;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.monster.EntityZombie;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.minecraft.world.entity.EntityTypes;
import org.jetbrains.annotations.NotNull;
import org.yastral.exoticchallenges.ExoticChallenges;

import java.lang.reflect.Field;
import java.util.Random;

public class Test2Command implements CommandExecutor {
    private int x = 0;
    private int z = 0;
    private int yy = 0;
    private Location loc1;
    private Location loc2;
    private Location loc3;
    private Location loc4;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            if(s.equalsIgnoreCase("boh")) {
                if (args[0].equalsIgnoreCase("world")) {
                    Player player = (Player) sender;
                    World worldy = player.getWorld();
                    WorldCreator wc = new WorldCreator("Signorino");
                    wc.environment(World.Environment.NORMAL);
                    wc.generateStructures(false);
                    Bukkit.createWorld(wc);
                    World world = Bukkit.getWorld("Signorino");
                    Location loc = new Location(world, 0, 0, 0, 0, 0);
                    player.teleport(loc);

                    new BukkitRunnable() {
                        int timeLeft = 10; //rimanenti 297

                        @Override
                        public void run() {

                            if (timeLeft > 0) {
                                //Random random = new Random();
                                //int coordsX = (int) ((Math.random() * (10000 - 2000)) + 2000);
                                //int coordsZ = (int) ((Math.random() * (10000 - 2000)) + 2000);
                                Location randomLocation = getRandomChunkCenter(world);
                                player.teleport(randomLocation);
                                //Bukkit.dispatchCommand(player, "tp " + coordsX + " 40 " + coordsZ);
                                Bukkit.dispatchCommand(player, "/chunk");
                                Bukkit.dispatchCommand(player, "/outset -h 32");
                                Bukkit.dispatchCommand(player, "/copy -be");
                                Bukkit.dispatchCommand(player, "/schem save chunk" + timeLeft);
                                player.sendMessage("Schem " + timeLeft + " Salvato.");
                                timeLeft--;
                            } else {
                                player.sendMessage("Completato");
                                cancel();
                            }
                        }
                    }.runTaskTimer(ExoticChallenges.getPlugin(), 0L, 80L); // 20L = 1 secondo*/


                    //player.sendMessage("Sei stato teletrasportato a coordinate casuali al centro di un chunk!");
                    return true;
                }else if(args[0].equalsIgnoreCase("try")){
                    Player player = (Player) sender;
                    String worldNname = player.getWorld().getName();


                    loc4 = new Location(Bukkit.getWorld(worldNname), 1000, 36, 1000, 0, 0);

                    new BukkitRunnable() {
                        int count = 4;

                        @Override
                        public void run() {
                            if (count > 0) {
                                player.teleport(loc4);
                                int random = (int) (Math.random() * 1000) + 1;
                                Location y = player.getLocation();

                                x = (int)y.getX(); //convert double into int
                                yy = (int)y.getY();
                                z = (int)y.getZ();
                                new BukkitRunnable() {
                                    int count = 1;

                                    @Override
                                    public void run() {
                                        if (count > 0) {
                                            try {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/schem load chunk" + random);
                                                count--;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }else {
                                            try {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldNname);
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",-24," + z);
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x + ",-24," + z);
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste -be");
                                                loc4.subtract(16, 0, 0);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 10L);
                                count--;
                            }else {
                                //player.sendMessage("finita la quarta riga");
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 15L);
                    return true;

            }
            }
        }else {
            sender.sendMessage("Questo comando può essere eseguito solo da un giocatore.");
            return false;
        }


        return false;
    }

    private Location getRandomChunkCenter(World world) {
        Random random = new Random();

        // Generazione di coordinate casuali dei chunk
        int chunkX = (int) ((Math.random() * (10000 - 2000)) + 2000);
        int chunkZ = (int) ((Math.random() * (10000 - 2000)) + 2000);

        // Caricamento del chunk
        Chunk chunk = world.getChunkAt(chunkX, chunkZ);

        // Calcolo del centro del chunk
        int blockX = chunk.getX() * 16 + 8;
        int blockZ = chunk.getZ() * 16 + 8;
        int blockY = world.getHighestBlockYAt(blockX, blockZ);

        return new Location(world, blockX, blockY, blockZ);
    }
    }

