package krisapps.portalrestrictions.portalrestrictions.commands;

import krisapps.portalrestrictions.portalrestrictions.PortalRestrictions;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionContext;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionTarget;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionType;
import krisapps.portalrestrictions.portalrestrictions.language.LocalizationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class SetRestriction implements CommandExecutor {

    PortalRestrictions main;
    public SetRestriction(PortalRestrictions main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Syntax: /setrestriction <restrictionTarget> <restrictionType> <restrictionDuration (days)> <restrictionContext>
        if (args.length >= 4){
            String target = args[0];
            String type = args[1];
            int duration = Integer.parseInt(args[2]);
            ArrayList<String> context = new ArrayList<>();
            for (int contextUnitID = 3; contextUnitID < args.length; contextUnitID++){
                context.add(args[contextUnitID]);
            }

            setRestriction(sender, RestrictionTarget.valueOf(target), RestrictionType.valueOf(type), duration, context);

        }else{
            sender.sendMessage(ChatColor.RED + "Insufficient arguments.");
            return false;
        }


        return true;
    }

    private void setRestriction(CommandSender s, RestrictionTarget target, RestrictionType type, int durationInDays, ArrayList<String> players){
        boolean isGlobalRestriction = false;

        try {
            main.restrictiondata.load(main.dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        //Check whether this restriction should be global.
        if (players.size() == 1 && players.get(0).equals("RESTRICT_GLOBAL")) { isGlobalRestriction = true;}

        if (!main.restrictiondata.contains("restrictions." + target)){
            main.restrictiondata.set("restrictions." + target + ".type", type.toString());
            main.restrictiondata.set("restrictions." + target + ".duration", durationInDays);
            main.restrictiondata.set("restrictions." + target + ".issuedOn", new Date());
            if (isGlobalRestriction){
                 main.restrictiondata.set("restrictions." + target + ".context", RestrictionContext.RESTRICT_GLOBAL.toString());
            }else{
                main.restrictiondata.set("restrictions." + target + ".context", RestrictionContext.RESTRICT_PLAYERS.toString());

                for (String playerName: players){
                    players.set(players.indexOf(playerName), Bukkit.getPlayer(playerName).getUniqueId().toString());
                }


                main.restrictiondata.set("restrictions." + target + ".restrictedFor", players);
            }

            try {
                main.restrictiondata.save(main.dataFile);
                s.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.RESTRICTION_SET_SUCCESS.getString().replace("%type%", type.toString())));
            }catch (IOException e){
                s.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.SAVE_ERROR.getString()));
                e.printStackTrace();
            }
        }else{
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.RESTRICTION_ALREADY_SET.getString().replace("%target%", target.toString())));
            return;
        }

    }
}
