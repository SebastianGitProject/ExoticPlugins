package org.yastral.exoticchallenges.lobbydata.commands;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.players.OpList;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.SleepStatus;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R4.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorldBorder;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.gui.ChallengesGui;
import org.yastral.exoticchallenges.listeners.settings.Settings;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class TestCommand implements CommandExecutor{

    private int x = 0;
    private int z = 0;
    private int yy = 0;
    private Location loc1;
    private Location loc2;
    private Location loc3;
    private Location loc4;
    private final ExoticChallenges plugin;
    private final Map<String, Material[][][]> savedBlockArrays = new HashMap<>();

    public TestCommand(ExoticChallenges plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(s.equalsIgnoreCase("test")){
            Player player = (Player) sender;
            String worldNname = player.getWorld().getName();
            if(args[0].equalsIgnoreCase("place")){
                //x = Integer.parseInt(args[1]);
                //z = Integer.parseInt(args[2]);
                loc1 = new Location(Bukkit.getWorld(worldNname), 1000, 36, 1000, 0, 0);




                new BukkitRunnable() {
                    int count = 4;

                    @Override
                    public void run() {
                        if (count > 0) {
                            player.teleport(loc1);
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
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/schem load chunk" + random);

                                        count--;
                                    }else {
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldNname);
                                        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",60," + x);
                                        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + z + ",60," + z);
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",-24," + z); //far teletrasportare 60 blocchi in più rispetto a y=-24
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x + ",-24," + z);
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste -be");
                                        loc1.subtract(16, 0 ,0);
                                        this.cancel();
                                    }
                                }
                        }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 20L);
                                    count--;
                                }else {
                            //player.sendMessage("finita la prima riga");
                                    this.cancel();
                            loc2 = new Location(Bukkit.getWorld(worldNname), 1000, 36, 984, 0, 0);




                            new BukkitRunnable() {
                                int count = 4;

                                @Override
                                public void run() {
                                    if (count > 0) {
                                        player.teleport(loc2);
                                        int random = (int) (Math.random() * 5) + 1;
                                        Location y = player.getLocation();

                                        x = (int)y.getX(); //convert double into int
                                        yy = (int)y.getY();
                                        z = (int)y.getZ();
                                        new BukkitRunnable() {
                                            int count = 1;

                                            @Override
                                            public void run() {
                                                if (count > 0) {
                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/schem load chunk" + random);

                                                    count--;
                                                }else {
                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldNname);
                                                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",60," + x);
                                                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + z + ",60," + z);
                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",-24," + z); //far teletrasportare 60 blocchi in più rispetto a y=-24
                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x + ",-24," + z);
                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste -be");
                                                    loc2.subtract(16, 0 ,0);
                                                    this.cancel();
                                                }
                                            }
                                        }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 20L);
                                        count--;
                                    }else {
                                        //player.sendMessage("finita la seconda riga");
                                        this.cancel();
                                        loc3 = new Location(Bukkit.getWorld(worldNname), 1000, 36, 968, 0, 0);




                                        new BukkitRunnable() {
                                            int count = 4;

                                            @Override
                                            public void run() {
                                                if (count > 0) {
                                                    player.teleport(loc3);
                                                    int random = (int) (Math.random() * 5) + 1;
                                                    Location y = player.getLocation();

                                                    x = (int)y.getX(); //convert double into int
                                                    yy = (int)y.getY();
                                                    z = (int)y.getZ();
                                                    new BukkitRunnable() {
                                                        int count = 1;

                                                        @Override
                                                        public void run() {
                                                            if (count > 0) {
                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/schem load chunk" + random);

                                                                count--;
                                                            }else {
                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldNname);
                                                                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",60," + x);
                                                                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + z + ",60," + z);
                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",-24," + z); //far teletrasportare 60 blocchi in più rispetto a y=-24
                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x + ",-24," + z);
                                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste -be");
                                                                loc3.subtract(16, 0 ,0);
                                                                this.cancel();
                                                            }
                                                        }
                                                    }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 20L);
                                                    count--;
                                                }else {
                                                    //player.sendMessage("finita la terza riga");
                                                    this.cancel();
                                                    loc4 = new Location(Bukkit.getWorld(worldNname), 1000, 36, 952, 0, 0);




                                                    new BukkitRunnable() {
                                                        int count = 4;

                                                        @Override
                                                        public void run() {
                                                            if (count > 0) {
                                                                player.teleport(loc4);
                                                                int random = (int) (Math.random() * 5) + 1;
                                                                Location y = player.getLocation();

                                                                x = (int)y.getX(); //convert double into int
                                                                yy = (int)y.getY();
                                                                z = (int)y.getZ();
                                                                new BukkitRunnable() {
                                                                    int count = 1;

                                                                    @Override
                                                                    public void run() {
                                                                        if (count > 0) {
                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/schem load chunk" + random);

                                                                            count--;
                                                                        }else {
                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldNname);
                                                                            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",60," + x);
                                                                            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + z + ",60," + z);
                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",-24," + z); //far teletrasportare 60 blocchi in più rispetto a y=-24
                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x + ",-24," + z);
                                                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste -be");
                                                                            loc4.subtract(16, 0 ,0);
                                                                            this.cancel();
                                                                        }
                                                                    }
                                                                }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 20L);
                                                                count--;
                                                            }else {
                                                                //player.sendMessage("finita la quarta riga");
                                                                this.cancel();
                                                            }
                                                        }
                                                    }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 30L);
                                                }
                                            }
                                        }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 30L);

                                    }
                                }
                            }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 30L);
                                }
                            }
                }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 30L);



                return true;
            }else if(args[0].equalsIgnoreCase("destroy")){
                if(x == 0 || z == 0){
                    player.sendMessage(ChatColor.RED + "Non hai incollato nessun chunk precedentemente!");
                    return true;
                }else{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldNname);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 975,-24,976");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 976,-24,975");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/outset -v 285");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/outset -h 34");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/set air");
                    /*Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/schem load ");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldNname);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + z);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste");*/
                    return true;
                }

            }else if(args[0].equalsIgnoreCase("bibbo")){
                //World world = Bukkit.getWorld("world");
                //world.regenerateChunk(1, 1);
                ChallengesGui.gui(player);
                return true;
            }else if(args[0].equalsIgnoreCase("copyblocks")){
                if (sender instanceof Player) {
                    if (args.length == 3) {
                        try {
                            int distance = Integer.parseInt(args[1]);
                            String fileName = args[2];

                            // Ottieni la posizione del giocatore come centro
                            Location center = player.getLocation();
                            World world = center.getWorld();

                            // Definiamo i limiti del cuboide
                            int minX = center.getBlockX() - distance;
                            int maxX = center.getBlockX() + distance;
                            int minY = center.getBlockY() - 30;
                            int maxY = center.getBlockY() + 30;
                            int minZ = center.getBlockZ() - distance;
                            int maxZ = center.getBlockZ() + distance;

                            // Calcola la dimensione dell'array
                            int sizeX = maxX - minX + 1;
                            int sizeY = maxY - minY + 1;
                            int sizeZ = maxZ - minZ + 1;

                            // Crea l'array 3D
                            Material[][][] blockArray = new Material[sizeX][sizeY][sizeZ];

                            // Copia i blocchi nell'array
                            for (int x = minX; x <= maxX; x++) {
                                for (int y = minY; y <= maxY; y++) {
                                    for (int z = minZ; z <= maxZ; z++) {
                                        Location loc = new Location(world, x, y, z);
                                        Material blockType = loc.getBlock().getType();
                                        blockArray[x - minX][y - minY][z - minZ] = blockType;
                                    }
                                }
                            }

                            // Salva l'array 3D in un file
                            saveBlockArrayToFile(fileName, blockArray);
                            savedBlockArrays.put(fileName, blockArray);

                            // Messaggio di conferma
                            player.sendMessage("Blocchi copiati e salvati nel file: " + fileName);

                            return true;

                        } catch (NumberFormatException e) {
                            player.sendMessage("Per favore inserisci un numero valido.");
                            return false;
                        }
                    } else {
                        player.sendMessage("Usa: /copyblocks <distanza> <nomefile>");
                        return false;
                    }
                } else {
                    sender.sendMessage("Solo i giocatori possono eseguire questo comando.");
                    return false;
                }
            }else if(args[0].equalsIgnoreCase("placeblocks")){
                if (sender instanceof Player) {
                    if (args.length == 2) {
                        String fileName = args[1];

                        // Carica l'array 3D dal file
                        Material[][][] blockArray = loadBlockArrayFromFile(fileName);

                        if (blockArray == null) {
                            player.sendMessage("Il file specificato non esiste o non è valido.");
                            return false;
                        }

                        // Ottieni la posizione del giocatore come centro
                        Location center = player.getLocation();
                        World world = center.getWorld();

                        // Posiziona i blocchi dal file
                        int centerX = center.getBlockX();
                        int centerY = center.getBlockY();
                        int centerZ = center.getBlockZ();

                        for (int x = 0; x < blockArray.length; x++) {
                            for (int y = 0; y < blockArray[x].length; y++) {
                                for (int z = 0; z < blockArray[x][y].length; z++) {
                                    Location loc = new Location(world, centerX + x, centerY + y, centerZ + z);
                                    Material blockType = blockArray[x][y][z];
                                    loc.getBlock().setType(blockType);
                                }
                            }
                        }

                        player.sendMessage("Blocchi posizionati dal file: " + fileName);

                        return true;
                    } else {
                        player.sendMessage("Usa: /placeblocks <nomefile>");
                        return false;
                    }
                } else {
                    sender.sendMessage("Solo i giocatori possono eseguire questo comando.");
                    return false;
                }
            }
            }



         return false;
    }

    private void saveBlockArrayToFile(String fileName, Material[][][] blockArray) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ExoticChallenges.getInstance().getDataFolder() + File.separator + fileName + ".dat"))) {
            oos.writeObject(blockArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Material[][][] loadBlockArrayFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ExoticChallenges.getInstance().getDataFolder() + File.separator + fileName + ".dat"))) {
            return (Material[][][]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
