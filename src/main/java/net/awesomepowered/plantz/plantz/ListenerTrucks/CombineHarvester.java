package net.awesomepowered.plantz.plantz.ListenerTrucks;

import net.awesomepowered.plantz.plantz.Plantation.Cropz;
import net.awesomepowered.plantz.plantz.Plantation.Farmer;
import net.awesomepowered.plantz.plantz.Plantz;
import net.awesomepowered.plantz.plantz.Shapez.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.NetherWartsState;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;

import java.util.ArrayList;
import java.util.Iterator;

public class CombineHarvester implements Listener {

    private Plantz plantz;

    public CombineHarvester(Plantz plantz) {
        this.plantz = plantz;
    }

    @EventHandler
    public void onPlant(PlayerInteractEvent ev) {
        Player p = ev.getPlayer();

        for (Farmer farmer : plantz.getFarmers()) {
            if (farmer.getFarmer().getUniqueId() == p.getUniqueId()) {
                ev.setCancelled(true);
                plantSeed(ev, farmer);
                return;
            }
        }

    }

    public void plantSeed(PlayerInteractEvent ev, Farmer farmer) {
        if (ev.getAction() == Action.LEFT_CLICK_BLOCK) {
            farmer.setSelection1(ev.getClickedBlock().getLocation());
        }

        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            farmer.setSelection2(ev.getClickedBlock().getLocation());
        }
    }

    @EventHandler
    public void onWater(AsyncPlayerChatEvent ev) {
        Player p = ev.getPlayer();

        for (Farmer farmer : plantz.getFarmers()) {
            if (farmer.getFarmer().getUniqueId() == p.getUniqueId()) {
                ev.setCancelled(true);
                Bukkit.getScheduler().runTask(Plantz.plantz, () -> waterSoil(ev, farmer));
                return;
            }
        }
    }

    public void waterSoil(AsyncPlayerChatEvent ev, Farmer farmer) {
        if (ev.getMessage().equalsIgnoreCase("mode")) {
            Cuboid cuboid = farmer.getCuboid();
            int i = 0;
            for (Cuboid cube : plantz.getCropzs().keySet()) {
                if (cube.inCuboid(cuboid.getLowerLocation())) {
                    for (Cropz cropz : plantz.getCropzs().get(cube)) {
                        switch (cropz.getMode()) {
                            case 0:
                                cropz.setMode(1);
                                i++;
                                break;
                            case 1:
                                cropz.setMode(2);
                                i++;
                                break;
                            case 2:
                                cropz.setMode(0);
                                i++;
                        }
                    }
                }
            }
            farmer.sendMessage(String.format("&aYou changed &d%s &aplant mode.", i));
        }

        if (ev.getMessage().equalsIgnoreCase("fertilize")) {
            int i = 0;

            for (Block block : farmer.getCuboid().getBlocks()) {
                for (Cuboid cube : plantz.getCropzs().keySet()) {
                    if (cube.getBlocks().contains(block)) {
                        farmer.sendMessage("&cThere are planted crops on your field!");
                        return;
                    }
                }
            }

            ArrayList<Cropz> list = new ArrayList<>();
            for (Block block : farmer.getCuboid().getBlocks()) {
                if (block.getState().getData() instanceof Crops || block.getState().getData() instanceof NetherWarts) {
                    Cropz cropz = new  Cropz(block.getState(), 0, 5);
                    cropz.mulch();
                    list.add(cropz);
                    i++;
                }
            }
            if (i >= 1) {
                plantz.getCropzs().put(farmer.getCuboid(), list);
            }
            farmer.sendMessage(String.format("&aYou made &d%s &aplants very happy.", i));
        }

    }
}
