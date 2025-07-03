package org.yastral.exoticchallenges.lobbydata.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.yastral.exoticchallenges.ExoticChallenges;
//import org.yastral.exoticchallenges.gui.MainMenu;
import org.yastral.exoticchallenges.listeners.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Solo i player possono eseguire il comando");

            return true;
        }
        Player player = (Player) sender;


        //new MainMenu().displayTo(player);
        /*Inventory inventory = Bukkit.createInventory(player, 9 * 6, ChatColor.GOLD + "" + ChatColor.BOLD + "Challenge Menu");



        ItemStack yellowWool = new ItemStack(Material.YELLOW_WOOL);
        ItemMeta ywoolMeta = yellowWool.getItemMeta();
        ywoolMeta.setDisplayName(ChatColor.GREEN + "Reload");
        yellowWool.setItemMeta(ywoolMeta);




        ItemStack redWool = new ItemStack(Material.RED_WOOL);
        ItemMeta rwoolMeta = redWool.getItemMeta();
        rwoolMeta.setDisplayName(ChatColor.RED + "Close");gold
        redWool.setItemMeta(rwoolMeta);


        ItemStack goldBlock = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta goldblockMeta = goldBlock.getItemMeta();
        goldblockMeta.setDisplayName(ChatColor.GOLD + "Top List:");
        goldblockMeta.setEnchantmentGlintOverride(true);
        World world = player.getWorld();
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ChatColor.GOLD + "Player in partita: " + ChatColor.YELLOW + world.getPlayers().size());
        lore.add(ChatColor.GOLD + " ");

        int x = 0;
        // Aggiungi i nomi dei giocatori alla lore
        for (Player p : world.getPlayers()) {
            x++;
            lore.add(ChatColor.GOLD + "#" + x + " " + p.getName() + ": " + ChatColor.YELLOW + p.getStatistic(Statistic.PLAYER_KILLS));
        }

        goldblockMeta.setLore(lore);
        goldBlock.setItemMeta(goldblockMeta);


        List<ItemStack> items = new ArrayList<>();


        for (int i = 0; i < Settings.getInstance().getNumChallenges(); i++) {
            ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "Challenge: " + ChatColor.YELLOW + "#" + (i + 1));
        meta.setLore(Arrays.asList(" ", ChatColor.GOLD + "\u2BA9 Info:", ChatColor.YELLOW + "%info%",ChatColor.GRAY + " ", ChatColor.GOLD + "\u2BA9 Stats " + player.getName() + ChatColor.YELLOW + ": %volte_completato_challenge%"));
            item.setItemMeta(meta);
            items.add(item);
        }


        for (int i = 0; i < Settings.getInstance().getNumChallenges(); i++) {
            if(Settings.getInstance().getNumChallenges() <= 5){
                ItemStack item = items.get(i);
                int assignedNumber = 18 + i * 2;  // 9, 11, 13, ...
                inventory.setItem(assignedNumber, item);

            }else if(Settings.getInstance().getNumChallenges() <= 10){
                ItemStack item = items.get(i);
                int assignedNumber = 9 + i * 2;  // 9, 11, 13, ...

                if(assignedNumber >= 18){
                    assignedNumber--;
                    inventory.setItem(assignedNumber, item);
                }else{
                    inventory.setItem(assignedNumber, item);
                }




            }

            //sender.sendMessage("L'ItemStack '" + item.getItemMeta().getDisplayName() + "' ha il numero " + assignedNumber);
        }


        inventory.setItem(0, yellowWool);
        inventory.setItem(8, redWool);
        inventory.setItem(49, goldBlock);

        player.openInventory(inventory);  //player.openBook
        player.setMetadata("OpenedMenu", new FixedMetadataValue(ExoticChallenges.getInstance(), "Challenge Menu"));*/

        return true;
    }





}
