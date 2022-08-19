package krisapps.portalrestrictions.portalrestrictions.commands;

import krisapps.portalrestrictions.portalrestrictions.PortalRestrictions;
import krisapps.portalrestrictions.portalrestrictions.language.LocalizationManager;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionContext;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionTarget;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ViewRestriction implements CommandExecutor {

    PortalRestrictions main;
    public ViewRestriction(PortalRestrictions main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Syntax: /viewrestriction <restrictionTarget>

        if (args.length == 1){
            try {
                view(RestrictionTarget.valueOf(args[0]), sender);
            }catch (EnumConstantNotPresentException e){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.UNSUPPORTED_RESTRICTION_TARGET.getString().replace("%target%", e.constantName())));
            }
        }else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.INVALID_SYNTAX.getString()));
            return false;
        }

        return true;
    }


    String type;
    String date;
    String context;
    String duration;
    ArrayList<String> players;

    void view(RestrictionTarget restrictionTarget, CommandSender sender){

        try {
            main.restrictiondata.load(main.dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (main.restrictiondata.getConfigurationSection("restrictions." + restrictionTarget.toString()) != null){

            type = main.restrictiondata.getString("restrictions." + restrictionTarget + ".type");
            context = main.restrictiondata.getString("restrictions." + restrictionTarget + ".context");
            date = main.restrictiondata.get("restrictions." + restrictionTarget + ".issuedOn", Date.class).toString();
            duration = main.restrictiondata.getString("restrictions." + restrictionTarget + ".duration");

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.SHOW_RESTRICTION_HEADER.getString().replace("%type%", restrictionTarget.toString())));

            if (context.equals("RESTRICT_PLAYERS")){
                players = (ArrayList<String>) main.restrictiondata.getList("restrictions." + restrictionTarget + ".restrictedFor");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.SHOW_SPECIALRESTRICTION_BODY.getString()).replace("%target%", restrictionTarget.toString()).replace("%type%", type).replace("%duration%", duration).replace("%context%", context.toString()).replace("%players%", players.toString()).replace("%date%", date));
            }else{
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.SHOW_GLOBALRESTRICTION_BODY.getString()).replace("%target%", restrictionTarget.toString()).replace("%type%", type).replace("%duration%", duration).replace("%context%", context.toString()).replace("%date%", date));
            }

        }else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.RESTRICTION_NOT_SET.getString()).replace("%type%", restrictionTarget.toString()));
        }
    }
}
