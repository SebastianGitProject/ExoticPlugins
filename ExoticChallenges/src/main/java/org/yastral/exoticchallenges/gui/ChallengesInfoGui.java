package org.yastral.exoticchallenges.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.listeners.QueueListener3;
import org.yastral.exoticchallenges.listeners.RoundSystem;
import org.yastral.exoticchallenges.listeners.settings.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ChallengesInfoGui {
    private static String guiTitle = "Challenges Info";

    public static Boolean open = false;

    public static void guiInfo(Player player){
        open = true;

        String[] guiSetup = {
                "uuuuuuuuu",
                " k  i  k ",
                "         ",
                " k     k ",
                "    r    ",
                " k     k ",
        };

        InventoryGui gui = new InventoryGui(ExoticChallenges.getPlugin(), ChatColor.GOLD + guiTitle, guiSetup);
        //gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS, 1)); // fill the empty slots with this


        gui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.EMERALD),
                click -> {
                    open = false;
                    gui.close();
                    QueueListener3.gui(player, player.getWorld().getName());
                    return true;
                },
                ChatColor.AQUA + "Return for main page"
        ));


        gui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.ENCHANTED_BOOK),
                ChatColor.AQUA + "#%n% Quest name: %name%",
                " ",
                ChatColor.AQUA + "Lobby: [" + ChatColor.GRAY + player.getWorld().getName() + ChatColor.AQUA + "]",
                ChatColor.GRAY + "Date: " + new SimpleDateFormat("HH:mm:ss").format(new Date())
        ));




        gui.addElement(new StaticGuiElement('u',
                new ItemStack(Material.WHITE_STAINED_GLASS_PANE),
                " "
        ));

        gui.addElement(new StaticGuiElement('k',
                new ItemStack(Material.PAPER),
                ChatColor.AQUA + "\u2BA9 Statistic:",
                "",
                ChatColor.AQUA + "---"
        ));


        gui.show(player);

    }
}
