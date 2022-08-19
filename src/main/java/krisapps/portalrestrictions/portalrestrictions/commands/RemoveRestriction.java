package krisapps.portalrestrictions.portalrestrictions.commands;

import krisapps.portalrestrictions.portalrestrictions.PortalRestrictions;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionContext;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionTarget;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionType;
import krisapps.portalrestrictions.portalrestrictions.language.LocalizationManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class RemoveRestriction implements CommandExecutor {

    PortalRestrictions main;
    public RemoveRestriction(PortalRestrictions main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Syntax: /removerestriction <restrictionTarget>
        if (args.length == 1){
            String target = args[0];

            removeRestriction(sender, RestrictionTarget.valueOf(target));

        }else{
            sender.sendMessage(ChatColor.RED + "Insufficient arguments.");
            return false;
        }


        return true;
    }

    private void removeRestriction(CommandSender s, RestrictionTarget target){

        try {
            main.restrictiondata.load(main.dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (main.restrictiondata.contains("restrictions." + target)) {
            main.restrictiondata.set("restrictions." + target, null);

            try {
                main.restrictiondata.save(main.dataFile);
                s.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.RESTRICTION_REMOVE_SUCCESS.getString().replace("%target%", target.toString())));
            }catch (IOException e){
                s.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.SAVE_ERROR.getString()));
                e.printStackTrace();
            }
        }else{
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.RESTRICTION_NOT_SET.getString().replace("%type%", target.toString())));
        }

    }
}
