package krisapps.portalrestrictions.portalrestrictions.events;

import krisapps.portalrestrictions.portalrestrictions.PortalRestrictions;
import krisapps.portalrestrictions.portalrestrictions.language.LocalizationManager;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionContext;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionType;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.Date;

public class OnPortalEnterAttemptHandler implements Listener {

    Date date = new Date();

    PortalRestrictions main;
    public OnPortalEnterAttemptHandler(PortalRestrictions main){
        this.main = main;
    }


    @EventHandler
    public void OnPortalEvent(PlayerPortalEvent event){

        if (main.restrictiondata.getConfigurationSection("restrictions") == null) { return; }
        if (main.restrictiondata.getConfigurationSection("restrictions").getKeys(false).size() > 0){

            for (String restrictionTarget: main.restrictiondata.getConfigurationSection("restrictions").getKeys(false)) {
                if (restrictionTarget.equals(event.getCause().toString())) {
                    if (main.restrictiondata.getString("restrictions." + restrictionTarget + ".type").equals(RestrictionType.DISALLOW_ENTER_PORTAL.toString())) {

                        String context = main.restrictiondata.getString("restrictions." + restrictionTarget + ".context");
                        Date restrictionDate = main.restrictiondata.getObject("restrictions." + restrictionTarget + ".issuedOn", Date.class);
                        int duration = main.restrictiondata.getInt("restrictions." + restrictionTarget + ".duration");
                        ArrayList<String> restrictedPlayers = (ArrayList<String>) main.restrictiondata.getList("restrictions." + restrictionTarget + ".restrictedFor");

                        if (RestrictionContext.valueOf(context).equals(RestrictionContext.RESTRICT_GLOBAL)) {
                            if (restrictionDate.getDay() - date.getDay() < duration) {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.RESTRICTED_ENTERPORTAL_GLOBAL.getString()));
                                return;
                            }

                        } else if (RestrictionContext.valueOf(context).equals(RestrictionContext.RESTRICT_PLAYERS)) {
                            if (restrictionDate.getDay() - date.getDay() < duration) {
                                if (restrictedPlayers.contains(event.getPlayer().getUniqueId().toString())) {
                                    event.setCancelled(true);
                                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.RESTRICTED_ENTERPORTAL_PLAYER.getString()));
                                    return;
                                }
                            }
                        }
                        break;
                    }
                }
            }


        }else{
            return;
        }
    }

}
