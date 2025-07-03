package org.yastral.exoticchallenges.listeners.settings;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yastral.exoticchallenges.ExoticChallenges;

import java.io.File;
import java.io.IOException;


public class Settings {
    private final static Settings instance = new Settings();

    private File file;
    private YamlConfiguration config;

    private String join;
    private String leave;
    private int num_challenges;
    private int num_player;
    private String name_schem;
    private boolean schemFile = false;
    private int num_rounds;
    private int time_rounds;
    private int time_between_two_rounds;
    private String mode;
    private String type_mode_multiworld;




    public void load(){
        file = new File(ExoticChallenges.getInstance().getDataFolder(), "settings.yml");

        if(!file.exists()){
            ExoticChallenges.getInstance().saveResource("settings.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        //config = new YamlConfiguration();
        //config.options().parseComments(true);


        try{
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        join = config.getString("Messages.Join");
        leave = config.getString("Messages.Leave");
        num_challenges = config.getInt("Challenges.num_challenge");
        mode = config.getString("Mode_plugin.type_of_mode");
        type_mode_multiworld = config.getString("Type_mode_bungeecord.type_bungeecord");
        num_player = config.getInt("Lobby.num_player");
        name_schem = config.getString("Lobby.file_lobby");
        num_rounds = config.getInt("Lobby.num_rounds");
        time_rounds = config.getInt("Lobby.time_rounds");
        time_between_two_rounds = config.getInt("Lobby.time_between_two_rounds");

        if (name_schem == null) {
            System.out.println("Il file 'Lobby.file_lobby' non è configurato nel settings.yml");
            schemFile = false;
        } else if (!name_schem.endsWith(".schem")) {
            System.out.println("Il file '" + name_schem + "' non ha come estensione .schem (accertarsi che è un file schem di WorldEdit)");
            schemFile = false;
        } else {
            System.out.println("Il file '" + name_schem + "' è stato caricato correttamente per le lobby!");
            schemFile = true;
        }
        //getLogger().info("Il valore: " + num_challenges);
    }

    public void save(){
        try{
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value){
        config.set(path, value);
        save();

    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = String.valueOf(join);

        set("Messages.Join", join);
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = String.valueOf(leave);

        set("Messages.Leave", leave);
    }

    public int getNumChallenges() {
        return num_challenges;
    }

    public void setNumchallenges(int num_challenges) {
        this.num_challenges = num_challenges;

        set("Challenges.num_challenge", num_challenges);
    }

    public int getNum_player() {
        return num_player;
    }

    public void setNum_player(int num_player) {
        this.num_player = num_player;

        set("Lobby.num_player", num_player);
    }

    public String getName_schem() {
        return name_schem;
    }

    public void setName_schem(String name_schem) {
        this.name_schem = name_schem;

        set("Lobby.file_lobby", name_schem);
    }

    public String getType_mode_multiworld() {
        return type_mode_multiworld;
    }

    public String getMode() {
        return mode;
    }


    public boolean isSchemFile() {
        return schemFile;
    }

    public int getNum_rounds() {
        return num_rounds;
    }

    public void setNum_rounds(int num_rounds) {
        this.num_rounds = num_rounds;
    }

    public int getTime_rounds() {
        return time_rounds;
    }

    public int getTime_between_two_rounds() {
        return time_between_two_rounds;
    }


    private Settings(){

    }

    public static Settings getInstance(){
        return instance;
    }


}
