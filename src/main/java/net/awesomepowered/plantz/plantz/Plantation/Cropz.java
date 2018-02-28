package net.awesomepowered.plantz.plantz.Plantation;

import com.sun.scenario.effect.Crop;
import net.awesomepowered.plantz.plantz.Plantz;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.Crops;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class Cropz {

    private BlockState crop;
    private int tpc;
    private int mode;
    private int taskId;
    private int maxData;

    public Cropz(BlockState crop, int mode, int tpc) {
        this.crop = crop;
        this.mode = mode;
        this.tpc = tpc;
        switch (crop.getData().getItemType()) { //Consistency pls mojang (づ｡◕_◕｡)づ
            case BEETROOT_BLOCK: case NETHER_WARTS:
                maxData = 3;
                break;
            default:
                maxData = 7;
        }
    }

    public BlockState getCrop() {
        return crop;
    }

    public void setCrop(BlockState crop) {
        this.crop = crop;
    }

    public int getTpc() {
        return tpc;
    }

    public void setTpc(int tpc) {
        this.tpc = tpc;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return this.mode;
    }

    public void refresh() {
        Bukkit.getScheduler().cancelTask(taskId);
        mulch();
    }

    public void witherAway() {
        Bukkit.getScheduler().cancelTask(taskId);
        Plantz.plantz.getCropzs().remove(this);
    }

    public void mulch() {
        crop.setRawData((byte) 0);
        crop.update();
        setTaskId(Bukkit.getScheduler().scheduleSyncRepeatingTask(Plantz.plantz, () -> {
            if (crop.getBlock().getType() == Material.AIR) {
                Plantz.plantz.getLogger().log(Level.WARNING, "Oh noes a plant died!");
                witherAway();
            }
            if (mode == 0) {
                if (crop.getRawData() == maxData) {
                    crop.setRawData((byte) 0);
                } else {
                    crop.setRawData((byte) (crop.getRawData()+1));
                }
            } else if (mode == 1) {
                if (crop.getRawData() == 0) {
                    crop.setRawData((byte) maxData);
                } else {
                    crop.setRawData((byte) (crop.getRawData()-1));
                }
            } else if (mode == 2) {
                crop.setRawData((byte) ThreadLocalRandom.current().nextInt(maxData));
            }
            crop.update();
        }, 0, tpc));
    }
}
