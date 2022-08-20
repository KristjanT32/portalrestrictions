package krisapps.portalrestrictions.portalrestrictions.datefollowing;

import krisapps.portalrestrictions.portalrestrictions.PortalRestrictions;
import krisapps.portalrestrictions.portalrestrictions.language.LocalizationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.util.Date;

public class RestrictionValidator implements Runnable {

    Date current_date;

    PortalRestrictions main;

    public RestrictionValidator(PortalRestrictions main) {
        this.main = main;
    }

    @Override
    public void run() {

        if (main.config.getBoolean("config.logValidator")) {
            main.getLogger().info("[ValidityChecker] Checking restrictions...");
        }
        current_date = new Date();

        if (main.restrictiondata.getConfigurationSection("restrictions") == null || main.restrictiondata.getConfigurationSection("restrictions").getKeys(false).size() == 0) {
            return;
        }

        for (String restriction : main.restrictiondata.getConfigurationSection("restrictions").getKeys(false)) {

            Date date = main.restrictiondata.getObject("restrictions." + restriction + ".issuedOn", Date.class);
            int duration = main.restrictiondata.getInt("restrictions." + restriction + ".duration");

            if (current_date.after(date)){
                if (current_date.getDay() - date.getDay() >= duration){
                    if (main.config.getBoolean("config.logValidator")) {
                        main.getLogger().info("Restriction " + restriction + " is no longer valid, removing...");
                    }
                    if (main.config.getBoolean("config.announceReset")){
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.RESTRICTION_REMOVED_SCHEDULE.getString()).replace("%target%", restriction).replace("%type%", main.restrictiondata.getString("restrictions." + restriction + ".type")));
                    }
                    main.restrictiondata.set("restrictions." + restriction, null);

                    try {
                        main.restrictiondata.save(main.dataFile);
                    } catch (IOException e) {
                        main.getLogger().info("Error removing restriction [" + restriction + "]");
                        e.printStackTrace();
                    }
                }
            }else{
                continue;
            }

        }
        if (main.config.getBoolean("config.logValidator")) {
            main.getLogger().info("[ValidityChecker] Checking complete.");
        }
    }
}
