package krisapps.portalrestrictions.portalrestrictions.tabcompletion;

import krisapps.portalrestrictions.portalrestrictions.types.RestrictionTarget;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class RemoveRestrictionTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1){
            completions.clear();
            completions.add(RestrictionTarget.END_PORTAL.toString());
            completions.add(RestrictionTarget.NETHER_PORTAL.toString());
        }


        return completions;
    }
}
