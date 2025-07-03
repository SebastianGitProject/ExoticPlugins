package org.yastral.exoticchallenges.board;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.yastral.exoticchallenges.ExoticChallenges;

public class Board /*implements Runnable*/{

    /*private final static Board instance = new Board();

    private Board(){
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){


            if(player.getScoreboard() != null && player.getScoreboard().getObjective(ExoticChallenges.getInstance().getName()) != null && !player.getWorld().getName().startsWith("lobby")){
                updateScoreboard(player);
            }else{
                createNewScoreboard(player);
            }
            if(player.getWorld().getName().startsWith("lobby")){

            }
        }
    }

    private void createNewScoreboard(Player player){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(ExoticChallenges.getInstance().getName(), "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Challenge Hub");

        objective.getScore(ChatColor.GRAY + "" + ChatColor.WHITE).setScore(14);
        objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "\u2BA9 Info:").setScore(13);
        objective.getScore(ChatColor.GOLD + "Nome: " + ChatColor.YELLOW + player.getName()).setScore(12);
        objective.getScore(ChatColor.GOLD + "Lobby: " + ChatColor.YELLOW + "hub").setScore(11);



        objective.getScore(ChatColor.GRAY + " " + ChatColor.WHITE).setScore(9);
        objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "\u2BA9 Challenge:").setScore(8);
        objective.getScore(ChatColor.GOLD + "Challenge rimanenti: " + ChatColor.YELLOW + "\u2611").setScore(7);


        objective.getScore(ChatColor.GRAY + "  " + ChatColor.WHITE).setScore(6);
        objective.getScore(ChatColor.GOLD + "mc.name.it").setScore(5);

        /*Team team = scoreboard.registerNewTeam("team1");
        String teamkey = ChatColor.GOLD.toString();
        team.addEntry(teamkey);
        team.setPrefix(ChatColor.GOLD + "Lobby: ");
        team.setSuffix(ChatColor.YELLOW + "0");*/


        /*Team team2 = scoreboard.registerNewTeam("team2");
        String teamkey2 = ChatColor.GOLD.toString();
        team2.addEntry(teamkey2);
        team2.setPrefix(ChatColor.GOLD + "Player Kills: ");
        team2.setSuffix(ChatColor.YELLOW + "0");

        //objective.getScore(teamkey).setScore(12);
        objective.getScore(teamkey2).setScore(10);




        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player){
        Scoreboard scoreboard = player.getScoreboard();
        //Team team1 = scoreboard.getTeam("team1");
        //Team team = scoreboard.getTeam("team1");
        Team team2 = scoreboard.getTeam("team2");

        //team1.setSuffix(ChatColor.YELLOW + "" + (player.getStatistic(Statistic.WALK_ONE_CM)/* + (player.getStatistic(Statistic.SPRINT_ONE_CM))*/ //+ "cm"));
        /*team2.setSuffix(ChatColor.YELLOW + "" + (player.getStatistic(Statistic.PLAYER_KILLS)));
        //team.setSuffix(ChatColor.YELLOW + " " + player.getWorld().getName());

    }


    public static Board getInstance(){
        return instance;
    }*/
}
