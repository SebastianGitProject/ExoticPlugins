package org.yastral.exoticchallenges.lobbydata;

import net.lingala.zip4j.ZipFile;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.listeners.QueueListener3;
import org.yastral.exoticchallenges.listeners.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

import static com.mojang.logging.LogUtils.getLogger;

public class LobbysGenerator{

    private static int x = 0;
    private static int z = 0;
    private static int yy = 0;
    private static Location loc1;
    private static Location loc2;
    private static Location loc3;
    private static Location loc4;
    private final Queue<Player> playerQueue;
    //private static String worldName ;
    private static int numFile = 0;
    private static boolean startExtractFiles = false;
    private static Set<Integer> uniqueRandomNumbers = new HashSet<>();


    public LobbysGenerator(Queue<Player> playerQueue){
        this.playerQueue = playerQueue;
        //this.worldName = worldName;
    }

    public static boolean extractFileZip(){
        File path = new File(ExoticChallenges.getInstance().getDataFolder() + "/lobbygen/dataChunk");
        if (!path.exists()) {
            path.mkdir();
        }

        File directorysu = new File("plugins/FastAsyncWorldEdit/", "schematics");
        int fileCheck = 0;
        // Ottieni tutti i file nella cartella
        File[] filessy = directorysu.listFiles();
        if(filessy.length == 0 || filessy == null){
            startExtractFiles = false;
            System.out.println(ChatColor.RED + "Non ci sono file nella cartella di fawe.");
            return false;
        }else if(filessy.length == 1){
            for (File filey : filessy) {
                if(filey.getName().equals(Settings.getInstance().getName_schem())){
                    startExtractFiles = true;
                }else{
                    startExtractFiles = false;
                    System.out.println(ChatColor.RED + "[ExoticChallenges] Il file schematic per le lobby non ha lo stesso nome inserito in settings.yml");
                    return false;
                }
            }
        }

        //Set<Integer> uniqueRandomNumbers = new HashSet<>();

        //generati 100 numeri randomici unici che vanno da 1 a 1000
        Random random = new Random();
        int numrounds = Settings.getInstance().getNum_rounds();
        while (uniqueRandomNumbers.size() < 16 * numrounds) {
            int randomNumber = random.nextInt(1000) + 1;
            uniqueRandomNumbers.add(randomNumber);
            //System.out.println("Numero generato: " + randomNumber);
        }


        //System.out.println("Fuori dal ciclo");
        new BukkitRunnable() {
            int times = 16 * numrounds;
            //int num = 0;

            @Override
            public void run() {
                if (times > 0) {

                    //vengono presi 16 di quei numeri randomici unici(i numeri presi non vengono ripetuti)
                    Set<Integer> extractedNumbers = new TreeSet<>();
                    while (extractedNumbers.size() < 16 * numrounds) {
                        int randomIndex = random.nextInt(uniqueRandomNumbers.size());
                        int extractedNumber = (int) uniqueRandomNumbers.toArray()[randomIndex];
                        extractedNumbers.add(extractedNumber);
                    }

                    int extractedNumber = (int) extractedNumbers.toArray()[times-1];
                    String resourcePath = "dataChunk/chunk" + extractedNumber + ".schem";
                    File dataChunkDir = new File(ExoticChallenges.getInstance().getDataFolder(), "lobbygen/dataChunk");
                    File file = new File(dataChunkDir, "chunk" + extractedNumber + ".schem");

                    if (!file.exists()) {
                        try {
                            saveResourceToFile(resourcePath, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //System.out.println("chunk" + extractedNumber + ".schem");
                    //System.out.println("Times: " + times);
                    times--;
                }else{
                    File directory = new File(ExoticChallenges.getInstance().getDataFolder(), "lobbygen/dataChunk");
                    // Ottieni tutti i file nella cartella
                    File[] files = directory.listFiles();
                    // Stampa i nomi dei file
                    for (File filey : files) {
                        if(filey.getName().startsWith("chunk")){
                            File destinationPath = new File("plugins\\FastAsyncWorldEdit\\schematics");
                            copyFilesFaster.copyFile2(filey, destinationPath);
                            //filey.renameTo(new File("plugins\\FastAsyncWorldEdit\\schematics", filey.getName()));
                        }
                    }

                    System.out.println(ChatColor.GREEN + "File messi in fawe");
                    File directorys = new File(ExoticChallenges.getInstance().getDataFolder(), "lobbygen/dataChunk");
                    // Ottieni tutti i file nella cartella
                    File[] filess = directorys.listFiles();
                    // Stampa i nomi dei file
                    for (File filey : filess) {
                        if(filey.getName().startsWith("chunk")){

                            filey.delete();
                        }
                    }
                    System.out.println(ChatColor.GREEN + "File cancellati da datachunk");
                    cancel();
                }
            }
        }.runTaskTimer(ExoticChallenges.getPlugin(), 0L, 0L); // 20L = 1 secondo


        return true;
        /*new BukkitRunnable() {
            int times = 1000;//64 * Settings.getInstance().getNum_rounds() ;


            @Override
            public void run() {
                if (times > 0) {
                    new BukkitRunnable() {
                        int times = 1;

                        @Override
                        public void run() {
                            if (times > 0) {

                                while (uniqueNumbers.size() < 1000) {

                                }
                                int rnd = (int) ((Math.random() * (1000 - 1)) + 1);
                                //ExoticChallenges.getInstance().saveResource("dataChunk/chunk" + rnd + ".schem", true);
                                String resourcePath = "dataChunk/chunk" + rnd + ".schem";
                                File dataChunkDir = new File(ExoticChallenges.getInstance().getDataFolder(), "lobbygen/dataChunk");
                                File file = new File(dataChunkDir, "chunk" + rnd + ".schem");

                                if (!file.exists()) {
                                    try {
                                        saveResourceToFile(resourcePath, file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                File directory = new File(ExoticChallenges.getInstance().getDataFolder(), "lobbygen/dataChunk");
                                // Ottieni tutti i file nella cartella
                                File[] files = directory.listFiles();
                                // Stampa i nomi dei file
                                for (File filey : files) {
                                    if(filey.getName().startsWith("chunk")){

                                        filey.renameTo(new File("plugins/FastAsyncWorldEdit/schematics", filey.getName()));
                                    }
                                }
                                File directorys = new File(ExoticChallenges.getInstance().getDataFolder(), "lobbygen/dataChunk");
                                // Ottieni tutti i file nella cartella
                                File[] filess = directorys.listFiles();
                                // Stampa i nomi dei file
                                for (File filey : filess) {
                                    if(filey.getName().startsWith("chunk")){

                                        filey.delete();
                                    }
                                }
                                times--;
                            } else {

                                cancel();
                            }
                        }
                    }.runTaskTimer(ExoticChallenges.getPlugin(), 0L, 0L); // 20L = 1 secondo
                    times--;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(ExoticChallenges.getPlugin(), 0L, 0L); // 20L = 1 secondo*/

        //getLogger().info("Extracted schem file to: " + file.getPath());
    }

    private static void saveResourceToFile(String resourcePath, File destination) throws IOException {
        try (InputStream in = ExoticChallenges.getInstance().getResource(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in jar");
            }
            Files.copy(in, destination.toPath());
        }
    }

    public static void genChunk(String worldName){
        loc1 = new Location(Bukkit.getWorld(worldName), 1000, 36, 1000, 0, 0);
        Random randomy = new Random();

        new BukkitRunnable() {
            int count = 4;

            @Override
            public void run() {
                if (count > 0) {
                    //player.teleport(loc1);
                    //int random = (int) (Math.random() * (1000 - 1)) + 1;
                    int randomIndex = randomy.nextInt(uniqueRandomNumbers.size());
                    int random = (int) uniqueRandomNumbers.toArray()[randomIndex];
                    //Location y = player.getLocation();
                    Location y = loc1;

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
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldName);
                                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",60," + x);
                                //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + z + ",60," + z);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",-24," + z); //far teletrasportare 60 blocchi in più rispetto a y=-24
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x + ",-24," + z);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste -bea");
                                loc1.subtract(16, 0 ,0);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 10L);
                    count--;
                }else {
                    //player.sendMessage("finita la prima riga");
                    this.cancel();
                    loc2 = new Location(Bukkit.getWorld(worldName), 1000, 36, 984, 0, 0);


                    new BukkitRunnable() {
                        int count = 4;

                        @Override
                        public void run() {
                            if (count > 0) {
                                //player.teleport(loc1);
                                //int random = (int) (Math.random() * (1000 - 1)) + 1;
                                int randomIndex = randomy.nextInt(uniqueRandomNumbers.size());
                                int random = (int) uniqueRandomNumbers.toArray()[randomIndex];
                                //Location y = player.getLocation();
                                Location y = loc2;

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
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldName);
                                            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",60," + x);
                                            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + z + ",60," + z);
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",-24," + z); //far teletrasportare 60 blocchi in più rispetto a y=-24
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x + ",-24," + z);
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste -bea");
                                            loc2.subtract(16, 0 ,0);
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 10L);
                                count--;
                            }else {
                                //player.sendMessage("finita la seconda riga");
                                this.cancel();
                                loc3 = new Location(Bukkit.getWorld(worldName), 1000, 36, 968, 0, 0);


                                new BukkitRunnable() {
                                    int count = 4;

                                    @Override
                                    public void run() {
                                        if (count > 0) {
                                            //player.teleport(loc1);
                                            //int random = (int) (Math.random() * (1000 - 1)) + 1;
                                            int randomIndex = randomy.nextInt(uniqueRandomNumbers.size());
                                            int random = (int) uniqueRandomNumbers.toArray()[randomIndex];
                                            //Location y = player.getLocation();
                                            Location y = loc3;

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
                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldName);
                                                        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",60," + x);
                                                        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + z + ",60," + z);
                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",-24," + z); //far teletrasportare 60 blocchi in più rispetto a y=-24
                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x + ",-24," + z);
                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste -bea");
                                                        loc3.subtract(16, 0 ,0);
                                                        this.cancel();
                                                    }
                                                }
                                            }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 10L);
                                            count--;
                                        }else {
                                            //player.sendMessage("finita la terza riga");
                                            this.cancel();
                                            loc4 = new Location(Bukkit.getWorld(worldName), 1000, 36, 952, 0, 0);


                                            new BukkitRunnable() {
                                                int count = 4;

                                                @Override
                                                public void run() {
                                                    if (count > 0) {
                                                        //player.teleport(loc1);
                                                        //int random = (int) (Math.random() * (1000 - 1)) + 1;
                                                        int randomIndex = randomy.nextInt(uniqueRandomNumbers.size());
                                                        int random = (int) uniqueRandomNumbers.toArray()[randomIndex];
                                                        //Location y = player.getLocation();
                                                        Location y = loc4;

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
                                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldName);
                                                                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",60," + x);
                                                                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + z + ",60," + z);
                                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + ",-24," + z); //far teletrasportare 60 blocchi in più rispetto a y=-24
                                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x + ",-24," + z);
                                                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/paste -bea");
                                                                    loc4.subtract(16, 0 ,0);
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
                                        }
                                    }
                                }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 15L);

                            }
                        }
                    }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 15L);
                }
            }
        }.runTaskTimer(ExoticChallenges.getPlugin(), 0, 15L);












    }

    public static void destroyChunk(String worldName){
        if(x == 0 || z == 0){
            getLogger().info(ChatColor.RED + "Non hai incollato nessun chunk precedentemente!");
        }else{
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + worldName);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 975,-24,976");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 976,-24,975");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/outset -v 285");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/outset -h 34");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/replace !glass,barrier air");
        }
    }

    public static void setWorldBorder(String worldName, double size) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            WorldBorder border = world.getWorldBorder();
            border.setCenter(976, 976);
            border.setSize(size);
            getLogger().info("World border set size to " + size + ", center:" + border.getCenter() + " for lobby: " + worldName);
        }
    }

    public static void destroyFiles(){
        File directory = new File("plugins/FastAsyncWorldEdit/schematics");
        // Ottieni tutti i file nella cartella
        File[] files = directory.listFiles();
        // Stampa i nomi dei file
        for (File filey : files) {
            if(filey.getName().startsWith("chunk")){
                filey.delete();
            }
        }

    }
}