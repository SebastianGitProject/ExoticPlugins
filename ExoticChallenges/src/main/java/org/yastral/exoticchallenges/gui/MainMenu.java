package org.yastral.exoticchallenges.gui;
/*
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.collection.StrictMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuContainer;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.button.ButtonReturnBack;
import org.mineacademy.fo.menu.button.StartPosition;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuClickLocation;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.SimpleLocalization;
import org.yastral.exoticchallenges.listeners.QueueListener3;
import org.yastral.exoticchallenges.listeners.settings.Settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.stream.Collectors;

public class MainMenu extends Menu {

    @Position(start = StartPosition.CENTER, value = 26)
    private final Button openPreferencesButton;

    @Position(start = StartPosition.CENTER)
    private final Button selectMobEggsButton;

    @Position(start = StartPosition.CENTER, value = 28)
    private final Button getItemsButton;



    public MainMenu() {
        //Queue<Player> queue = QueueListener3.getPlayerQueue(player);
        setSlotNumbersVisible();
        setTitle("&6&lChallenge Menu");
        setSize(9 * 6);

        this.openPreferencesButton = new ButtonMenu(new PreferencesMenu(), CompMaterial.GOLD_BLOCK,
                "&6Top:",
                "",
                "Click to open the menu");

        this.selectMobEggsButton = new ButtonMenu(new SelectMobEggsMenu(), CompMaterial.SPAWNER,
                "&6Select Mob Egg",
                "",
                "Click to open the menu");

        this.getItemsButton = new ButtonMenu(new GetItemMenu(), CompMaterial.NETHER_STAR,
                "&6Info lobby:",
                "",
                "Rounds: ",
                "Current round: (0/0)",
                "Total of quests: " + Settings.getInstance().getNumChallenges());



        /*this.openPreferencesButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                new PreferencesMenu().displayTo(player);
            }*/



    /*}

    @Override
    public ItemStack getItemAt(int slot) {
        if(slot < 9 * 1){
            return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE,
                    " ").make();
        }

            if(slot > 26 && slot < 36){
                return ItemCreator.of(CompMaterial.ENCHANTED_BOOK,
                        "&a&lCHALLENGE #%n",
                        "",
                        "&6\u2BA9 &6&lInfo:",
                        "&6Name quest: &e%quest%",
                        "&6Detail of quest: &e%info%",
                        "",
                        "&6Times completed: &e%volte_completato_challenge%",
                        "",
                        "&2Click for more info").make();
            }


            if(slot > 8 && slot < 18){
                return ItemCreator.of(CompMaterial.ENCHANTED_BOOK,
                        "&a&lCHALLENGE #%n", //(slot - (slot - 1)),
                        "",
                        "&6\u2BA9 &6&lInfo:",
                        "&6Name quest: &e%quest%",
                        "&6Detail of quest: &e%info%",
                        "",
                        "&6Times completed: &e%volte_completato_challenge%",
                        "",
                        "&2Click for more info").make();
            }

        if(slot > 50 * 1 && slot <= 54 * 1){
            return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE,
                    " ").make();
        }
        if(slot > 44 * 1 && slot < 48 * 1){
            return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE,
                    " ").make();
        }
        if(slot == 49){
            return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE,
                    " ").make();
        }
        if(slot > 35 * 1 && slot < 39 * 1){
            return ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE,
                    "Seconds: da 9 a 7s").make(); //devono fare anche il suono
        }
        if(slot > 38 * 1 && slot < 42 * 1){
            return ItemCreator.of(CompMaterial.ORANGE_STAINED_GLASS_PANE,
                    "Seconds: da 6 a 4s").make();
        }
        if(slot > 41 * 1 && slot < 45 * 1){
            return ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE,
                    "Seconds: da 3 a 1s").make();
        }

        return NO_ITEM;
    }

    private class PreferencesMenu extends Menu {

        @Position(start = StartPosition.CENTER, value = -1)
        private final Button clearInventoryButton;

        @Position(start = StartPosition.CENTER, value = 1)
        private final Button refillHealthButton;


        PreferencesMenu() {
            super(MainMenu.this);

            setTitle("&6Preferences");
            setSize(9 * 3);

            this.clearInventoryButton = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                    player.getInventory().clear();
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.LAVA_BUCKET, "&6Clear Inventory").make();
                }


            };

            this.refillHealthButton = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                    player.setHealth(player.getMaxHealth());

                    restartMenu("&aRefilled health!");
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.GOLDEN_APPLE,
                            "&6Clear Inventory",
                            "",
                            "&fCurrent health: &c" + Math.round(getViewer().getHealth()),  //getViewer fa ritornare il player che visualizza l'inventory
                            "",
                            "&fClick to refill").make();
                }
            };
        }

        @Override
        protected void onPostDisplay(Player viewer){

            animate(20, new MenuRunnable() {

                boolean toggle = true;
                @Override
                public void run() {
                    setItem(getCenterSlot(), ItemCreator.of(toggle ? CompMaterial.RED_WOOL : CompMaterial.BLUE_WOOL).make());

                    toggle = !toggle;
                }
            });
        }


        @Override
        protected String[] getInfo() {
            return new String[] {
                    "Click bucket to clear your inventory",
                    "",
                    "Click apple to refill your health"
            };
        }

        /*@Override
        protected boolean addReturnButton() {
            return super.addReturnButton();
        }

        @Override
        protected int getInfoButtonPosition() {
            return super.getInfoButtonPosition();
        }

        @Override
        protected int getReturnButtonPosition() {
            return super.getReturnButtonPosition();
        }*/
    /*}

    private class SelectMobEggsMenu extends MenuPagged<EntityType> {

        SelectMobEggsMenu(){
            super(MainMenu.this, Arrays.asList(EntityType.values())
                    .stream()
                    .filter(type -> type.isSpawnable() && type.isAlive())  //in filter, ci può essere qualsiasi cosa come experience_orb
                    .collect(Collectors.toList())
            );

            setTitle("&6Egg menu");
            //setSize(9 * 4);
        }

        /*@Override
        public Button formNextButton() {
            return new Button() {
                final boolean canGo = getCurrentPage() < getPages().size();

                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
                    if (this.canGo)
                        setCurrentPage(MathUtil.range(getCurrentPage() + 1, 1, getPages().size()));
                }

                @Override
                public ItemStack getItem() {
                    final boolean lastPage = getCurrentPage() == getPages().size();

                    return ItemCreator
                            .of(this.canGo ? MenuPagged.getActivePageButton() : MenuPagged.getInactivePageButton())
                            .name(lastPage ? SimpleLocalization.Menu.PAGE_LAST : SimpleLocalization.Menu.PAGE_NEXT.replace("{page}", String.valueOf(getCurrentPage() + 1)))
                            .make();
                }
            };
        }

        @Override
        public Button formPreviousButton() {
            return new Button() {
                final boolean canGo = getCurrentPage() > 1;

                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
                    if (this.canGo)
                        setCurrentPage(MathUtil.range(getCurrentPage() - 1, 1, getPages().size()));
                }

                @Override
                public ItemStack getItem() {
                    final int previousPage = getCurrentPage() - 1;

                    return ItemCreator
                            .of(this.canGo ? MenuPagged.getActivePageButton() : MenuPagged.getInactivePageButton())
                            .name(previousPage == 0 ? SimpleLocalization.Menu.PAGE_FIRST : SimpleLocalization.Menu.PAGE_PREVIOUS.replace("{page}", String.valueOf(previousPage)))
                            .make();
                }
            };
        }

        @Override
        protected boolean addReturnButton() {
            return super.addReturnButton();
        }*/

        /*@Override
        protected ItemStack convertToItemStack(EntityType item) { //converte le uova in itemstack
            return ItemCreator.ofEgg(item,
                    "&6&l" + ItemUtil.bountifyCapitalized(item.name()),
                    "",
                    "&fClick to obtain the egg").make();
        }

        @Override
        protected void onPageClick(Player player, EntityType entityType, ClickType clickType) {
            ItemCreator.ofEgg(entityType).give(player);
        }
    }

    private class GetItemMenu extends MenuContainer {

        GetItemMenu(){
            super(MainMenu.this);

            setTitle("&6Get items");
            setSize(9 * 2);
        }
        @Override
        protected ItemStack getDropAt(int slot) {  //mette le pozioni quando si apre il menu in automatico
            if (slot < 9 * 1){
                return ItemCreator.ofPotion(PotionEffectType.INVISIBILITY).make();
            }

             return NO_ITEM;
        }

        @Override
        protected void onMenuClose(StrictMap<Integer, ItemStack> items) {
            //todo custom logic > save items for config
        }

        @Override
        protected boolean isActionAllowed(MenuClickLocation location, int slot, @Nullable ItemStack clicked, @Nullable ItemStack cursor) {
            return slot <= 9 * 1;  //il player può modificare solo la prima riga e può essere applicato a tutti i menu
        }

        /*@Override
        protected ItemStack onItemClick(int slot, ClickType clickType, @Nullable ItemStack item) {
            return super.onItemClick(slot, clickType, item);
        }*/
    /*}
}

    /*
    @Override
    public ItemStack getItemAt(int slot){

        if(slot == 11){
            return openPreferencesButton.getItem();
        }
        return NO_ITEM;
    }*/
    /*
    @Override
    protected void onMenuClick(MenuClickLocation location, int slot, MenuClickType click, int hotbarButton){

    }

    @Override
    protected void onMenuClose(){

    }*/

