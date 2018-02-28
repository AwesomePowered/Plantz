package net.awesomepowered.plantz.plantz;

import net.awesomepowered.plantz.plantz.Plantation.Farmer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class CommandPlanter implements CommandExecutor {

    Plantz plantz;

    public CommandPlanter(Plantz plantz) {
        this.plantz = plantz;
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("plantation.owner") && sender instanceof Player) {
            Player p = (Player) sender;
            for(Iterator<Farmer> i = plantz.getFarmers().iterator(); i.hasNext();) {
                Farmer farmer = i.next();
                if (farmer.getFarmer().getUniqueId() == p.getUniqueId()) {
                    farmer.sendMessage("&aYou are no longer a farmer!");
                    i.remove();
                    return false;
                }
            }
            Farmer farmer = new Farmer(p);
            plantz.getFarmers().add(farmer);
            farmer.sendMessage("&aYou are now a farmer! Happy planting!");
        }
        return false;
    }
}
