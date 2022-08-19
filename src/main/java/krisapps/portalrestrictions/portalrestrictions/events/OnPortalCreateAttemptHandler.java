package krisapps.portalrestrictions.portalrestrictions.events;

import krisapps.portalrestrictions.portalrestrictions.PortalRestrictions;
import krisapps.portalrestrictions.portalrestrictions.language.LocalizationManager;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionContext;
import krisapps.portalrestrictions.portalrestrictions.types.RestrictionType;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.Date;

public class OnPortalCreateAttemptHandler implements Listener {

    Date date = new Date();

    PortalRestrictions main;
    public OnPortalCreateAttemptHandler(PortalRestrictions main){
        this.main = main;
    }


    String getPortalType(PortalCreateEvent event){
        if (event.getReason().equals(PortalCreateEvent.CreateReason.END_PLATFORM)){
            return "END_PORTAL";
        }else if (event.getReason().equals(PortalCreateEvent.CreateReason.NETHER_PAIR) || event.getReason().equals(PortalCreateEvent.CreateReason.FIRE)){
            return "NETHER_PORTAL";
        }else{
            return null;
        }
    }



    @EventHandler
    public void OnPortalEvent(PortalCreateEvent event){

        if (main.restrictiondata.getConfigurationSection("restrictions") == null) { return; }
        if (main.restrictiondata.getConfigurationSection("restrictions").getKeys(false).size() > 0){

            for (String restrictionTarget: main.restrictiondata.getConfigurationSection("restrictions").getKeys(false)) {
                if (restrictionTarget.equals(getPortalType(event))) {
                    if (main.restrictiondata.getString("restrictions." + restrictionTarget + ".type").equals(RestrictionType.DISALLOW_CREATE_PORTAL.toString())) {

                        String context = main.restrictiondata.getString("restrictions." + restrictionTarget + ".context");
                        Date restrictionDate = main.restrictiondata.getObject("restrictions." + restrictionTarget + ".issuedOn", Date.class);
                        int duration = main.restrictiondata.getInt("restrictions." + restrictionTarget + ".duration");
                        ArrayList<String> restrictedPlayers = (ArrayList<String>) main.restrictiondata.getList("restrictions." + restrictionTarget + ".restrictedFor");

                        if (RestrictionContext.valueOf(context).equals(RestrictionContext.RESTRICT_GLOBAL)) {
                            if (restrictionDate.getDay() - date.getDay() < duration) {
                                event.setCancelled(true);
                                if (event.getEntity() != null) {
                                    event.getEntity().sendMessage(ChatColor.translateAlternateColorCodes('&', LocalizationManager.RESTRICTED_CREATEPORTAL_GLOBAL.getString()));
                                }
                                return;
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
