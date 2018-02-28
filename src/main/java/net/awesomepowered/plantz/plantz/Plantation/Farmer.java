package net.awesomepowered.plantz.plantz.Plantation;

import net.awesomepowered.plantz.plantz.Shapez.Cuboid;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Farmer {

    private Player farmer;
    private Location selection1;
    private Location selection2;

    public Farmer(Player p) {
        this.farmer = p;
    }

    public Location getSelection1() {
        return selection1;
    }

    public void setSelection1(Location selection1) {
        this.selection1 = selection1;
        sendMessage(String.format("&5First position set to (&d%s&5, &d%s&5, &d%s&5)", selection1.getBlockX(), selection1.getBlockY(), selection1.getBlockZ()));
    }

    public Location getSelection2() {
        return selection2;
    }

    public void setSelection2(Location selection2) {
        if (getSelection2() != null && getSelection2().equals(selection2)) {
            return;
        }
        this.selection2 = selection2;
        sendMessage(String.format("&5Second position set to (&d%s&5, &d%s&5, &d%s&5)", selection2.getBlockX(), selection2.getBlockY(), selection2.getBlockZ()));
    }

    public Cuboid getCuboid() {
        if (selection1 != null && selection2 != null) {
            return new Cuboid(selection1, selection2);
        }
        return null;
    }

    public Player getFarmer() {
        return farmer;
    }

    public void sendMessage(Object message) {
        farmer.sendMessage(ChatColor.translateAlternateColorCodes('&', message.toString()));
    }
}
