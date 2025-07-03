package org.yastral.exoticchallenges.gui;

import de.themoep.inventorygui.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.yastral.exoticchallenges.ExoticChallenges;
import org.yastral.exoticchallenges.listeners.QueueListener3;
import org.yastral.exoticchallenges.listeners.RoundSystem;
import org.yastral.exoticchallenges.listeners.settings.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ChallengesGui{

    private static String guiTitle = "Challenges";

    public static Boolean open = false;


    //public final Map<String, Queue<Player>> playerAlive;


    /*public ChallengesGui(Map<String, Queue<Player>> playerAlive) {
        this.playerAlive = playerAlive;
    }*/

    //private final Queue<Player> playerQueue;

    public static void gui(Player player){
        //RoundSystem roundSystem = roundSystems.get(player.getWorld().getName());
        //QueueListener3 alive = (QueueListener3) playerAlive.get(player.getWorld().getName());
        /*open = true;
        String[] guiSetup2 = {
                "  s i z  ",
                "  ggggg  ",
                "  fpdnl  ",
                "         ",
                "asdfghjkl",
                "   t n   ",
        };


        String[] guiSetup = {
                "puuuuuuuy",
                "         ",
                "         ",
                "         ",
                "    h    ",
                "uuutunuuu",
        };

        StringBuilder secondRow = new StringBuilder(guiSetup[1]);
        StringBuilder fourthRow = new StringBuilder(guiSetup[3]);

        for (int i = 1; i <= Settings.getInstance().getNumChallenges(); i++) {
            if (i <= 5) {
                // Aggiungi "i" alla seconda riga
                int position = 4 - (i - 1); // Posizione centrale basata su i
                secondRow.setCharAt(position * 2, 'i');
            } else {
                // Aggiungi "i" alla quarta riga
                int position = 4 - ((i - 6) * 2); // Posizione centrale basata su i
                fourthRow.setCharAt(position, 'i');
            }
        }

        // Aggiorna le righe nel setup originale
        guiSetup[1] = secondRow.toString();
        guiSetup[3] = fourthRow.toString();
        InventoryGui gui = new InventoryGui(ExoticChallenges.getPlugin(), ChatColor.GOLD + guiTitle, guiSetup);
        //gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS, 1)); // fill the empty slots with this


        //static element
        //for (Player queuedPlayer : playerQueues) {
            gui.addElement(new StaticGuiElement('t',
                    new ItemStack(Material.GOLD_BLOCK),
                    ChatColor.GOLD + "\u2BA9 Top:",
                    //queuedPlayer.getName() + "\n",
                    "",
                    ""
            ));
        //}


        gui.addElement(new StaticGuiElement('n',

                new ItemStack(Material.NETHER_STAR),
                " ",
                ChatColor.AQUA + "\u2BA9 Info lobby:",
                ChatColor.YELLOW + "Total of players: " + Settings.getInstance().getNum_player() ,
                ChatColor.YELLOW + "Players alive: " + playerAlive.size(),
                ChatColor.YELLOW + "Time: " + new SimpleDateFormat("HH:mm:ss").format(new Date()),
                " ",
                ChatColor.AQUA + "\u2BA9 Info Rounds: ",
                ChatColor.YELLOW + "Current round: ("  + roundSystem.getCurrentRound() +  "/" + Settings.getInstance().getNum_rounds() + ")",
                ChatColor.YELLOW + "Total of challenges: " + Settings.getInstance().getNumChallenges(),
                ChatColor.YELLOW + "Challenges completed: %num%",
                " "
        ));

        gui.addElement(new StaticGuiElement('p',
                new ItemStack(Material.RED_STAINED_GLASS_PANE),
                ChatColor.AQUA + "Info of previous challenge:",
                "",
                ChatColor.YELLOW + "---",
                ChatColor.YELLOW + "---",
                ChatColor.YELLOW + "---"
        ));

        gui.addElement(new StaticGuiElement('y',
                new ItemStack(Material.LIME_STAINED_GLASS_PANE),
                ChatColor.AQUA + "Next challenge: %name%"
        ));

        //faccio un ciclo for per tutte le challenge, e se una challenge ha nel nome una particolare frase, sarà un enchanted_book
        gui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.BOOK),
                click -> {
                    open = false;
                    gui.close();
                    ChallengesInfoGui.guiInfo(player);
                    return true; // returning false will not cancel the initial click event to the gui
                },
                ChatColor.AQUA + "#%n% Quest name: %name%",
                " ",
                " ",
                ChatColor.YELLOW + "Click to see the personal statistics",
                ChatColor.YELLOW + "for this challenge."
        ));

        GuiElementGroup group = new GuiElementGroup('i');
        String[] texts = {"ciao", "Hello", "Bibbo" , "Suca", "ALEEEEE"};  //aggiungere nell'array i nomi delle quest e pian piano inserirli nelle lettere i
        for (String text : texts) {
            // Add an element to the group
            // Elements are in the order they got added to the group and don't need to have the same type.
            group.addElement((new StaticGuiElement('e', new ItemStack(Material.DIRT), text)));
        }
        gui.addElement(group);




        gui.addElement(new StaticGuiElement('u',
                new ItemStack(Material.WHITE_STAINED_GLASS_PANE),
                " "
        ));


        /*
        //state element
        GuiStateElement element = new GuiStateElement('z',
                new GuiStateElement.State(
                        change -> {
                            change.getWhoClicked().setGameMode(GameMode.CREATIVE);
                            change.getWhoClicked().sendMessage("You are now in creative!");
                        },
                        "creativeEnabled", // a key to identify this state by
                        new ItemStack(Material.RED_WOOL, 1, (short) 5), // the item to display as an icon
                        ChatColor.GREEN + "Enable creative!", // explanation text what this element does
                        "By clicking here you will start in creative"
                ),
                new GuiStateElement.State(
                        change -> {
                            change.getWhoClicked().setGameMode(GameMode.SURVIVAL);
                            change.getWhoClicked().sendMessage("You are no longer in creative!");
                        },
                        "creativeDisabled",
                        new ItemStack(Material.YELLOW_WOOL, 1, (short) 14),
                        ChatColor.RED + "Disable creative!",
                        "By clicking here you will stop to be in creative"
                )
        );

        if (player.isFlying()) {
            element.setState("creativeEnabled");
        } else {
            element.setState("creativeDisabled");
        }

        gui.addElement(element);
*/


        //dynamic element
        /*gui.addElement(new DynamicGuiElement('h', (viewer) -> {
            return new StaticGuiElement('h', new ItemStack (Material.EMERALD_BLOCK),
                    click -> {
                        click.getGui().draw(); // Update the GUI
                        return true;
                    },
                    ChatColor.GREEN + "Reload page");
        }));



        //element group(like pages)
        /*GuiElementGroup group = new GuiElementGroup('g');
        String[] texts = {"ciao", "Hello", "Bibbo" , "Suca", "ALEEEEE"};
        for (String text : texts) {
            // Add an element to the group
            // Elements are in the order they got added to the group and don't need to have the same type.
            group.addElement((new StaticGuiElement('e', new ItemStack(Material.DIRT), text)));
        }
        gui.addElement(group);

        // First page
        gui.addElement(new GuiPageElement('f', new ItemStack(Material.DIAMOND_BLOCK), GuiPageElement.PageAction.FIRST, "Go to first page (current: %page%)"));

        // Previous page
        gui.addElement(new GuiPageElement('j', new ItemStack(Material.DIAMOND), GuiPageElement.PageAction.PREVIOUS, "Go to previous page (%prevpage%)"));

        // Next page
        gui.addElement(new GuiPageElement('r', new ItemStack(Material.GOLD_INGOT), GuiPageElement.PageAction.NEXT, "Go to next page (%nextpage%)"));

        // Last page
        gui.addElement(new GuiPageElement('l', new ItemStack(Material.GOLD_BLOCK), GuiPageElement.PageAction.LAST, "Go to last page (%pages%)"));
        //InventoryHolder holder = null;
        //InventoryGui boooh = InventoryGui.get(holder);*/


        //gui.show(player);

    }







}
