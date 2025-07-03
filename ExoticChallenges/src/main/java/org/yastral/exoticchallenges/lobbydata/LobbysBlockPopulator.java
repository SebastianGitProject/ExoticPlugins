package org.yastral.exoticchallenges.lobbydata;

import org.bukkit.*;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class LobbysBlockPopulator extends BlockPopulator {

    public void populate(World world, Random random, Chunk chunk) {
        WorldBorder b = Bukkit.getWorld(world.getName()).getWorldBorder();
        b.setCenter(Bukkit.getWorld(world.getName()).getSpawnLocation());
        b.setSize(500);
        b.setDamageAmount(2);
        if(Math.abs(chunk.getX()) > 3 || Math.abs(chunk.getZ()) > 3) {
            for (int x=0; x<16; x++) {
                for (int z=0;z<16;z++) {
                    for(int y= -64 ; y<= 255 ; y++) {
                        int realX = x + chunk.getX() * 16;
                        int realZ = z + chunk.getZ() * 16;
                        world.getBlockAt(realX, y, realZ).setType(Material.AIR);
                    }

                }
            }
        }
    }
    //String name = "%n".replace("%n", "ciao");
}
