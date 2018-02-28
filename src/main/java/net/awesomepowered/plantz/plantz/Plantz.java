package net.awesomepowered.plantz.plantz;

import net.awesomepowered.plantz.plantz.ListenerTrucks.CombineHarvester;
import net.awesomepowered.plantz.plantz.Plantation.Cropz;
import net.awesomepowered.plantz.plantz.Plantation.Farmer;
import net.awesomepowered.plantz.plantz.Shapez.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class Plantz extends JavaPlugin {

    public static Plantz plantz;
    private List<Farmer> farmers = new ArrayList<>();
    //private List<Cropz> cropzs = new ArrayList<>();
    private Map<Cuboid, List<Cropz>> cropzs = new HashMap<>();

    @Override
    public void onEnable() {
        plantz = this;
        Bukkit.getPluginManager().registerEvents(new CombineHarvester(this), this);
        getCommand("plantz").setExecutor(new CommandPlanter(this));
        new BukkitRunnable() {
            @Override
            public void run() {
                plantPlantz();
            }
        }.runTaskLater(this, 100); //Make sure all worlds are loaded.
    }

    public void onDisable() {
        harvestPlantz();
    }

    public List<Farmer> getFarmers() {
        return farmers;
    }

    public Map<Cuboid, List<Cropz>> getCropzs() {
        return cropzs;
    }

    public void plantPlantz() {
        if (getConfig().getConfigurationSection("crops") == null) {
            return;
        }
        getLogger().log(Level.INFO, "Planting crops");
        for (String s : getConfig().getConfigurationSection("crops").getKeys(false)) {
            String[] cuboidz = s.split(":");
            Cuboid cuboid = new Cuboid(stringToLoc(cuboidz[0]), stringToLoc(cuboidz[1]));
            getCropzs().put(cuboid, new ArrayList<>());
            for (Block block : cuboid.getBlocks()) {
                if (block.getState().getData() instanceof Crops || block.getState().getData() instanceof NetherWarts) {
                    Cropz cropz = new Cropz(block.getState(), getConfig().getInt("crops."+s+"."+locToString(block.getLocation())+".mode", 0), 5);
                    cropz.mulch();
                    getCropzs().get(cuboid).add(cropz);
                }
            }
        }
    }

    public void harvestPlantz() {
        getConfig().set("crops", null);
        for (Cuboid cube : getCropzs().keySet()) {
            String cuboidz = locToString(cube.getUpperLocation())+":"+locToString(cube.getLowerLocation());
            for (Cropz cropz : getCropzs().get(cube)) {
                getConfig().set("crops."+cuboidz+"."+locToString(cropz.getCrop().getLocation())+".mode", cropz.getMode());
            }
        }
        saveConfig();
    }

    public String locToString(Location loc) {
        String world = loc.getWorld().getName();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        return world+"~"+x+"~"+y+"~"+z;
    }

    public Location stringToLoc(String s) {
        String[] loc = s.split("~");
        return new Location(Bukkit.getWorld(loc[0]),Double.valueOf(loc[1]),Double.valueOf(loc[2]),Double.valueOf(loc[3]));
    }

}
