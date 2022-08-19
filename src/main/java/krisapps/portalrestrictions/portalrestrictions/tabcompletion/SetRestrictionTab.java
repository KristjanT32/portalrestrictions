package krisapps.portalrestrictions.portalrestrictions.tabcompletion;

import krisapps.portalrestrictions.portalrestrictions.types.RestrictionContext;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionTarget;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetRestrictionTab implements TabCompleter {

    //Syntax: /setrestriction <restrictionTarget> <restrictionType> <restrictionDuration (days)> <restrictionContext or players[]>

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1){
            completions.clear();
            completions.add(RestrictionTarget.END_PORTAL.toString());
            completions.add(RestrictionTarget.NETHER_PORTAL.toString());
        }
        if (args.length == 2){
            completions.clear();
            completions.add(RestrictionType.DISALLOW_CREATE_PORTAL.toString());
            completions.add(RestrictionType.DISALLOW_ENTER_PORTAL.toString());
        }
        if (args.length == 4){
                completions.clear();
                completions.add(RestrictionContext.RESTRICT_GLOBAL.toString());
            if (!args[1].equals(RestrictionType.DISALLOW_CREATE_PORTAL.toString())) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getDisplayName());
                }
            }
        }

        return completions;
    }
}
