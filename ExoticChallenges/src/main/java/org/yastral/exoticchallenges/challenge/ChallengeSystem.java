package org.yastral.exoticchallenges.challenge;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.yastral.exoticchallenges.ExoticChallenges;


import java.util.*;

public class ChallengeSystem {

    /*private final ExoticChallenges plugin;
    private final FileConfiguration config;
    private final Map<Integer, Challenge> challenges = new HashMap<>();

    public ChallengeSystem(ExoticChallenges plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        loadChallenges();
    }

    private void loadChallenges() {
        String generationType = config.getString("Generation_of_challenges");
        int numChallenges = config.getInt("Challenges.num_challenge");

        if ("random".equalsIgnoreCase(generationType)) {
            generateRandomChallenges(numChallenges);
        } else if ("manual".equalsIgnoreCase(generationType)) {
            loadManualChallenges(numChallenges);
        } else {
            Bukkit.getLogger().severe("Invalid Generation_of_challenges value in config!");
        }
    }

    private void generateRandomChallenges(int numChallenges) {
        Random random = new Random();
        for (int i = 1; i <= numChallenges; i++) {
            String difficulty = ChallengeUtils.getRandomDifficulty();
            String type = ChallengeUtils.getRandomType();
            String name = "Challenge " + i;
            int amount = random.nextInt(10) + 1;
            int height = type.equals("reaching_height") ? random.nextInt(256) : -1;
            String target = ChallengeUtils.getRandomTargetForType(type);

            // Genera condizioni casuali
            Map<String, Boolean> conditions = ChallengeUtils.generateRandomConditions();

            // Calcola il tempo base e aggiungi il tempo extra per le condizioni attive
            int time = 300 + ChallengeUtils.getExtraTimeForConditions(conditions);

            Challenge challenge = new Challenge(i, difficulty, type, name, amount, height, target, time, "Randomly generated challenge");
            challenge.addConditions(conditions); // Aggiungi le condizioni alla challenge

            challenges.put(i, challenge); //todo fare in modo che per ogni 2 condizioni attive, aumenta la difficoltà(di base è su easy)
        }
    }

    private void loadManualChallenges(int numChallenges) {
        for (int i = 1; i <= numChallenges; i++) {
            if (!config.contains("Challenges_task." + i)) {
                Bukkit.getLogger().severe("Missing challenge with ID " + i);
                return;
            }
            String difficulty = config.getString("Challenges_task." + i + ".difficulty");
            String type = config.getString("Challenges_task." + i + ".type");
            String name = config.getString("Challenges_task." + i + ".name_of_task");
            int amount = config.getInt("Challenges_task." + i + ".amount");
            int height = config.getInt("Challenges_task." + i + ".height");
            String target = config.getString("Challenges_task." + i + ".target");
            int time = config.getInt("Challenges_task." + i + ".time_to_complete_task");
            String description = config.getString("Challenges_task." + i + ".description");

            Challenge challenge = new Challenge(i, difficulty, type, name, amount, height, target, time, description);
            challenge.loadConditionsFromConfig(config, i);
            challenges.put(i, challenge);
        }
    }

    public Map<Integer, Challenge> getChallenges() {
        return challenges;
    }
}

class Challenge {
    private final int id;
    private final String difficulty;
    private final String type;
    private final String name;
    private final int amount;
    private final int height;
    private final String target;
    private final int time;
    private final String description;
    private final Map<String, Boolean> conditions = new HashMap<>();

    public Challenge(int id, String difficulty, String type, String name, int amount, int height, String target, int time, String description) {
        this.id = id;
        this.difficulty = difficulty;
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.height = height;
        this.target = target;
        this.time = time;
        this.description = description;
    }

    public void addConditions(Map<String, Boolean> conditions) {
        this.conditions.putAll(conditions);
    }

    public void loadConditionsFromConfig(FileConfiguration config, int id) {
        for (String key : config.getConfigurationSection("Challenges_task." + id + ".Other_condition_to_reach").getKeys(false)) {
            boolean value = config.getBoolean("Challenges_task." + id + ".Other_condition_to_reach." + key);
            conditions.put(key, value);
        }
    }

    // Getters
}

class ChallengeUtils {

    private static final Random random = new Random();

    private static final List<String> validPickupItems = Arrays.asList(
            "APPLE", "DIAMOND", "GOLD_INGOT", "IRON_INGOT", "EMERALD", "REDSTONE", "LAPIS_LAZULI", "COAL", "NETHERITE_INGOT"
            // Aggiungi tutti gli ID validi per "pickup"
    );
    private static final List<String> validDestroyBlocks = Arrays.asList(
            "STONE", "DIRT", "GRASS_BLOCK", "SAND", "GRAVEL", "COBBLESTONE", "NETHERRACK", "END_STONE"
            // Aggiungi tutti gli ID validi per "destroy"
    );
    private static final List<String> validBreedingMobs = Arrays.asList(
            "COW", "SHEEP", "PIG", "CHICKEN", "WOLF", "CAT", "HORSE"
            // Aggiungi tutti gli ID validi per "breeding"
    );
    private static final List<String> validReachingHeights = Collections.singletonList("256");
    private static final List<String> validKillMobs = Arrays.asList(
            "CREEPER", "ZOMBIE", "SKELETON", "SPIDER", "ENDERMAN", "BLAZE", "GHAST", "WITHER_SKELETON", "PIGLIN"
            // Aggiungi tutti gli ID validi per "kill_mob"
    );
    private static final List<String> validCraftItems = Arrays.asList(
            "GOLDEN_BLOCK", "IRON_BLOCK", "DIAMOND_BLOCK", "NETHERITE_BLOCK", "ANVIL", "ENCHANTING_TABLE"
            // Aggiungi tutti gli ID validi per "craft"
    );
    private static final List<String> validSummonMobs = Arrays.asList(
            "IRON_GOLEM", "SNOW_GOLEM", "WITHER", "ENDER_DRAGON"
            // Aggiungi tutti gli ID validi per "summon"
    );
    private static final List<String> validDimensions = Arrays.asList(
            "overworld", "nether", "end"
            // Aggiungi tutti gli ID validi per "dimension"
    );
    private static final List<String> validProjectiles = Arrays.asList(
            "SPECTRAL_ARROW", "SNOWBALL", "EGG", "ENDER_PEARL", "POTION"
            // Aggiungi tutti gli ID validi per "shoot_a_projectile"
    );
    private static final List<String> validPlaceBlocks = Arrays.asList(
            "NETHERITE_BLOCK", "DIAMOND_BLOCK", "IRON_BLOCK", "GOLD_BLOCK", "EMERALD_BLOCK"
            // Aggiungi tutti gli ID validi per "place_block"
    );
    private static final List<String> validBiomes = Arrays.asList(
            "SAVANNA", "DESERT", "FOREST", "TAIGA", "PLAINS", "SWAMP", "JUNGLE", "OCEAN", "NETHER", "END"
            // Aggiungi tutti gli ID validi per "reach_biome"
    );
    private static final List<String> validAdvancements = Arrays.asList(
            "SMELT_IRON", "FIND_DIAMOND", "ENTER_NETHER", "ENTER_END", "KILL_WITHER", "KILL_ENDER_DRAGON"
            // Aggiungi tutti gli ID validi per "get_advancements"
    );
    private static final List<String> validFoods = Arrays.asList(
            "STEAK", "PORKCHOP", "CHICKEN", "MUTTON", "RABBIT", "BEEF", "COD", "SALMON", "BREAD"
            // Aggiungi tutti gli ID validi per "eat_food"
    );
    private static final List<String> validDeathMessages = Arrays.asList(
            "DAMAGE_ANVIL", "DAMAGE_CACTUS", "DAMAGE_LAVA", "DAMAGE_DROWNING", "DAMAGE_FIRE"
            // Aggiungi tutti gli ID validi per "death_message"
    );

    public static String getRandomDifficulty() {
        String[] difficulties = {"easy", "normal", "hard", "extreme"};
        return difficulties[random.nextInt(difficulties.length)];
    }

    public static String getRandomType() {
        String[] types = {
                "pickup", "destroy", "breeding", "reaching_height", "kill_mob", "craft",
                "summon", "dimension", "shoot_a_projectile", "place_block", "reach_biome",
                "get_advancements", "eat_food", "death_message"
        };
        return types[random.nextInt(types.length)];
    }

    public static String getRandomTargetForType(String type) {
        switch (type) {
            case "pickup":
                return getRandomFromList(validPickupItems);
            case "destroy":
                return getRandomFromList(validDestroyBlocks);
            case "breeding":
                return getRandomFromList(validBreedingMobs);
            case "reaching_height":
                return getRandomFromList(validReachingHeights);
            case "kill_mob":
                return getRandomFromList(validKillMobs);
            case "craft":
                return getRandomFromList(validCraftItems);
            case "summon":
                return getRandomFromList(validSummonMobs);
            case "dimension":
                return getRandomFromList(validDimensions);
            case "shoot_a_projectile":
                return getRandomFromList(validProjectiles);
            case "place_block":
                return getRandomFromList(validPlaceBlocks);
            case "reach_biome":
                return getRandomFromList(validBiomes);
            case "get_advancements":
                return getRandomFromList(validAdvancements);
            case "eat_food":
                return getRandomFromList(validFoods);
            case "death_message":
                return getRandomFromList(validDeathMessages);
            default:
                return "UNKNOWN";
        }
    }

    public static int getExtraTimeForConditions(Map<String, Boolean> conditions) {
        int activeConditions = 0;
        for (boolean condition : conditions.values()) {
            if (condition) {
                activeConditions++;
            }
        }
        return activeConditions * 60; // Aggiungi 1 minuto (60 secondi) per ogni condizione attiva
    }

    public static Map<String, Boolean> generateRandomConditions() {
        Map<String, Boolean> conditions = new HashMap<>();
        // Genera condizioni casuali come esempio
        conditions.put("kill_mob", random.nextBoolean());
        conditions.put("craft_item", random.nextBoolean());
        conditions.put("place_block", random.nextBoolean());
        // Aggiungi altre condizioni se necessario
        return conditions;
    }

    private static String getRandomFromList(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }*/
}

