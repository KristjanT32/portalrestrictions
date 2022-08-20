package krisapps.portalrestrictions.portalrestrictions.datefollowing;

import krisapps.portalrestrictions.portalrestrictions.PortalRestrictions;
import krisapps.portalrestrictions.portalrestrictions.language.LocalizationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.util.Date;

public class ValidateRestrictions implements CommandExecutor {

    Date current_date;
    PortalRestrictions main;
    public ValidateRestrictions(PortalRestrictions main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Syntax: /validaterestrictions

        validate(sender);

        return true;
    }


    void validate(CommandSender sender){
        current_date = new Date();

        if (main.restrictiondata.getConfigurationSection("restrictions") == null || main.restrictiondata.getConfigurationSection("restrictions").getKeys(false).size() == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.VALIDATING_NO_VALID_RESTRICTIONS.getString()));
            return;
        }else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.VALIDATING_START.getString().replace("%num%", String.valueOf(main.restrictiondata.getConfigurationSection("restrictions").getKeys(false).size()))));
        }

        for (String restriction : main.restrictiondata.getConfigurationSection("restrictions").getKeys(false)) {

            Date date = main.restrictiondata.getObject("restrictions." + restriction + ".issuedOn", Date.class);
            int duration = main.restrictiondata.getInt("restrictions." + restriction + ".duration");

            if (current_date.after(date)) {
                if (current_date.getDay() - date.getDay() >= duration) {
                    if (main.config.getBoolean("config.announceReset")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.VALIDATING_INVALID_RESTRICTION.getString()).replace("%target%", restriction).replace("%type%", main.restrictiondata.getString("restrictions." + restriction + ".type")));
                    }
                    main.restrictiondata.set("restrictions." + restriction, null);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('%', LocalizationManager.VALIDATING_RESTRICTION_REMOVED.getString()));

                    try {
                        main.restrictiondata.save(main.dataFile);
                    } catch (IOException e) {
                        main.getLogger().info("Error removing restriction.");
                        e.printStackTrace();
                    }
                }
            } else {
                continue;
            }

        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.VALIDATING_DONE.getString()));

    }
}
